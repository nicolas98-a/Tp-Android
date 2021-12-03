package com.unaj.loginsqlite.model

data class Reservation(
    val id : Int = -1,
    val userEmail : String,
    val complexName : String,
    val date : String,
    val time : String
) {
}