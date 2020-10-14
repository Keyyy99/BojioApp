package com.example.bojioapp.model

class ChatMessage(val id:String,val fromId:String, val toId:String, val timestamp: Long, val text: String){
    constructor():this("","","",-1,"")
}