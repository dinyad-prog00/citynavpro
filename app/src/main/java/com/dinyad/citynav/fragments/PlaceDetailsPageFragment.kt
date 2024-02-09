package com.dinyad.citynav.fragments

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.adapters.ReviewAdapter
import com.dinyad.citynav.repositories.PlaceRepository
import com.dinyad.citynav.repositories.UserRepository
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.dinyad.citynav.viewmodels.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class PlaceDetailsPageFragment(
) : Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var context: MainActivity;
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.place_deatails_page, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val place = sharedViewModel.currentPlace.value;
        context = sharedViewModel.mainActivity.value!!;
        navController =  sharedViewModel.navController.value!!

        val userRepository = UserRepository()

        println(place)

        if (place != null) {
            val mainImg = view?.findViewById<ImageView>(R.id.details_img)
            val img1 = view?.findViewById<ImageView>(R.id.image1)
            val img2 = view?.findViewById<ImageView>(R.id.image2)
            val img3 = view?.findViewById<ImageView>(R.id.image3)
            val img4 = view?.findViewById<ImageView>(R.id.image4)
            val imgPlus = view?.findViewById<TextView>(R.id.img_plus)
            val likeBtn = view?.findViewById<ImageView>(R.id.like_btn)

            val back = view?.findViewById<ImageView>(R.id.back_action)

            val addReview = view?.findViewById<Button>(R.id.btnAddReview)

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


            val paint = placeName?.paint
            val width = paint?.measureText(placeName?.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width!!, placeName?.textSize!!, intArrayOf(
                Color.parseColor("#8BD8F9"),
                Color.parseColor("#5495FF"),

                ), null, Shader.TileMode.REPEAT)

            placeName?.paint?.setShader(textShader)

            if(user !=null && user!!.favoritePlace.contains(place.placeId)){
                likeBtn?.setImageResource(R.drawable.ic_liked)
            }else{
                likeBtn?.setImageResource(R.drawable.ic_unliked)
            }

            userRepository.onUserChange( FirebaseAuth.getInstance().currentUser!!.uid){
                println("userRepository.onUserChange")
                if(user !=null && user!!.favoritePlace.contains(place.placeId)){
                    likeBtn?.setImageResource(R.drawable.ic_liked)
                }else{
                    likeBtn?.setImageResource(R.drawable.ic_unliked)
                }
            }


            likeBtn?.setOnClickListener{
               userRepository.likeOrUnlikePlace(place.placeId)
            }

            back?.setOnClickListener{
            context.onBackPressedDispatcher.onBackPressed();
            }


            addReview?.setOnClickListener{
                navController.navigate(R.id.review_page)
            }

            val rating = view.findViewById<RatingBar>(R.id.ratingBar)
            val placeRepository = PlaceRepository(null,null)
            placeRepository.getReviews(place.placeId){
                val reviewsList = view?.findViewById<RecyclerView>(R.id.reviews_list)
                reviewsList?.adapter = ReviewAdapter(it)
                var totalRating = 0f
                var n=0
                for (r in it){
                    println(r.rating)
                    if(r.rating!=null){

                        totalRating  += r.rating
                        n++
                    }

                }


                println(if(n==0) 0f else totalRating/n.toFloat())

                rating.rating = if(n==0) 0f else totalRating/n.toFloat()


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




