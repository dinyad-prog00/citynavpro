package com.dinyad.citynav.models

class UserModel (
    val id : String = "user1",
    val name: String = "Me",
    val email: String = "",
    val favoritePlace : MutableList<String> = mutableListOf()
)