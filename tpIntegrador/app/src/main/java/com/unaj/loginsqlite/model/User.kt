package com.unaj.loginsqlite.model

    //Model class
data class User (
    val id: Int = -1,
    val name: String,
    val email: String,
    val password: String,
    val rol: Int
        )