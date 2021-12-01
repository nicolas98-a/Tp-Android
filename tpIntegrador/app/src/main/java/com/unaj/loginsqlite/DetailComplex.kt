package com.unaj.loginsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.unaj.loginsqlite.model.Complex

class DetailComplex : AppCompatActivity() {

    private lateinit var detailComplexName: TextView
    private lateinit var detailComplexPhone: TextView
    private lateinit var btnViewMap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_complex)

        detailComplexName = findViewById(R.id.actDetailComplexName)
        detailComplexPhone = findViewById(R.id.actDetailComplexPhone)
        btnViewMap = findViewById(R.id.viewMap)

        val complex = intent.getSerializableExtra("objectComplex") as Complex

        detailComplexName.text = complex.name
        detailComplexPhone.text = complex.phone


        btnViewMap.setOnClickListener {
            val mapIntent = Intent(this, GoogleMapActivity::class.java)
            startActivity(mapIntent)
        }
    }
}