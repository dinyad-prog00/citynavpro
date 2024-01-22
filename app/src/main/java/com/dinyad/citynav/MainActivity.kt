package com.dinyad.citynav
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.dinyad.citynav.R
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.viewmodels.SharedViewModel

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


        }

        private fun setupNavigation() {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
            navController = navHostFragment.navController
            sharedViewModel.navController.value = navController

            val appBarConfiguration = AppBarConfiguration(navController.graph)
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            setupActionBarWithNavController(navController, appBarConfiguration)


            /*navController.addOnDestinationChangedListener { _, destination, _ ->
                // Handle destination changes if needed
            }*/
        }

        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }



}
