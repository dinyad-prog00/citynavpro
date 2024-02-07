package com.dinyad.citynav.services.api

import com.dinyad.citynav.models.PlaceModel
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpPlacesApiService {
    @GET("businesses/search")
    suspend fun getNearbyPlaces(
        @Query("term") term: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("radius") radius: Int=4000,
        @Header("Authorization") authorization: String = com.dinyad.citynav.BuildConfig.YELP_API_KEY,
    ): Response<YelpResponse>

    @GET("businesses/search")
    suspend fun getPolularPlaces(
        @Query("term") term: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("radius") radius: Int=4000,
        @Query("attributes") attributes : String= "hot_and_new",
        @Header("Authorization") authorization: String = com.dinyad.citynav.BuildConfig.YELP_API_KEY,
    ): Response<YelpResponse>

    @GET("businesses/{placeId}")
    suspend fun getPlaceDetails(
        @Path("placeId") placeId: String,
        @Header("Authorization") authorization: String = com.dinyad.citynav.BuildConfig.YELP_API_KEY,
    ): Response<YelpBusinessDetails>



}

data class YelpResponse(
    val businesses: List<YelpBusiness>
)

data class YelpBusiness(
    val name: String,
    val rating: Double,
    val location: YelpLocation
)

data class YelpLocation(
    val address1: String
)


data class YelpBusinessDetails(
    @SerializedName("id")  val placeId: String,
    val name: String,
    val photos : List<String>,
    val location: YelpLocation,
    val phone: String,
    val hours: List<YelpHour>  // You can customize this based on the details you need
)

data class YelpHour(
    val hours_type: String,
    val open: List<YelpOpen>
)

data class YelpOpen(
    val is_overnight: Boolean,
    val start: String,
    val end: String,
    val day: Int
)
