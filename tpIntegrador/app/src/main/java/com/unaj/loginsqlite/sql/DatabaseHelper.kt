package com.unaj.loginsqlite.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    // Creo la tabla usuario
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_NAME + " TEXT," +
            COLUMN_USER_EMAIL + " TEXT," +
            COLUMN_USER_PASSWORD + " TEXT, " +
            COLUMN_USER_ROL + "INTEGER DEFAULT -1" + ")")

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"
    private val ALTER_USER_TABLE = "ALTER TABLE "+TABLE_USER + " ADD "+COLUMN_USER_ROL +" INTEGER DEFAULT -1"

    // Creo la tabla complejo
    private val CREATE_COMPLEX_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_COMPLEX + "(" +
            COLUMN_COMPLEX_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_COMPLEX_NAME + " TEXT," +
            COLUMN_COMPLEX_LOCATION + " TEXT," +
            COLUMN_COMPLEX_PHONE + " TEXT," +
            COLUMN_COMPLEX_PARKING + " INTEGER," +
            COLUMN_COMPLEX_LOCKER_ROOM + " INTEGER," +
            COLUMN_COMPLEX_GRILL + " INTEGER, " +
            COLUMN_COMPLEX_ADMIN_EMAIL + " TEXT" + ")")

    private val DROP_COMPLEX_TABLE = "DROP TABLE IF EXISTS $TABLE_COMPLEX"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_COMPLEX_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borrar la base si existe
        //db.execSQL(DROP_USER_TABLE)

        //db.execSQL(DROP_COMPLEX_TABLE)
        db.execSQL(CREATE_COMPLEX_TABLE)
        db.execSQL(ALTER_USER_TABLE)
        // Crear las tablas de vuelta
        //onCreate(db)
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
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)),
                    rol = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROL)).toInt()
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
        values.put(COLUMN_USER_ROL, user.rol)

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

    @SuppressLint("Range")
    fun getUserRol(email: String): Int{
        val columns = arrayOf(COLUMN_USER_ROL)
        val db = this.readableDatabase

        // criterio de seleccion
        val selection = "$COLUMN_USER_EMAIL = ?"

        // argumento de seleccion
        val selectionArgs = arrayOf(email)

        // query: SELECT user_rol FROM user WHERE user_email = 'ejemplo@mail.com';
        val cursor = db.query(TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        if (cursor.moveToFirst()){
           return cursor.getString(cursor.getColumnIndex(COLUMN_USER_ROL)).toInt()
        }
        return -1
    }

    // Agregar un complejo
    fun addComplex(complex: Complex) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_COMPLEX_NAME, complex.name)
        values.put(COLUMN_COMPLEX_LOCATION, complex.location.toString())
        values.put(COLUMN_COMPLEX_PHONE, complex.phone)
        values.put(COLUMN_COMPLEX_PARKING, complex.parking)
        values.put(COLUMN_COMPLEX_LOCKER_ROOM, complex.lockerRoom)
        values.put(COLUMN_COMPLEX_GRILL, complex.grill)
        values.put(COLUMN_COMPLEX_ADMIN_EMAIL, complex.adminEmail)


        // Insertar fila
        db.insert(TABLE_COMPLEX, null, values)
        db.close()
    }

    // Actualizar complejo
    fun updateComplex(complex: Complex) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_COMPLEX_NAME, complex.name)
        values.put(COLUMN_COMPLEX_LOCATION, complex.location.toString())
        values.put(COLUMN_COMPLEX_PHONE, complex.phone)
        values.put(COLUMN_COMPLEX_PARKING, complex.parking)
        values.put(COLUMN_COMPLEX_LOCKER_ROOM, complex.lockerRoom)
        values.put(COLUMN_COMPLEX_GRILL, complex.grill)
        values.put(COLUMN_COMPLEX_ADMIN_EMAIL, complex.adminEmail)

        // actualizar fila
        db.update(
            TABLE_COMPLEX, values, "$COLUMN_USER_ID = ?",
            arrayOf(complex.id.toString()))
        db.close()
    }

    // Borrar complejo
    fun deleteComplex(complex: Complex) {
        val db = this.writableDatabase
        db.delete(
            TABLE_COMPLEX, "$COLUMN_USER_ID = ?",
            arrayOf(complex.id.toString()))

        db.close()
    }

    // Obtener una lista de complejos
    @SuppressLint("Range")
    fun getAllComplex(): List<Complex> {
        val columns = arrayOf(COLUMN_COMPLEX_ID, COLUMN_COMPLEX_NAME, COLUMN_COMPLEX_LOCATION,
            COLUMN_COMPLEX_PHONE, COLUMN_COMPLEX_PARKING, COLUMN_COMPLEX_LOCKER_ROOM,
            COLUMN_COMPLEX_GRILL, COLUMN_COMPLEX_ADMIN_EMAIL)
        val sortOrder = "$COLUMN_COMPLEX_NAME ASC"
        val complexList = arrayListOf<Complex>()

        val db = this.readableDatabase

        // query a la tabla user
        val cursor = db.query(TABLE_COMPLEX,
            columns,
            null,
            null,
            null,
            null,
            sortOrder)

        if (cursor.moveToFirst()){
            do {
                var loc = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_LOCATION))
                val delimiter = ","
                var locParts = loc.split(delimiter)

                val complex = Complex(
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_NAME)),
                    location = LatLng(locParts[0].toDouble(), locParts[1].toDouble()),
                    phone = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_PHONE)),
                    parking = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_PARKING)).toInt(),
                    lockerRoom = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_LOCKER_ROOM)).toInt(),
                    grill = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_GRILL)).toInt(),
                    adminEmail = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_ADMIN_EMAIL))
                )
                complexList.add(complex)
            }  while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return complexList
    }

    @SuppressLint("Range")
    fun getComplexByAdminEmail(email: String): Complex? {
        val db = this.readableDatabase

        val columns = arrayOf(COLUMN_COMPLEX_ID, COLUMN_COMPLEX_NAME, COLUMN_COMPLEX_LOCATION,
            COLUMN_COMPLEX_PHONE, COLUMN_COMPLEX_PARKING, COLUMN_COMPLEX_LOCKER_ROOM,
            COLUMN_COMPLEX_GRILL, COLUMN_COMPLEX_ADMIN_EMAIL)

        // criterio de seleccion
        val selection = "$COLUMN_COMPLEX_ADMIN_EMAIL = ?"

        // argumento de seleccion
        val selectionArgs = arrayOf(email)

        // query: SELECT * FROM cpmplex WHERE complex_admin_email = 'ejemplo@mail.com';
        val cursor = db.query(TABLE_COMPLEX,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null)

        if (cursor.moveToFirst()) {
            var loc = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_LOCATION))
            val delimiter = ","
            var locParts = loc.split(delimiter)

            return Complex(
                id = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_ID)).toInt(),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_NAME)),
                location = LatLng(locParts[0].toDouble(), locParts[1].toDouble()),
                phone = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_PHONE)),
                parking = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_PARKING)).toInt(),
                lockerRoom = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_LOCKER_ROOM))
                    .toInt(),
                grill = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_GRILL)).toInt(),
                adminEmail = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLEX_ADMIN_EMAIL))
            )
        }
        return null
    }

    companion object {
        private val DATABASE_VERSION = 6
        private val DATABASE_NAME = "UserManager.db"

        // User table name
        private val TABLE_USER = "user"
        // User Table Columns name
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"
        private val COLUMN_USER_ROL = "user_rol"

        // COMPLEX TABLE NAME
        private val TABLE_COMPLEX = "complex"
        // Complex table columns name
        private val COLUMN_COMPLEX_ID = "complex_id"
        private val COLUMN_COMPLEX_NAME = "complex_name"
        private val COLUMN_COMPLEX_LOCATION = "complex_location"
        private val COLUMN_COMPLEX_PHONE = "complex_phone"
        private val COLUMN_COMPLEX_PARKING = "complex_parking"
        private val COLUMN_COMPLEX_LOCKER_ROOM = "complex_locker_room"
        private val COLUMN_COMPLEX_GRILL = "complex_grill"
        private val COLUMN_COMPLEX_ADMIN_EMAIL = "complex_admin_email"
    }
}


