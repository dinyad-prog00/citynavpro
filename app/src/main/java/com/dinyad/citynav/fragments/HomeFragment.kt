package com.dinyad.citynav.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.PlaceAdapter
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.popularPlaces
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.ktx.api.net.awaitFetchPlace
import com.google.android.libraries.places.ktx.api.net.awaitFindAutocompletePredictions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment(
    private val context:MainActivity
) : Fragment(

){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.home_fragment,container,false)

        val wellknown_places= view?.findViewById<RecyclerView>(R.id.wellknown_places)
        wellknown_places?.adapter = PlaceAdapter(context,popularPlaces,R.layout.place_card)
        return view


    }
}