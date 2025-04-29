package com.gayatri.e_commerce_application.domain.models

import androidx.compose.runtime.mutableStateMapOf

data class UserData(
    val fastName : String = "",
    val lastName : String = "",
    val email : String = "",
    val password : String = "",
    val phoneNumber : String = "",
    val address : String = "",
    val profileImage : String = "",
) {
    fun toMap(): Map<String, Any> {
        val map = mutableStateMapOf<String, Any>()
        map["fastName"] = fastName
        map["lastName"] = lastName
        map["email"] = email
        map["password"] = password
        map["phoneNumber"] = phoneNumber
        map["address"] = address
        map["profileImage"] = profileImage
        return map
    }
}

data class UserDataParent(
    val nodeId : String = "",
    val userData : UserData = UserData()
)