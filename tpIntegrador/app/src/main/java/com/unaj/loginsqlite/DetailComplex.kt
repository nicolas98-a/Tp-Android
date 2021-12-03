package com.unaj.loginsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.model.Field

class DetailComplex : AppCompatActivity() {

    private lateinit var detailComplexName: TextView
    private lateinit var detailComplexPhone: TextView
    private lateinit var detailComplexIvParking: ImageView
    private lateinit var detailComplexIvLockerRoom: ImageView
    private lateinit var detailComplexIvGrill: ImageView
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var textViewIllumination: TextView
    private lateinit var textViewCovered: TextView
    private lateinit var textViewSynthetic: TextView
    private lateinit var textViewFieldPrice: TextView

    private lateinit var btnViewMap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_complex)

        initViews()

        //

        val field = Field(1, "Planeta Gol", 1500, 1, 0, 1)
        renderField(field)
        //

        val complexFromIntent = intent.getSerializableExtra("objectComplex") as Complex

        renderComplexDetail(complexFromIntent)

        editTextDate.setOnClickListener { showDatePickerDialog() }
        editTextTime.setOnClickListener { showTimePickerDialog() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)

        for (i in 0 until (menu.size())) {

            val menuItem = menu.getItem(i)
            val spannable = SpannableString(menuItem.title.toString())
            spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.black)), 0, spannable.length, 0)
            menuItem.title = spannable

        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> chooseIdiom()
            R.id.action_go_home -> startMenuActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderComplexDetail(complex: Complex) {
        detailComplexName.text = complex.name
        detailComplexPhone.text = complex.phone

        if (complex.parking == 0){
            detailComplexIvParking.setImageResource(R.drawable.iconsnoestacionar60)
        }
        if (complex.lockerRoom == 0){
            detailComplexIvLockerRoom.setImageResource(R.drawable.icons_no_changing_room_60)
        }
        if (complex.grill == 0){
            detailComplexIvGrill.setImageResource(R.drawable.icons_no_parrilla_50)
        }

        btnViewMap.setOnClickListener {
            val mapIntent = Intent(this, GoogleMapActivity::class.java)
            mapIntent.putExtra("LOCATION", complex.location)
            startActivity(mapIntent)
        }
    }

    private fun renderField(field: Field) {
        textViewFieldPrice.text = "$" + field.price.toString()

        if (field.illumination == 0) {
            textViewIllumination.setTextColor(resources.getColor(R.color.red))
            textViewIllumination.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.icons_eliminar_48), null, null,null)
        }
        if (field.covered == 0) {
            textViewCovered.setTextColor(resources.getColor(R.color.red))
            textViewCovered.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.icons_eliminar_48), null, null,null)
        }
        if (field.synthetic == 0) {
            textViewSynthetic.setTextColor(resources.getColor(R.color.red))
            textViewSynthetic.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.icons_eliminar_48),null,null,null)

        }
    }

    private fun initViews() {
        detailComplexName = findViewById(R.id.actDetailComplexName)
        detailComplexPhone = findViewById(R.id.actDetailComplexPhone)
        detailComplexIvParking = findViewById(R.id.ivComplexParking)
        detailComplexIvLockerRoom = findViewById(R.id.ivComplexLockerRoom)
        detailComplexIvGrill = findViewById(R.id.ivComplexGrill)
        editTextDate = findViewById(R.id.etDate)
        editTextTime = findViewById(R.id.etTime)
        textViewIllumination = findViewById(R.id.textViewIluminacion)
        textViewCovered = findViewById(R.id.textViewCovered)
        textViewSynthetic = findViewById(R.id.textViewSynthetic)
        textViewFieldPrice = findViewById(R.id.fieldPrice)

        btnViewMap = findViewById(R.id.viewMap)
    }

    private fun showDatePickerDialog() {

        val datePicker = DatePickerFragment{ day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        editTextDate.setText("$day/$month/$year")
    }

    private fun showTimePickerDialog() {

        val timePicker = TimePickerFragment { onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String) {
        editTextTime.setText(time)
    }
    private fun startMenuActivity() {
        val intentMenuActivity = Intent(this, MenuActivity::class.java)
        startActivity(intentMenuActivity)
    }

    private fun chooseIdiom() {
        val intentIdiom = Intent(Intent.ACTION_MAIN)
        intentIdiom.setClassName("com.android.settings", "com.android.settings.LanguageSettings")
        startActivity(intentIdiom)
    }
}