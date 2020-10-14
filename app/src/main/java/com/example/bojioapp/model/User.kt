package com.example.bojioapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.firebase.database.IgnoreExtraProperties


@Parcelize
class  getUser( val age: String,val email: String, val gender: String, val intro: String, val name: String, val phone: String,val photo: String,val uid: String): Parcelable{
    constructor():this("","","","","","","","")
}

@IgnoreExtraProperties
data class User (val name:String = "-",
                 val age:String = "-",
                 val gender:String = "-",
                 val phone:String = "-",
                 val email: String = "-",
                 val intro: String = "-",
                 val photo:String = "-",
                 val uid: String = "-")


