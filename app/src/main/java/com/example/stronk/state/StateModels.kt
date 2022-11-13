package com.example.stronk.state

import java.util.*

data class User(val id: Int, val username: String, val gender: String, val avatarUrl: String,val email: String, val birthdate: Date)

data class Category(val id: Int, val name: String, val detail: String)

data class UserRoutine(
    val id: Int,
    val username: String,
    val gender: String,
    val avatarUrl: String,
    val date: Date
)

data class Routine(val id: Int, val name: String, val description: String, val creationDate: Date, val rating: Int, val difficulty: String, val user: UserRoutine, val category: String)

data class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?,val description:String)

data class CycleInfo(val name: String, val exList: List<ExInfo>, val cycleReps: Int)