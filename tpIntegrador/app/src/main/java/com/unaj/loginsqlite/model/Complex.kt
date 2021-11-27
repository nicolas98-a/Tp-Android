package com.unaj.loginsqlite.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Complex (
     val id : Int = -1,
     val name : String,
     val location : String,
     val phone : String,
     val parking : Int,
     val lockerRoom : Int,
     val grill : Int,
     val adminEmail: String
         ) {
}