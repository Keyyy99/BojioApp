package com.example.bojioapp

class Request {
    var rid:String?=null
    var host:String?=null
    var title:String?=null
    var date:String?=null
    var time:String?=null
    var location:String?=null
    var category:String?=null
    var description:String?=null
    var participantNum:String?=null

    constructor():this("","","","","","","","",""){

    }

    constructor(rid: String?,host: String?, title: String?, date: String?, time: String?, location: String?, category: String?, description: String?, participantNum: String?) {
        this.rid = rid
        this.host= host
        this.title = title
        this.date = date
        this.time = time
        this.location = location
        this.category = category
        this.description = description
        this.participantNum = participantNum
    }

}