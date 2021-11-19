package com.unaj.loginsqlite.helpers

import android.app.Application

class UserRolApplication : Application() {

    companion object{
        lateinit var prefs:Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}