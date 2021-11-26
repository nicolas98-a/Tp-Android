package com.unaj.loginsqlite

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.widget.NestedScrollView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.helpers.InputValidation

class RegisterComplexActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterComplexActivity

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var textInputLayoutComplexName: TextInputLayout
    private lateinit var textInputLayoutComplexPhone: TextInputLayout
    private lateinit var textInputLayoutComplexLocation: TextInputLayout

    private lateinit var textInputEditTextComplexName: TextInputEditText
    private lateinit var textInputEditTextComplexPhone: TextInputEditText
    private lateinit var textInputEditTextCpmplexLocation: TextInputEditText

    private lateinit var appCompatCheckBoxParking: AppCompatCheckBox
    private lateinit var appCompatCheckBoxLockerRoom: AppCompatCheckBox
    private lateinit var appCompatCheckBoxGrill: AppCompatCheckBox

    private lateinit var appCompatButtonSaveComplex: AppCompatButton

    private lateinit var inputValidation: InputValidation

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

        textInputEditTextComplexName = findViewById(R.id.textInputEditTextComplexName)
        textInputEditTextComplexPhone = findViewById(R.id.textInputEditTextComplexPhone)
        textInputEditTextCpmplexLocation = findViewById(R.id.textInputEditTextComplexLocation)

        appCompatCheckBoxParking = findViewById(R.id.appCompatCheckBoxParking)
        appCompatCheckBoxLockerRoom = findViewById(R.id.appCompatCheckBoxLockerRoom)
        appCompatCheckBoxGrill = findViewById(R.id.appCompatCheckBoxGrill)

        appCompatButtonSaveComplex = findViewById(R.id.appCompatButtonSaveComplex)


    }

    private fun initListeners(){
        appCompatButtonSaveComplex!!.setOnClickListener(this)
    }

    private fun initObjects(){
        inputValidation = InputValidation(activity)
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
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextCpmplexLocation, textInputLayoutComplexLocation, getString(R.string.error_message_complex_location))) {
            return
        }

        AlertDialog.Builder(activity).apply {
            setTitle(R.string.save)
            setMessage(R.string.success_message_save_complex)
            setPositiveButton("Ok", DialogInterface.OnClickListener{ dialog, _ ->
                dialog.dismiss()
            })
        }.show()
    }
}