package com.dinyad.citynav.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dinyad.citynav.R
import com.dinyad.citynav.repositories.UserRepository
import com.dinyad.citynav.repositories.UserRepository.Singleton.user
import com.google.firebase.auth.FirebaseAuth

class ProfilePageFragment(): Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View? {

        val view = inflater?.inflate(R.layout.profile_page, container, false)
        val uname = view?.findViewById<TextView>(R.id.user_name)
        val uemail = view?.findViewById<TextView>(R.id.user_email)

        val btnLogout = view?.findViewById<Button>(R.id.btnLogout)
        val  userRepository = UserRepository()
        if(user!=null){
            uname?.text= user?.name
            uemail?.text= user?.email
        }else{
            userRepository.getUser(FirebaseAuth.getInstance().currentUser?.uid!!){
                uname?.text= user?.name
                uemail?.text= user?.email
            }
        }
        btnLogout?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
        }
        return  view

    }
}