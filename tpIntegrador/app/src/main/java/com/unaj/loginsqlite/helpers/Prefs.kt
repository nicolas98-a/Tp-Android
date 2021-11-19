package com.unaj.loginsqlite.helpers

import android.content.Context

class Prefs (val context:Context){

    val SHARED_NAME = "Mydb"
    val SHARED_USER_Email = "useremail"
    val SHARED_ROL = "rol"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveUserEmail(email:String){
        storage.edit().putString(SHARED_USER_Email, email).apply()
    }

    fun saveUserRol(rol:Boolean){
        storage.edit().putBoolean(SHARED_ROL, rol).apply()
    }

    fun getUserEmail():String{
       return storage.getString(SHARED_USER_Email, "")!!
    }

    fun getUserRol():Boolean{
        // Si el user no es admin, por defecto devuelve false
        return storage.getBoolean(SHARED_ROL, false)
    }

    fun wipe(){
        storage.edit().clear().apply()
    }


}