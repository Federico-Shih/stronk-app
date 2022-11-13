package com.example.stronk.state.models

// Modelo interno
data class User(
    val id: Int,
    val username: String,
//    val firstName: String,
//    val lastName: String,
//    val gender: String,
    val birthdate: Long,
    val email: String,
//    val phone: String,
    val avatarUrl: String
)
