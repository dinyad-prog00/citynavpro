package com.dinyad.citynav
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.viewmodels.SharedViewModel
import android.content.Intent
import android.widget.Button
import com.dinyad.citynav.loginRepository.LoginEmail
import com.dinyad.citynav.loginRepository.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    lateinit var sharedViewModel: SharedViewModel
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Initialize SharedViewModel
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
            sharedViewModel.mainActivity.value = this

            GooglePlacesService.checkPermissionThenRequestIfNot(this,this)

            if (savedInstanceState == null) {
                setupNavigation()
            }
            /*
            val btnGoToRegister = findViewById<Button>(R.id.btnRegister)

            btnGoToRegister.setOnClickListener {
                // Ouvrir l'activité d'enregistrement
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }*/
            // Récupérer la référence du bouton
            //val btnConnexion = findViewById<Button>(R.id.btnConnexion)

            // Ajouter un écouteur de clic au bouton
            //btnConnexion.setOnClickListener {
                // Démarrer l'activité de connexion
               // val intent = Intent(this, LoginEmail::class.java)
              //  startActivity(intent)
          //  }



        }

        private fun setupNavigation() {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
            navController = navHostFragment.navController
            sharedViewModel.navController.value = navController

            val appBarConfiguration = AppBarConfiguration(navController.graph)
            val toolbar = findViewById<Toolbar>(R.id.toolbar)

            setSupportActionBar(toolbar)
            actionBar?.apply {
                // Display the back (up) button
                setDisplayHomeAsUpEnabled(true)
                //setHomeAsUpIndicator(R.drawable.ic_back_arrow)

                // Hide the application icon and title
                setDisplayShowHomeEnabled(false)
                setDisplayShowTitleEnabled(false)

            }
            setupActionBarWithNavController(navController, appBarConfiguration)


            /*navController.addOnDestinationChangedListener { _, destination, _ ->
                // Handle destination changes if needed
            }*/
        }

        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }



}
