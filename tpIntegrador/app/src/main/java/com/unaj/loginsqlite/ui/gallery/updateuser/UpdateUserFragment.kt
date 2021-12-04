package com.unaj.loginsqlite.ui.gallery.updateuser

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.databinding.FragmentUpdateUserBinding
import com.unaj.loginsqlite.helpers.InputValidation
import com.unaj.loginsqlite.model.User
import com.unaj.loginsqlite.sql.DatabaseHelper


class UpdateUserFragment : Fragment() {

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var textInputLayoutConfirmPassword: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextConfirmPassword: TextInputEditText

    private lateinit var inputValidation: InputValidation
    private lateinit var databaseHelper: DatabaseHelper

    private var _binding: FragmentUpdateUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        //appCompatButtonUpdate.setOnClickListener {updateUser()}
        return inflater.inflate(R.layout.fragment_update_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val user_email = binding.userEmail
        val user_name = binding.userName
        val emailFromPrefs = UserRolApplication.prefs.getUserEmail()
        val nameFromPrefs = UserRolApplication.prefs.getUserName()
        user_email.setText(emailFromPrefs)
        user_name.setText(nameFromPrefs)*/


    }

    private fun updateUser() {
        validateInput()
        var user = User(
            name = textInputEditTextName!!.text.toString().trim(),
            email = textInputEditTextEmail!!.text.toString().trim(),
            password = textInputEditTextPassword!!.text.toString().trim(),
            rol = 1
        )

        databaseHelper!!.updateUser(user)


        // AlertDialog con mensaje exitoso
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle(R.string.save)
                setMessage(R.string.success_message)
                setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                    startGallery()
                })
            }.show()
        }
    }

    private fun startGallery() {
        findNavController().navigate(R.id.action_updateUserFragment_to_nav_gallery)
    }

    private fun validateInput() {
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
    }


}