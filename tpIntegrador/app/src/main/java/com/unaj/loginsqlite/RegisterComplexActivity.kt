package com.unaj.loginsqlite

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.widget.NestedScrollView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.helpers.InputValidation
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.model.Field
import com.unaj.loginsqlite.sql.DatabaseHelper

class RegisterComplexActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterComplexActivity

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var textInputLayoutComplexName: TextInputLayout
    private lateinit var textInputLayoutComplexPhone: TextInputLayout
    private lateinit var textInputLayoutComplexLocation: TextInputLayout
    private lateinit var textInputLayoutFieldPrice: TextInputLayout

    private lateinit var textInputEditTextComplexName: TextInputEditText
    private lateinit var textInputEditTextComplexPhone: TextInputEditText
    private lateinit var textInputEditTextComplexLocation: TextInputEditText
    private lateinit var textInputEditTextFieldPrice: TextInputEditText

    private lateinit var appCompatCheckBoxParking: AppCompatCheckBox
    private lateinit var appCompatCheckBoxLockerRoom: AppCompatCheckBox
    private lateinit var appCompatCheckBoxGrill: AppCompatCheckBox
    private lateinit var appCompatCheckBoxFieldIllumination: AppCompatCheckBox
    private lateinit var appCompatCheckBoxFieldCovered: AppCompatCheckBox
    private lateinit var appCompatCheckBoxFieldSynthetic: AppCompatCheckBox

    private lateinit var appCompatButtonSaveComplex: AppCompatButton

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complex)

        // inicializo las vistas
        initViews()

        // inicializo los listeners
        initListeners()

        // inicilizo los objetos que voy a usar
        initObjects()
    }

    private fun initViews(){
        nestedScrollView = findViewById(R.id.nestedScrollView)

        textInputLayoutComplexName = findViewById(R.id.textInputLayoutComplexName)
        textInputLayoutComplexPhone = findViewById(R.id.textInputLayoutComplexPhone)
        textInputLayoutComplexLocation = findViewById(R.id.textInputLayoutComplexLocation)
        textInputLayoutFieldPrice = findViewById(R.id.textInputLayoutFieldPrice)

        textInputEditTextComplexName = findViewById(R.id.textInputEditTextComplexName)
        textInputEditTextComplexPhone = findViewById(R.id.textInputEditTextComplexPhone)
        textInputEditTextComplexLocation = findViewById(R.id.textInputEditTextComplexLocation)
        textInputEditTextFieldPrice = findViewById(R.id.textInputEditTextFieldPrice)

        appCompatCheckBoxParking = findViewById(R.id.appCompatCheckBoxParking)
        appCompatCheckBoxLockerRoom = findViewById(R.id.appCompatCheckBoxLockerRoom)
        appCompatCheckBoxGrill = findViewById(R.id.appCompatCheckBoxGrill)
        appCompatCheckBoxFieldIllumination = findViewById(R.id.appCompatCheckBoxFieldIllumination)
        appCompatCheckBoxFieldCovered = findViewById(R.id.appCompatCheckBoxFieldCovered)
        appCompatCheckBoxFieldSynthetic = findViewById(R.id.appCompatCheckBoxFieldSynthetic)

        appCompatButtonSaveComplex = findViewById(R.id.appCompatButtonSaveComplex)


    }

    private fun initListeners(){
        appCompatButtonSaveComplex!!.setOnClickListener(this)
    }

    private fun initObjects(){
        inputValidation = InputValidation(activity)
        databaseHelper = DatabaseHelper(activity)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.appCompatButtonSaveComplex -> saveComplex()
        }
    }

    private fun saveComplex() {
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextComplexName, textInputLayoutComplexName, getString(R.string.error_message_complex_name))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextComplexPhone, textInputLayoutComplexPhone, getString(R.string.error_message_complex_phone))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextComplexLocation, textInputLayoutComplexLocation, getString(R.string.error_message_complex_location))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextFieldPrice, textInputLayoutFieldPrice, getString(R.string.error_price))) {
            return
        }

        val complexParking = appCompatCheckBoxParking.isChecked
        val parking: Int = if (!complexParking) {
            0 // false
        } else {
            1 // true
        }

        val complexLockerRoom = appCompatCheckBoxLockerRoom.isChecked
        val lockerRoom: Int = if (!complexLockerRoom) {
            0 // false
        } else {
            1 // true
        }

        val complexGrill= appCompatCheckBoxGrill.isChecked
        val grill: Int = if (!complexGrill) {
            0 // false
        } else {
            1 // true
        }

        val fieldIllumination = appCompatCheckBoxFieldIllumination.isChecked
        val illumination: Int = if (!fieldIllumination){
            0 // false
        } else {
            1 //true
        }

        val fieldCovered = appCompatCheckBoxFieldCovered.isChecked
        val covered: Int = if (!fieldCovered){
            0 // false
        } else {
            1 //true
        }

        val fieldSynthetic = appCompatCheckBoxFieldSynthetic.isChecked
        val synthetic: Int = if (!fieldSynthetic) 0 else 1

        val emailFromIntent = intent.getStringExtra("EMAIL")


        val complex = Complex(
            name = textInputEditTextComplexName.text.toString(),
            location = textInputEditTextComplexLocation.text.toString(),
            phone = textInputEditTextComplexPhone.text.toString(),
            parking = parking,
            lockerRoom = lockerRoom,
            grill = grill,
            adminEmail = emailFromIntent.toString()
        )

        val field = Field(
            complexName = textInputEditTextComplexName.text.toString(),
            price = textInputEditTextFieldPrice.text.toString().toInt(),
            illumination = illumination,
            covered = covered,
            synthetic = synthetic
        )

        val rtaComplex = databaseHelper.addComplex(complex)
        val rtaField = databaseHelper.addField(field)

        if (rtaComplex == -1 || rtaField == -1){
            // Fallo al guardar en DB
            AlertDialog.Builder(this).apply {
                setTitle(R.string.save)
                setMessage(R.string.error_database)
                setPositiveButton("Ok", DialogInterface.OnClickListener{ dialog, _ ->
                    dialog.dismiss()
                    emptyInputEditText()
                })
            }.show()

        } else {
            AlertDialog.Builder(activity).apply {
                setTitle(R.string.save)
                setMessage(R.string.success_message_save_complex)
                setPositiveButton("Ok", DialogInterface.OnClickListener{ dialog, _ ->
                    dialog.dismiss()
                    startLogin()
                })
            }.show()
        }

    }

    private fun startLogin(){
        val loginIntent = Intent(activity, LoginActivity::class.java)
        startActivity(loginIntent)
    }

    private fun emptyInputEditText() {
        textInputEditTextComplexName!!.text = null
        textInputEditTextComplexPhone!!.text = null
        textInputEditTextComplexLocation!!.text = null
        textInputEditTextFieldPrice!!.text = null

        appCompatCheckBoxParking!!.isChecked = false
        appCompatCheckBoxLockerRoom!!.isChecked = false
        appCompatCheckBoxGrill!!.isChecked = false
        appCompatCheckBoxFieldIllumination!!.isChecked = false
        appCompatCheckBoxFieldCovered!!.isChecked = false
        appCompatCheckBoxFieldSynthetic!!.isChecked = false
    }
}