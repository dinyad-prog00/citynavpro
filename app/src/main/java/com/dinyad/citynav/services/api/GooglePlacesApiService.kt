package com.dinyad.citynav.services.api

import com.dinyad.citynav.models.PlaceModel
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String = com.dinyad.citynav.BuildConfig.GOOGLE_MAPS_API_KEY
    ): Response<PlacesApiResponse>

    @GET("place/nearbysearch/json")
    suspend fun getPolularPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = PlaceTypes.POINT_OF_INTEREST,
        @Query("key") apiKey: String = com.dinyad.citynav.BuildConfig.GOOGLE_MAPS_API_KEY,
        @Query("rankBy") rankBy: String = "prominence"
    ): Response<PlacesApiResponse>

    @GET("place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "place_id,name,formatted_address,editorial_summary,photo",
        @Query("language") language: String = "fr",
        @Query("key") apiKey: String = com.dinyad.citynav.BuildConfig.GOOGLE_MAPS_API_KEY
    ): Response<PlacesApiResponseSingle>


}

data class PlacesApiResponse(
    @SerializedName("results") val results: List<PlaceModel>,
    // Ajoutez d'autres champs selon la structure de la réponse
)

data class PlacesApiResponseSingle(
    @SerializedName("result") val result: PlaceModel,
    // Ajoutez d'autres champs selon la structure de la réponse
)

