package com.dinyad.citynav.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dinyad.citynav.R
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.viewmodels.SharedViewModel

class ReviewPageFragment() : Fragment() {
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val navController = sharedViewModel.navController.value;
        val context = sharedViewModel.mainActivity.value
        val view = inflater?.inflate(R.layout.review_page, container, false)
        val place = sharedViewModel.currentPlace.value;
        val placeRepository = PlaceRepository(null,null)

        val ratingBar = view?.findViewById<RatingBar>(R.id.ratingBar)
        val editTextReview= view?.findViewById<EditText>(R.id.editTextReview)
        val btnSubmit  = view?.findViewById<Button>(R.id.btnSubmit)

        btnSubmit?.setOnClickListener {
            val rating = ratingBar?.rating
            val reviewText = editTextReview?.text.toString()

            placeRepository.addReview(place?.placeId!!,rating,reviewText)


            Toast.makeText(context, "Avis bien ajouté :)", Toast.LENGTH_SHORT).show()

            context?.onBackPressedDispatcher?.onBackPressed();
        }

        val back = view?.findViewById<ImageView>(R.id.back_action)

        back?.setOnClickListener{
            context?.onBackPressedDispatcher?.onBackPressed();
        }

        return view


    }
}