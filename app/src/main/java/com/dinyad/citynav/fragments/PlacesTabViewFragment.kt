package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.PlaceAdapter
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.places
import com.dinyad.citynav.viewmodels.SharedViewModel

class PlacesTabViewFragment(
    private val context: MainActivity,
    private val type: String,
) : Fragment(){
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.places_tab_view,container,false)
        val wellknown_places= view?.findViewById<RecyclerView>(R.id.tab_view)
        // Access SharedViewModel from the activity
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val navController = sharedViewModel.navController.value;

        wellknown_places?.adapter = places[type]?.let {
            println("oui oui oui====== ${it.size}")
            PlaceAdapter(context,
                it,R.layout.place_tab_card,navController!!,sharedViewModel)
        }
        return view
    }
}