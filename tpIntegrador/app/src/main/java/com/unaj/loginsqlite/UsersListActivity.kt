package com.unaj.loginsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class UsersListActivity : AppCompatActivity() {
    private lateinit var textViewUserEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

       // initViews()
        textViewUserEmail = findViewById<View>(R.id.textViewUserEmail) as TextView
        val emailFromIntent = intent.getStringExtra("EMAIL")
        textViewUserEmail.text = emailFromIntent
    }

    private fun initViews() {
        textViewUserEmail = findViewById<View>(R.id.textViewUserEmail) as TextView
    }
}
