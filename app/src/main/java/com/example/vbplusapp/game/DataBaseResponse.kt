package com.example.vbplusapp.game

import java.util.logging.ErrorManager

class DataBaseResponse {
    lateinit var responseText: String
    lateinit var errorMessage: String
    var responseState: Int = NOT_SET

    constructor(responseText: String, responseState: Int, errorMessage: String = ""){
        this.responseState = responseState
        this.responseText  = responseText
    }

    constructor(){
        this.responseText = ""
        this.errorMessage = ""
    }
}