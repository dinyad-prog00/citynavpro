package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.dinyad.citynav.BuildConfig
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.CategoryPagerAdapter
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.repositories.UserRepository
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.dinyad.citynav.services.api.YelpPlacesApiService
import com.dinyad.citynav.viewmodels.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomePageFragment() :Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View? {

        val view = inflater?.inflate(R.layout.home_page,container,false)

        // Access SharedViewModel from the activity
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Access MainActivity instance
        val context: MainActivity = sharedViewModel.mainActivity.value!!
        if(!Places.isInitialized()){
            Places.initialize(context, BuildConfig.GOOGLE_MAPS_API_KEY)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val retrofitYelp = Retrofit.Builder()
            .baseUrl("https://api.yelp.com/v3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val yelpService = retrofitYelp.create(YelpPlacesApiService::class.java)


        val placeRepository = PlaceRepository(yelpService,fusedLocationClient)

        val viewPager = view?.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = view?.findViewById<TabLayout>(R.id.tabLayout)
        val loadingSpinner = view?.findViewById<ProgressBar>(R.id.loadingSpinner)

        val greeting = view?.findViewById<TextView>(R.id.title)
        val  userRepository = UserRepository()
        if(user!=null){
          greeting?.text="Bonjour ${user?.name} !"
        }else{
            userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid!!){
                greeting?.text="Bonjour ${user?.name} !"
            }
        }

        tabLayout?.visibility = View.GONE


        placeRepository.updateNeabyPlacesList(lifecycleScope) {
            println("PopularPlacesFragment")
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.home_fragment,PopularPlacesFragment(context!!))
            transaction.addToBackStack(null)
            transaction.commit()

            val fragments = listOf(
                PlacesTabViewFragment(context, PlaceTypes.RESTAURANT),
                PlacesTabViewFragment(context, PlaceTypes.LODGING),
                PlacesTabViewFragment(context, PlaceTypes.MUSEUM)
            )

            val titles = listOf("Restaurants", "Hotêls", "Musées")

            val adapter = CategoryPagerAdapter(childFragmentManager, fragments, titles)
            viewPager?.adapter = adapter

            tabLayout?.setupWithViewPager(viewPager)
            loadingSpinner?.visibility = View.GONE
            tabLayout?.visibility = View.VISIBLE


        }

        // Hide the action bar
        (activity as AppCompatActivity?)?.supportActionBar?.hide()

         fun onItemClick(placeModel: PlaceModel) {
            // Handle item click, e.g., navigate to place details page
            // You can use Navigation Component or any other navigation method
            //val action = HomePageFragmentDirections.actionHomeToDetails()
           //navController.navigate(action)
        }

        return  view;

    }





}