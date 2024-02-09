package com.dinyad.citynav.adapters

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dinyad.citynav.MainActivity
import com.dinyad.citynav.R
import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.models.ReviewModel
import com.dinyad.citynav.viewmodels.SharedViewModel

class ReviewAdapter(
    private val reviewList: ArrayList<ReviewModel>,
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){

    class  ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.username)
        val rating = view.findViewById<RatingBar>(R.id.user_rating)
        val text : TextView? = view.findViewById(R.id.user_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_card,parent,false)
        return  ViewHolder( view)
    }

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewList[position]

        holder.username.text = review.username
        holder.rating ?.rating= review.rating!!
        holder.text?.text = review.text
    }
}