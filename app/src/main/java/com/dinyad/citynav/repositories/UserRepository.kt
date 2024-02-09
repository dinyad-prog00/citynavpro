package com.dinyad.citynav.repositories

import com.dinyad.citynav.models.PlaceModel
import com.dinyad.citynav.models.UserModel
import com.dinyad.citynav.repositories.UserRepository.Singleton.favoritePlacesIds
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository {

    object Singleton {
        var user : UserModel? =  null
        val favoritePlacesIds = arrayListOf<String>()
    }


    private val usersRef = Firebase.database.getReference("users")

    fun onUserChange( id : String ,callback : ()-> Unit){
        usersRef.child(id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val u = snapshot.getValue(UserModel::class.java)
                println(u)
                if(u!=null){
                    favoritePlacesIds.clear()
                    favoritePlacesIds.addAll(u.favoritePlace)
                    user= u
                }

                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun likeOrUnlikePlace(placeId:String){
        println(user?.favoritePlace)
         user?.let {
             if(it.favoritePlace.contains(placeId)) {
                 it.favoritePlace.remove(placeId)
             }else{
                 it.favoritePlace.add(placeId)
             }

             usersRef.child(it.id).setValue(it)
         }

        println(user?.favoritePlace)
    }

    fun insertUser(user:UserModel){
        usersRef.child(user.id).setValue(user)
    }
    fun getUser(id:String,callback: () -> Unit){
        usersRef.child(id).get().addOnCompleteListener {
            if(it.isSuccessful){
                user = it.result.getValue(UserModel::class.java)
                callback()
            }
        }
    }






}