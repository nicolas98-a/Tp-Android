package com.unaj.loginsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.helpers.InputValidation
import com.unaj.loginsqlite.model.User
import com.unaj.loginsqlite.sql.DatabaseHelper

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private val activity = this@RegisterActivity

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var textInputLayoutConfirmPassword: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextConfirmPassword: TextInputEditText

    private lateinit var appCompatButtonRegister: AppCompatButton
    private lateinit var appCompatTextViewLoginLink: AppCompatTextView

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // oculto el action bar
        supportActionBar!!.hide()

        // inicializo las vistas
        initViews()

        // inicializo los listeners
        initListeners()

        // inicilizo los objetos que voy a usar
        initObjects()
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

        appCompatButtonRegister = findViewById<View>(R.id.appCompatButtonRegister) as AppCompatButton

        appCompatTextViewLoginLink = findViewById<View>(R.id.appCompatTextViewLoginLink) as AppCompatTextView

    }

    private fun initListeners() {
        appCompatButtonRegister!!.setOnClickListener(this)
        appCompatTextViewLoginLink!!.setOnClickListener(this)
    }

    private fun initObjects() {
        inputValidation = InputValidation(activity)
        databaseHelper = DatabaseHelper(activity)
    }

    // sobreescribo para escuchar el click en la view
    override fun onClick(v: View) {
        when (v.id) {
           R.id.appCompatButtonRegister -> postDataToSQLite()
           R.id.appCompatTextViewLoginLink -> finish()
        }
    }

    // Valido los inputs y guardo un usuario
    private fun postDataToSQLite() {
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email_not_filled))) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return
        }
        if (!inputValidation!!.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword, textInputLayoutConfirmPassword,
            getString(R.string.error_password_match))) {
            return
        }

        if (!databaseHelper!!.checkUser(textInputEditTextEmail!!.text.toString().trim())) {
            var user = User(
                name = textInputEditTextName!!.text.toString().trim(),
                email = textInputEditTextEmail!!.text.toString().trim(),
                password = textInputEditTextPassword!!.text.toString().trim()
            )

            databaseHelper!!.addUser(user)

            // SnackBar con mensaje de registro exitoso
            Snackbar.make(nestedScrollView!!, getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
            emptyInputEditText()
        } else {
            // Mensaje de error ya existe el usuario
            Snackbar.make(nestedScrollView!!, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
        }
    }

    // Vacio los inputs
    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
        textInputEditTextConfirmPassword!!.text = null
    }
}
