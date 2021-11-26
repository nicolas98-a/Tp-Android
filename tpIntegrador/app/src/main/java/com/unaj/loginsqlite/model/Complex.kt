package com.unaj.loginsqlite.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Complex (
     val id : Int,
     val name : String,
     val location : LatLng,
     val phone : String,
     val parking : Boolean,
     val lockerRoom : Boolean,
     val grill : Boolean
         ) {
}