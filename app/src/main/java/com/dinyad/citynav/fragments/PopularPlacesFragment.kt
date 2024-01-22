package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.PlaceAdapter
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.popularPlaces
import com.dinyad.citynav.viewmodels.SharedViewModel

class PopularPlacesFragment(
    private val context:MainActivity
) : Fragment(

){
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.popular_places,container,false)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val navController = sharedViewModel.navController.value;
        val wellknown_places= view?.findViewById<RecyclerView>(R.id.wellknown_places)
        wellknown_places?.adapter = PlaceAdapter(context,popularPlaces,R.layout.place_card,navController!!)
        return view


    }
}