package com.dinyad.citynav.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.viewmodels.SharedViewModel

class PlaceDetailsPageFragment(
) : Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var context: MainActivity;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.place_deatails_page, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val place = sharedViewModel.currentPlace.value;
        context = sharedViewModel.mainActivity.value!!;

        println(place)

        if (place != null) {
            val mainImg = view?.findViewById<ImageView>(R.id.details_img)
            val img1 = view?.findViewById<ImageView>(R.id.image1)
            val img2 = view?.findViewById<ImageView>(R.id.image2)
            val img3 = view?.findViewById<ImageView>(R.id.image3)
            val img4 = view?.findViewById<ImageView>(R.id.image4)
            val imgPlus = view?.findViewById<TextView>(R.id.img_plus)

            val back = view?.findViewById<ImageView>(R.id.back_action)

            val placeName = view?.findViewById<TextView>(R.id.place_name)
            val placeDesc = view?.findViewById<TextView>(R.id.description)
            val placeAddr = view?.findViewById<TextView>(R.id.address)

            loadInto(mainImg!!,place.photos,0)
            loadInto(img1!!,place.photos,1)
            loadInto(img2!!,place.photos,2)
            loadInto(img3!!,place.photos,3)
            loadInto(img4!!,place.photos,4)

            placeName?.text = place.name
            placeDesc?.text = place.summary?.overview
            placeAddr?.text = place.address
            imgPlus?.text= if (place.photos.size > 5 ) "+${place.photos.size - 4}" else ""

            back?.setOnClickListener{
            context.onBackPressedDispatcher.onBackPressed();
            }


        }

        // Show the action bar
        //(activity as AppCompatActivity?)?.supportActionBar?.show()


        return view


    }

    fun loadInto(imgView:ImageView,imgs: List<String>,index:Int){
        if(imgs.size>index){
            Glide.with(sharedViewModel.mainActivity.value!!).load(Uri.parse(imgs[index]))
                .into(imgView)
        }
    }
}