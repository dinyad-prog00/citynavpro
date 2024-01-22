package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R

class PlaceDetailsPageFragment (
) : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.place_deatails_page,container,false)

       /* val wellknown_places= view?.findViewById<RecyclerView>(R.id.wellknown_places)
        wellknown_places?.adapter = PlaceAdapter(context,
            PlaceRepository.Singleton.popularPlaces,
            R.layout.place_card)*/

        // Show the action bar
        (activity as AppCompatActivity?)?.supportActionBar?.show()

        return view


    }
}