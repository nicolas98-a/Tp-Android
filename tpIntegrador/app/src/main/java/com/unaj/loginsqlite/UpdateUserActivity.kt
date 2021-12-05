package com.unaj.loginsqlite

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.helpers.InputValidation
import com.unaj.loginsqlite.helpers.UserRolApplication
import com.unaj.loginsqlite.model.User
import com.unaj.loginsqlite.sql.DatabaseHelper

class UpdateUserActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@UpdateUserActivity

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var textInputLayoutConfirmPassword: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextConfirmPassword: TextInputEditText

    private lateinit var appCompatButtonUpdate: AppCompatButton

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        // inicializo las vistas
        initViews()

        // inicializo los listeners
        initListeners()

        // inicilizo los objetos que voy a usar
        initObjects()
    }

    private fun initListeners() {
        appCompatButtonUpdate!!.setOnClickListener(this)
    }

    private fun initObjects() {
        inputValidation = InputValidation(activity)
        databaseHelper = DatabaseHelper(activity)
    }

    // sobreescribo para escuchar el click en la view
    override fun onClick(v: View) {
        when (v.id) {
            R.id.appCompatButtonUpdate -> postDataToSQLite()
        }
    }

    private fun initViews() {
        nestedScrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView

        textInputLayoutName = findViewById<View>(R.id.textInputLayoutName) as TextInputLayout
        textInputLayoutEmail = findViewById<View>(R.id.textInputLayoutEmail) as TextInputLayout
        textInputLayoutPassword = findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout
        textInputLayoutConfirmPassword = findViewById<View>(R.id.textInputLayoutConfirmPassword) as TextInputLayout

        textInputEditTextName = findViewById<View>(R.id.textInputEditTextName) as TextInputEditText
        textInputEditTextEmail = findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        textInputEditTextPassword = findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText
        textInputEditTextConfirmPassword = findViewById<View>(R.id.textInputEditTextConfirmPassword) as TextInputEditText

        appCompatButtonUpdate = findViewById<View>(R.id.appCompatButtonUpdate) as AppCompatButton
    }

    // Valido los inputs y guardo un usuario
    private fun postDataToSQLite() {
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(
                R.string.error_message_name
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(
                R.string.error_message_email_not_filled
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(
                R.string.error_message_email
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(
                R.string.error_message_password
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword, textInputLayoutConfirmPassword,
                getString(R.string.error_password_match))) {
            return
        }


            var user = User(
                name = textInputEditTextName!!.text.toString().trim(),
                email = textInputEditTextEmail!!.text.toString().trim(),
                password = textInputEditTextPassword!!.text.toString().trim(),
                rol = 1
            )

            databaseHelper!!.updateUser(user)

            UserRolApplication.prefs.saveUserName(textInputEditTextName!!.text.toString().trim { it <= ' '})

            // AlertDialog con mensaje exitoso
            AlertDialog.Builder(activity).apply {
                setTitle(R.string.save)
                setMessage(R.string.change_message)
                setIcon(R.drawable.logo_perfil)
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                        startLogin()
                })
            }.show()

    }

    // Vacio los inputs
    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
        textInputEditTextConfirmPassword!!.text = null
    }


    private fun startLogin(){
        val loginIntent = Intent(activity, LoginActivity::class.java)

        emptyInputEditText()

        startActivity(loginIntent)
    }
}