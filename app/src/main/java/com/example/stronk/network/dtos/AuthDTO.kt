package com.example.stronk.network.dtos

data class LoginDTO(
    val username: String,
    val password: String,
)

data class RegisterDTO(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String = "other",
    val birthdate: Int = 0,
    val phone: String = "",
)

data class VerifyEmailDTO(
    val email: String,
    val code: String,
)

data class ResendEmailDTO(
    val email: String,
)