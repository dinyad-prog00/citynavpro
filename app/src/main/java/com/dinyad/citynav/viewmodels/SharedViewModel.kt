package com.dinyad.citynav.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.dinyad.citynav.MainActivity

class SharedViewModel : ViewModel() {
    val mainActivity = MutableLiveData<MainActivity>()
    val navController = MutableLiveData<NavController>();
}
