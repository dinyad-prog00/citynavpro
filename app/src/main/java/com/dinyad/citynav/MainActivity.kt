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
import com.dinyad.citynav.Activities.LoginEmailActivity
import com.dinyad.citynav.repositories.UserRepository
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    lateinit var sharedViewModel: SharedViewModel
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            auth = FirebaseAuth.getInstance()
            val  userRepository = UserRepository()
            // Initialize the AuthStateListener
            authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val usr = firebaseAuth.currentUser
                if (usr == null) {
                    // User is signed out, navigate to your login or home page
                    user=null
                    val intent = Intent(this, LoginEmailActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid!!){

                    }
                }
            }
            println("main activty ${FirebaseAuth.getInstance().currentUser}")

            if (auth.currentUser != null) {

                println("in main activty")

            setContentView(R.layout.activity_main)
                sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
                sharedViewModel.mainActivity.value = this
                if (savedInstanceState == null) {
                    setupNavigation()
                }

                val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
                bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.item_home -> {
                            // Navigate to HomeFragment
                            navController.navigate(R.id.home_page)
                            true
                        }

                        R.id.item_favorite -> {
                            navController.navigate(R.id.favorite_page)
                            true
                        }

                        R.id.item_compte -> {
                            navController.navigate(R.id.profile_page)
                            true
                        }
                        // Add cases for other BottomNavigationView items as needed
                        else -> false
                    }
                }

            // Initialize SharedViewModel


            GooglePlacesService.checkPermissionThenRequestIfNot(this,this)




            } else {
                // User is not logged in, redirect to login
                startActivity(Intent(this, LoginEmailActivity::class.java))
                finish()
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
            actionBar?.apply {
                // Display the back (up) button
                setDisplayHomeAsUpEnabled(true)
                //setHomeAsUpIndicator(R.drawable.ic_back_arrow)

                // Hide the application icon and title
                setDisplayShowHomeEnabled(false)
                setDisplayShowTitleEnabled(false)

            }
            setupActionBarWithNavController(navController, appBarConfiguration)

        }

        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }

    override fun onStart() {
        super.onStart()
        // Register the AuthStateListener when the activity starts
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        // Unregister the AuthStateListener when the activity stops
        auth.removeAuthStateListener(authStateListener)
    }



}
