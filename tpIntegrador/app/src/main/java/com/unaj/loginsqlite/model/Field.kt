package com.unaj.loginsqlite.model

data class Field(
    val id : Int = -1,
    val complexName : String,
    val price : Int,
    val illumination : Int,
    val covered : Int,
    val synthetic : Int
) {
}