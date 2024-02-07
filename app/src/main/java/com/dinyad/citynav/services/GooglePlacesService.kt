package com.dinyad.citynav.services

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.FocusRelativeDirection
import com.dinyad.citynav.models.PhotoRef
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.services.api.GooglePlacesApiService
import com.dinyad.citynav.services.api.PlacesApiResponse
import com.dinyad.citynav.services.api.YelpBusiness
import com.dinyad.citynav.services.api.YelpBusinessDetails
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.ktx.api.net.awaitFindAutocompletePredictions
import com.google.android.libraries.places.ktx.api.net.awaitFindCurrentPlace
import com.google.maps.android.BuildConfig
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun Response<PlacesApiResponse>.bodyList(): List<PlaceModel> {
    return body()?.let { it.results }.also { println("Places lis size ${it?.size}}") }
        ?: emptyList()

}

class GooglePlacesService {
    companion object {
        private fun buildRectangleBounds(from: LatLng, distance: Double): RectangularBounds {
            val southWest = SphericalUtil.computeOffset(from, distance, 225.0)
            val northEast = SphericalUtil.computeOffset(from, distance, 45.0)
            return RectangularBounds.newInstance(southWest, northEast)
        }


        /**
         * Checks that the user has granted permission for fine or coarse location.
         * If granted, finds current Place.
         * If not yet granted, launches the permission request.
         * See https://developer.android.com/training/permissions/requesting
         */
         fun checkPermissionThenRequestIfNot(
            context: Context,
            activity: Activity,

        ): Place? {
            when {
                (ContextCompat.checkSelfPermission(
                    context,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    // You can use the API that requires the permission.
                   // return getCurrentPlace(context, placesClient, activity)
                    Log.i(TAG,"Permissions already granted")

                }

                else -> {
                    // Ask for both the ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions.
                    Log.i(TAG,"Permissions  requested")
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }

            }

            return null
        }


        private val TAG = "CurrentPlaceActivity"
        private const val PERMISSION_REQUEST_CODE = 10

        /* public suspend fun getCurrentPlace(
            context: Context,
            placesClient: PlacesClient,
            activity: Activity
        ): Place? {


// Use fields to define the data types to return.
            val placeFields: List<Place.Field> = listOf(Place.Field.NAME, Place.Field.LAT_LNG)

// Use the builder to create a FindCurrentPlaceRequest.
            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val response = placesClient.awaitFindCurrentPlace(placeFields)


                println("Current place : ${response?.placeLikelihoods?.size}")


                for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
                    ?: emptyList()) {
                    Log.i(
                        TAG,
                        "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                    )

                }
                if (!response?.placeLikelihoods.isNullOrEmpty()) {
                    return response.placeLikelihoods[0].place
                    //fetchPlacesPredictions(placesClient,response.placeLikelihoods[0].place.latLng,Place.Type.RESTAURANT,response.placeLikelihoods[0].place.name)
                }
                /*} else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }*/
            } else {
                // A local method to request required permissions;
                // See https://developer.android.com/training/permissions/requesting
                return checkPermissionThenFindCurrentPlace(context, activity, placesClient)
            }

            return null


        } */

        @SuppressLint("MissingPermission")
        fun getLastLocation(
            fusedLocationClient: FusedLocationProviderClient,
            callback: (location: Location) -> Unit
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        callback(location)
                    } ?: run {
                        val loc = Location("test")
                        loc.longitude =-122.0849 // 1.8805170
                        loc.latitude =37.4226 //50.9532068
                        callback(loc)
                        Log.e(TAG, "=============Dernière localisation non disponible.")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Erreur lors de la récupération de la localisation: $e")
                }
        }

        suspend fun getPlacesByType2(
            placesApiService: GooglePlacesApiService,
            location: String,
            type: String
        ): List<PlaceModel> = placesApiService.getNearbyPlaces(location, 1000, type).bodyList()

        suspend fun getPlacesByType(
            placesApiService: GooglePlacesApiService,
            location: String,
            type: String
        ): List<PlaceModel> = coroutineScope {
            val places: List<PlaceModel> =
                placesApiService.getNearbyPlaces(location, 1000, type).bodyList()

            val deferreds: List<Deferred<PlaceModel?>> = places.map { p ->
                async(Dispatchers.Default) {

                    placesApiService.getPlaceDetails(p.placeId).body()?.result
                }
            }

            val placesDt: List<PlaceModel> =
                deferreds.awaitAll().filter { it != null } as List<PlaceModel>

            placesDt.map { setPhotos(it) }

        }

        suspend fun getPopularPlacesByType(
            placesApiService: GooglePlacesApiService,
            location: String
        ): List<PlaceModel> = placesApiService.getPolularPlaces(location, 1000).bodyList()


        suspend fun getPopularPlaces(
            placesApiService: GooglePlacesApiService,
            location: String,
            types: List<String>,
            limit: Int = 20
        ): List<PlaceModel> = coroutineScope {
            val deferreds: List<Deferred<List<PlaceModel>>> = types.map { type ->
                async(Dispatchers.Default) {
                    placesApiService.getPolularPlaces(location, 1000, type).bodyList()
                }
            }
            val places: List<PlaceModel> = deferreds.awaitAll().flatten().shuffled().take(limit)

            val deferreds2: List<Deferred<PlaceModel?>> = places.map { p ->
                async(Dispatchers.Default) {
                    placesApiService.getPlaceDetails(p.placeId).body()?.result
                }
            }

            val placesDt: List<PlaceModel> =
                deferreds2.awaitAll().filter { it != null } as List<PlaceModel>

            placesDt.map { setPhotos(it) }
        }

        suspend fun getPlacesForManyTypes(
            placesApiService: GooglePlacesApiService,
            location: String, types: List<String>
        ): MutableMap<String, ArrayList<PlaceModel>> = coroutineScope {

            val deferreds: List<Deferred<Pair<String, List<PlaceModel>>>> = types.map { type ->
                async(Dispatchers.Default) {
                    type to getPlacesByType(placesApiService, location, type)
                }
            }
            deferreds.awaitAll()
                .associate { it.first to it.second } as MutableMap<String, ArrayList<PlaceModel>>

        }

        fun setPhotos(place: PlaceModel): PlaceModel {
            if (place.photo_refs != null) {
                //val rr= getPhotoes(placesApiService, p.photo_refs);
                place.photos =
                    place.photo_refs.map { "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${it.ref}&key=${com.dinyad.citynav.BuildConfig.GOOGLE_MAPS_API_KEY}" }
            } else {
                place.photos =
                    mutableListOf("https://images.pexels.com/photos/2087323/pexels-photo-2087323.jpeg?auto=compress&cs=tinysrgb&w=1200")
            }
            place.type = "PlaceType"
            return place
        }


        fun businessToPlace(business:List<YelpBusinessDetails>) : List<PlaceModel>{

            return  business.map { PlaceModel(it.placeId, listOf()) }

        }





        val typesNames = mapOf<String, String>(
            PlaceTypes.RESTAURANT to "Restaurant",
            PlaceTypes.POINT_OF_INTEREST to "Point d'Itr",
            PlaceTypes.LODGING to "Hébergement"
        )


    }
}


