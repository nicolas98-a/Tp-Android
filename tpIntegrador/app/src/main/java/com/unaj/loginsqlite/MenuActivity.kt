package com.unaj.loginsqlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.unaj.loginsqlite.databinding.ActivityMenuBinding
import com.unaj.loginsqlite.helpers.UserRolApplication
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.model.Field
import com.unaj.loginsqlite.sql.DatabaseHelper

class MenuActivity : AppCompatActivity() {

    private val activity = this@MenuActivity

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.contactFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerView = navView.getHeaderView(0)

        val user_name = headerView.findViewById<TextView>(R.id.user_name)
        val nameFromPrefs = UserRolApplication.prefs.getUserName()
        user_name.text = nameFromPrefs;

        val user_email = headerView.findViewById<TextView>(R.id.user_email)
        val emailFromPrefs = UserRolApplication.prefs.getUserEmail()
        user_email.text = emailFromPrefs

        val exitItem = navView.menu.findItem(R.id.logOut)
        exitItem.setOnMenuItemClickListener {
            UserRolApplication.prefs.wipe()
            startLogin()
            true
        }

        databaseHelper = DatabaseHelper(this)
        //cargarCanchas()
        //cargarComplejos()

    }

    private fun startLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)

        startActivity(loginIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)

        val menuItem = menu.getItem(0)
        val spannable = SpannableString(menuItem.title.toString())
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.black)), 0, spannable.length, 0)
        menuItem.title = spannable

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> chooseIdiom()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getActivity(): Activity{
        return activity
    }

    private fun chooseIdiom() {
        val intentIdiom = Intent(Intent.ACTION_MAIN)
        intentIdiom.setClassName("com.android.settings", "com.android.settings.LanguageSettings")
        startActivity(intentIdiom)
    }

    private fun cargarComplejos() {

        val complex1 = Complex(1,"Planeta Gol", "-34.7876912170861, -58.25886175164722", "42558965", 0, 1,1, "admin@gmail.com")
        val complex2 = Complex(2,"Sport 7", "-34.776372459147055, -58.25504980974949", "42554587", 1, 0,1, "admin2@gmail.com")
        val complex3 = Complex(3,"Mundo futbol", "-34.787644792529264, -58.2558092597688", "42544784", 1, 1,0, "admin3@gmail.com")

        databaseHelper.addComplex(complex1)
        databaseHelper.addComplex(complex2)
        databaseHelper.addComplex(complex3)
    }
    private fun cargarCanchas() {
        val field1 = Field(1, "Planeta Gol", 1500, 1, 0, 1)
        val field2 = Field(1, "Sport 7", 1200, 1, 1, 1)
        val field3 = Field(1, "Mundo futbol", 1300, 0, 0, 1)

        databaseHelper.addField(field1)
        databaseHelper.addField(field2)
        databaseHelper.addField(field3)
    }


}