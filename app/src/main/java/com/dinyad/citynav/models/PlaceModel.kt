package com.dinyad.citynav.models

import com.google.gson.annotations.SerializedName

class PlaceModel(
    @SerializedName("place_id") val placeId:String,
    @SerializedName("name") val name:String = "Dinyad City",
    @SerializedName("type") var type: String = "Restaurant",
    @SerializedName("editorial_summary") val summary:PlaceEditorialSummary?,
    @SerializedName("formatted_address") var address: String = "",
    //@SerializedName("imgUrl") val  imgUrl,
    @SerializedName("photos") val photo_refs: List<PhotoRef>,
    @Transient var  photos: List<String> = mutableListOf("https://images.pexels.com/photos/2087323/pexels-photo-2087323.jpeg?auto=compress&cs=tinysrgb&w=1200")

) {
    override fun toString(): String {
        return "PlaceModel(placeId='$placeId', addresse='$address', description='${summary?.overview}', name='$name', type='$type', photo_refs=$photo_refs, photos=$photos)"
    }
}

data  class  PhotoRef (
    @SerializedName("photo_reference") val ref : String
)

data class PlaceEditorialSummary (
    @SerializedName("overview") val overview: String = "",
)




