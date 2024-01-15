package com.dinyad.citynav

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.dinyad.citynav.adapters.CategoryPagerAdapter
import com.dinyad.citynav.fragments.HomeFragment
import com.dinyad.citynav.fragments.PlacesTabView
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!Places.isInitialized()){
            Places.initialize(this, BuildConfig.GOOGLE_MAPS_API_KEY)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val placesApiService = retrofit.create(GooglePlacesApiService::class.java)

        val placeRepository = PlaceRepository(placesApiService,fusedLocationClient)

        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val loadingSpinner: ProgressBar = findViewById(R.id.loadingSpinner)

        tabLayout.visibility = View.GONE


        placeRepository.updateNeabyPlacesList(lifecycleScope) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.home_fragment,HomeFragment(this))
            transaction.addToBackStack(null)
            transaction.commit()

            val fragments = listOf(
                PlacesTabView(this,PlaceTypes.RESTAURANT),
                PlacesTabView(this,PlaceTypes.LODGING),
                PlacesTabView(this,PlaceTypes.MUSEUM)
            )

            val titles = listOf("Restaurants", "Hotêls", "Musées")

            val adapter = CategoryPagerAdapter(supportFragmentManager, fragments, titles)
            viewPager.adapter = adapter

            tabLayout.setupWithViewPager(viewPager)
            loadingSpinner.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE

           /* val transaction2 = supportFragmentManager.beginTransaction()
            transaction2.replace(R.id.places_tap_view,PlacesTabView(this))
            transaction2.addToBackStack(null)
            transaction2.commit()*/
        }



        /* injecter home fragment*/


    }
}