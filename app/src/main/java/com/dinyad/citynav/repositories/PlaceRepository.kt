package com.dinyad.citynav.repositories

import android.content.Context
import android.util.Log
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.models.ReviewModel
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.places
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.popularPlaces
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.dinyad.citynav.services.api.YelpPlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlaceRepository(
    private  val yelpService: YelpPlacesApiService?,
    private val fusedLocationClient: FusedLocationProviderClient?
) {
    object Singleton {
        val popularPlaces = arrayListOf<PlaceModel>()
        val places = mutableMapOf<String, ArrayList<PlaceModel>>()
    }

    val database = Firebase.database
    val placesRef = database.getReference("places")



    fun updateNeabyPlacesList(coroutineScope: CoroutineScope, callback: () -> Unit) {
        GooglePlacesService.getLastLocation(fusedLocationClient!!) {
            Log.i("updateNeabyPlacesList yyy", "${it.latitude},${it.longitude}")
            coroutineScope.launch(Dispatchers.Default) {


                if(popularPlaces.isEmpty()){

                val pPlaces = GooglePlacesService.getYelpPopularPlaces(
                    yelpService!!,
                    50.94811382774165,
                    1.8533044913016194,

                )

                Log.i("updateNeabyPlacesList ZZ", pPlaces?.size.toString())

                if (pPlaces != null) {
                    popularPlaces.clear();
                    popularPlaces.addAll(pPlaces.toList())
                }
                }
                /*
               val pPlaces = GooglePlacesService.getPopularPlaces(
                    placesApiService,
                    "${it.latitude},${it.longitude}",
                    listOf(PlaceTypes.MUSEUM,PlaceTypes.RESTAURANT,PlaceTypes.LODGING),
                    25
                )*/



                              val placesNb = GooglePlacesService.getYelpNearbyPlaces(yelpService!!,
                                   50.94811382774165,
                                   1.8533044913016194,)
                               if(placesNb!=null){
                                   places.clear()
                                   places.putAll(placesNb)

                               }



                               withContext(Dispatchers.Main){

                                   callback()
                               }



/*
                               val restos = GooglePlacesService.getPlacesByType(
                                   placesApiService,
                                   "${it.latitude},${it.longitude}",
                                   PlaceTypes.RESTAURANT
                               )
                               if (restos != null) {
                                   places[PlaceTypes.RESTAURANT] = ArrayList(restos)
                               }

                               val placesTmp = GooglePlacesService.getPlacesForManyTypes(placesApiService,"${it.latitude},${it.longitude}",listOf(PlaceTypes.MUSEUM,PlaceTypes.RESTAURANT,PlaceTypes.LODGING))
                               if (placesTmp != null) {
                                   places.clear();
                                   places.putAll(placesTmp)


                               withContext(Dispatchers.Main){
                                   callback()
                               }

                                */

            }

        }
    }

    fun getUserFavoritePlaces(coroutineScope: CoroutineScope, callback: (fovoritesPlaes:List<PlaceModel>) -> Unit) {
        coroutineScope.launch(Dispatchers.Default) {
            val places = GooglePlacesService.getYelpPlacesByIds(yelpService!!, listOf() )
            callback(places)
        }
    }

    fun updateData(){
        placesRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snap: DataSnapshot) {
                for(ds in snap.children){
                    val place = ds.getValue(PlaceModel::class.java)
                    if(place!=null){

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun insertPlace(place:PlaceModel){
        placesRef.child(place.placeId).setValue(place)
    }
    fun updatePlace(place:PlaceModel){
        placesRef.child(place.placeId).setValue(place)
    }
    fun addReview(placeId: String, rating: Float?, text: String?) {
        val reviewsRef = placesRef.child(placeId).child("reviews")

        // Create a unique key for the review
        val reviewId = reviewsRef.push().key
        // Add the review to the database
        reviewId?.let {
            reviewsRef.child(it).setValue(ReviewModel( rating, text, user?.name))
        }
    }

    fun getReviews(placeId: String,callback: (ArrayList<ReviewModel>) -> Unit){
        val reviewsRef = placesRef.child(placeId).child("reviews").addValueEventListener(object : ValueEventListener{
            val list = arrayListOf<ReviewModel>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for( ds in snapshot.children){
                    var r = ds.getValue(ReviewModel::class.java)
                    if(r!=null){
                        list.add(r)
                    }
                }
                callback(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}