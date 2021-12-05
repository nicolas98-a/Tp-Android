package com.unaj.loginsqlite

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.helpers.InputValidation
import com.unaj.loginsqlite.helpers.UserRolApplication
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

    private lateinit var appCompatCheckBox: AppCompatCheckBox
    private lateinit var appCompatButtonRegister: AppCompatButton
    private lateinit var appCompatTextViewLoginLink: AppCompatTextView

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        appCompatCheckBox = findViewById<View>(R.id.appCompatCheckBoxRol) as AppCompatCheckBox
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
            val userRol = appCompatCheckBox.isChecked
            val rol: Int = if (userRol) {
                0 // Es Administrador
            } else {
                1 // Usuario comun
            }

            var user = User(
                name = textInputEditTextName!!.text.toString().trim(),
                email = textInputEditTextEmail!!.text.toString().trim(),
                password = textInputEditTextPassword!!.text.toString().trim(),
                rol = rol
            )

            databaseHelper!!.addUser(user)

            // AlertDialog con mensaje exitoso
            AlertDialog.Builder(activity).apply {
                setTitle(R.string.text_register)
                setMessage(R.string.success_message)
                setIcon(R.drawable.logo_perfil)
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    if (user.rol == 0){
                        startSaveComplexActivity(textInputEditTextEmail!!.text.toString().trim())
                    }else {
                        startLogin()
                    }
                })
            }.show()

        } else {
            // Mensaje de error ya existe el usuario
            Snackbar.make(nestedScrollView!!, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
            AlertDialog.Builder(activity).apply {
                setTitle(R.string.text_register)
                setMessage(R.string.error_email_exists)
                setIcon(R.drawable.icons_eliminar_48)
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            }.show()
        }
    }

    // Vacio los inputs
    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
        textInputEditTextConfirmPassword!!.text = null
    }

    private fun startSaveComplexActivity(email: String){
        val saveComplexIntent = Intent(activity, RegisterComplexActivity::class.java)
        saveComplexIntent.putExtra("EMAIL", email)
        emptyInputEditText()
        startActivity(saveComplexIntent)
    }

    private fun startLogin(){
        val loginIntent = Intent(activity, LoginActivity::class.java)
        UserRolApplication.prefs.saveUserName(textInputEditTextName!!.text.toString().trim { it <= ' '})
        emptyInputEditText()
        startActivity(loginIntent)
    }
}
