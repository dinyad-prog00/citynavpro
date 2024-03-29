package com.dinyad.citynav.adapters

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.viewmodels.SharedViewModel

class PlaceAdapter(
    private  val context: MainActivity,
    private val placeList: ArrayList<PlaceModel>,
    private val id : Int,
    private val navController: NavController,
    private  val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    class  ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val placeImage = view.findViewById<ImageView>(R.id.place_item_img)
        val placeName = view.findViewById<TextView>(R.id.place_name)
        val placeType : TextView? = view.findViewById(R.id.place_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(id,parent,false)
        return  ViewHolder( view)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPlace = placeList[position]
        Glide.with(context).load(Uri.parse(currentPlace.photos[0])).into(holder.placeImage)
        holder.placeName.text = currentPlace.name
        holder.placeType?.text=currentPlace.type
        holder.itemView.setOnClickListener {
            sharedViewModel.currentPlace.value = currentPlace
            navController.navigate(R.id.details_page)

        }

        val paint = holder.placeType?.paint
        val width = paint?.measureText(holder.placeType?.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width!!, holder.placeType?.textSize!!, intArrayOf(
            Color.parseColor("#8BD8F9"),
            Color.parseColor("#5495FF"),

        ), null, Shader.TileMode.REPEAT)

        holder.placeType?.paint?.setShader(textShader)

    }
}