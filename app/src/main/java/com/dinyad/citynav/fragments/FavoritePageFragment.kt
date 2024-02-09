package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.PlaceAdapter
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.dinyad.citynav.services.api.YelpPlacesApiService
import com.dinyad.citynav.viewmodels.SharedViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoritePageFragment() :Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View? {

        val view = inflater?.inflate(R.layout.favorite_page, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val context: MainActivity = sharedViewModel.mainActivity.value!!
        val navController = sharedViewModel.navController.value;

        val fovorite_places= view?.findViewById<RecyclerView>(R.id.favorite_list)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofitYelp = Retrofit.Builder()
            .baseUrl("https://api.yelp.com/v3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val yelpService = retrofitYelp.create(YelpPlacesApiService::class.java)

        lifecycleScope.launch(Dispatchers.Default) {
            val places = GooglePlacesService.getYelpPlacesByIds(yelpService, if(user!=null) user!!.favoritePlace else listOf() )
            withContext(Dispatchers.Main){
                fovorite_places?.adapter = PlaceAdapter(context,
                    places as ArrayList<PlaceModel> ,R.layout.place_tab_card,navController!!,sharedViewModel)

            }
           }

         return view

    }
}