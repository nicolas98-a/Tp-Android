package com.unaj.loginsqlite.model

import java.io.Serializable

data class Reservation(
    val id : Int = -1,
    val userEmail : String,
    val complexName : String,
    val date : String,
    val time : String
) :Serializable {
}