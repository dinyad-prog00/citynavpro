package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.PlaceAdapter

class HomeFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.home_fragment,container,false)
        val wellknown_places= view?.findViewById<RecyclerView>(R.id.wellknown_places)
        wellknown_places?.adapter = PlaceAdapter()
        return view
    }
}