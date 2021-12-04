package com.unaj.loginsqlite.ui.gallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.databinding.FragmentGalleryBinding
import com.unaj.loginsqlite.helpers.UserRolApplication
import androidx.navigation.fragment.findNavController

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        UserRolApplication.prefs.saveUserName("Chris")
        UserRolApplication.prefs.saveUserEmail("chrisbotta1@gmail.com")
        val user_name = binding.userName
        val nameFromPrefs = UserRolApplication.prefs.getUserName()
        user_name.text = nameFromPrefs;

        val user_email = binding.userEmail
        val emailFromPrefs = UserRolApplication.prefs.getUserEmail()
        user_email.text = emailFromPrefs


        binding.button.setOnClickListener {requestPermission()}
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateDates.setOnClickListener {findNavController().navigate(R.id.action_nav_gallery_to_updateUserFragment)}
    }



    private fun requestPermission() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickPhotoFromGallery()
                }

                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            pickPhotoFromGallery()
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){isGranted ->

        if (isGranted){
            pickPhotoFromGallery()
        } else{
            Toast.makeText(requireActivity(),"Se necesitan habilitar los permisos",Toast.LENGTH_SHORT).show()
        }

    }

    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->

        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            binding.imageView.setImageURI(data)
        }

    }

    private fun pickPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/+"
        startForActivityGallery.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}