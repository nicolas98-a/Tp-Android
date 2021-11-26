package com.unaj.loginsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.unaj.loginsqlite.helpers.UserRolApplication.Companion.prefs
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.sql.DatabaseHelper

class UsersListActivity : AppCompatActivity() {
    private lateinit var textViewUserEmail: TextView
    private lateinit var textViewUserRol: TextView
    private lateinit var btnLogOut: Button
    private lateinit var btnMap: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

       // initViews()
        textViewUserEmail = findViewById<View>(R.id.textViewUserEmail) as TextView
        textViewUserRol = findViewById<View>(R.id.textViewUserRol) as TextView
        btnLogOut = findViewById(R.id.btnLogOut)
        btnMap = findViewById(R.id.mostrarMap)

        databaseHelper = DatabaseHelper(this)

        //val emailFromIntent = intent.getStringExtra("EMAIL")
        //textViewUserEmail.text = emailFromIntent

        btnLogOut.setOnClickListener {
            prefs.wipe()
            onBackPressed()
        }

        btnMap.setOnClickListener {
           val mapIntent = Intent(this, GoogleMapActivity::class.java)
            startActivity(mapIntent)
        }

        val emailFromPrefs = prefs.getUserEmail()
        val rolInt = databaseHelper.getUserRol(emailFromPrefs)

        val rol: String = if (rolInt == 0){
            "Administrador"
        }else{
            "Usuario"
        }
        textViewUserEmail.text = emailFromPrefs
        textViewUserRol.text = rol
    }

    private fun initViews() {
        textViewUserEmail = findViewById<View>(R.id.textViewUserEmail) as TextView
    }
}
