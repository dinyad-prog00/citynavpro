package com.dinyad.citynav.repositories

import android.content.Context
import android.util.Log
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.places
import com.dinyad.citynav.repositories.PlaceRepository.Singleton.popularPlaces
import com.dinyad.citynav.services.GooglePlacesService
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.PlaceTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlaceRepository(
    private val placesApiService: GooglePlacesApiService,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    object Singleton {
        val popularPlaces = arrayListOf<PlaceModel>()
        val places = mutableMapOf<String, ArrayList<PlaceModel>>()
    }


    fun updateNeabyPlacesList(coroutineScope: CoroutineScope, callback: () -> Unit) {

        GooglePlacesService.getLastLocation(fusedLocationClient) {
            Log.i("updateNeabyPlacesList", "${it.latitude},${it.longitude}")
            coroutineScope.launch(Dispatchers.Default) {
               val pPlaces = GooglePlacesService.getPopularPlaces(
                    placesApiService,
                    "${it.latitude},${it.longitude}",
                    listOf(PlaceTypes.MUSEUM,PlaceTypes.RESTAURANT,PlaceTypes.LODGING),
                    25
                )
                Log.i("updateNeabyPlacesList", pPlaces?.size.toString())
                if (pPlaces != null) {
                    popularPlaces.clear();
                    popularPlaces.addAll(pPlaces.toList())
                }

                /*withContext(Dispatchers.Main){
                    callback()
                }

                val restos = GooglePlacesService.getPlacesByType(
                    placesApiService,
                    "${it.latitude},${it.longitude}",
                    PlaceTypes.RESTAURANT
                )
                if (restos != null) {
                    places[PlaceTypes.RESTAURANT] = ArrayList(restos)
                }
*/
                val placesTmp = GooglePlacesService.getPlacesForManyTypes(placesApiService,"${it.latitude},${it.longitude}",listOf(PlaceTypes.MUSEUM,PlaceTypes.RESTAURANT,PlaceTypes.LODGING))
                if (placesTmp != null) {
                    places.clear();
                    places.putAll(placesTmp)
                }
                withContext(Dispatchers.Main){
                    callback()
                }

            }

        }
    }

}