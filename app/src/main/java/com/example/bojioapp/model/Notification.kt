package com.example.bojioapp.model

class Notification(val uid:String, val timestamp: Long, val text:String){
    constructor():this("",-1,"")
}