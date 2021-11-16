package com.unaj.loginsqlite.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.unaj.loginsqlite.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    // Creo la tabla usuario
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_NAME + " TEXT," +
            COLUMN_USER_EMAIL + " TEXT," +
            COLUMN_USER_PASSWORD + " TEXT" + ")")

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"




    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borrar la base si existe
        db.execSQL(DROP_USER_TABLE)

        // Crear las tablas de vuelta
        onCreate(db)
    }

    // Obtener una lista de usuarios
    @SuppressLint("Range")
    fun getAllUsers(): List<User> {
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_NAME, COLUMN_USER_PASSWORD)
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = arrayListOf<User>()

        val db = this.readableDatabase

        // query a la tabla user
        val cursor = db.query(TABLE_USER,
        columns,
        null,
        null,
        null,
        null,
        sortOrder)

        if (cursor.moveToFirst()){
            do {
                val user = User(
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))
                )
                userList.add(user)
            }  while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    // Agregar un usuario
    fun addUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // Insertar fila
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    // Actualizar usuario
    fun updateUser(user: User) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // actualizar fila
        db.update(TABLE_USER, values, "$COLUMN_USER_ID = ?",
        arrayOf(user.id.toString()))
        db.close()
    }

    // Borrar usuario
    fun deleteUser(user: User) {
        val db = this.writableDatabase
        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?",
        arrayOf(user.id.toString()))

        db.close()
    }

    // Chequear si existe el usuario
    fun checkUser(email: String): Boolean {
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        // criterio de seleccion
        val selection = "$COLUMN_USER_EMAIL = ?"

        // argumento de seleccion
        val selectionArgs = arrayOf(email)

        // query: SELECT user_id FROM user WHERE user_email = 'ejemplo@mail.com';
        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }

        return false
    }

    // Chequeo si existe el usuario
    fun checkUser(email: String, password: String): Boolean {

        val columns = arrayOf(COLUMN_USER_ID)

        val db = this.readableDatabase

        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"

        val selectionArgs = arrayOf(email, password)

        // query SELECT user_id FROM user WHERE user_email = 'ejemplo@mail.com' AND user_password = 'qwerty';

        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        if (cursorCount > 0) {
            return true
        }

        return false

    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "UserManager.db"

        // User table name
        private val TABLE_USER = "user"
        // User Table Columns name
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"

    }
}