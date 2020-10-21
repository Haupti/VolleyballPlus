package com.example.vbplusapp.game

class DatabaseResponse {
    lateinit var responseText: String
    lateinit var errorMessage: String
    var responseState: Int = NOT_SET
    var contentType: String = "NOT_SET"

    constructor(responseText: String, responseState: Int, errorMessage: String = ""){
        this.responseState = responseState
        this.responseText  = responseText
    }

    constructor(){
        this.responseText = ""
        this.errorMessage = ""
    }

}