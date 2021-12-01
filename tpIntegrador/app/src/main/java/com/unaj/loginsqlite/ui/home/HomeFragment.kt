package com.unaj.loginsqlite.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.unaj.loginsqlite.Adapters.ComplexAdapter
import com.unaj.loginsqlite.LoginActivity
import com.unaj.loginsqlite.MenuActivity
import com.unaj.loginsqlite.R
import com.unaj.loginsqlite.databinding.FragmentHomeBinding
import com.unaj.loginsqlite.fragments.ComplexDetailFragment
import com.unaj.loginsqlite.helpers.UserRolApplication
import com.unaj.loginsqlite.interfaces.IComunicaFragments
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.sql.DatabaseHelper

class HomeFragment : Fragment()  {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private lateinit var activity: Activity
    lateinit var interfaceIComunicaFragments: IComunicaFragments


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        databaseHelper = DatabaseHelper(this.requireActivity())

        initRecycler()

        val btnLogOut = binding.logOut
        btnLogOut.setOnClickListener {
            UserRolApplication.prefs.wipe()
            startLogin()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycler(){
/*
        val complex1 = Complex(1,"Planeta Gol", "-34.7876912170861, -58.25886175164722", "42558965", 0, 1,1, "admin@gmail.com")
        val complex2 = Complex(2,"Sport 7", "-34.776372459147055, -58.25504980974949", "42554587", 1, 0,1, "admin2@gmail.com")
        val complex3 = Complex(3,"Mundo futbol", "-34.787644792529264, -58.2558092597688", "42544784", 1, 1,0, "admin3@gmail.com")

       databaseHelper.addComplex(complex1)
        databaseHelper.addComplex(complex2)
        databaseHelper.addComplex(complex3)
*/
        val complexs = databaseHelper.getAllComplex()
        binding.rvComplex.layoutManager = LinearLayoutManager(MenuActivity().getActivity())
        val adapter = ComplexAdapter(complexs)
        binding.rvComplex.adapter = adapter

    }


    /*override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Activity) {
            this.activity = context
            interfaceIComunicaFragments =  this.activity as IComunicaFragments
        }
    }
*/

    private fun startLogin(){
        val loginIntent = Intent(this.context, LoginActivity::class.java)

        startActivity(loginIntent)
    }

}