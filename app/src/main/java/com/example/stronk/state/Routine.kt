package com.example.stronk.state

data class User(val id: Int, val username: String, val gender: String, val avatarUrl: String, val date: Long)

data class Category(val id: Int, val name: String, val detail: String)

data class Routine(val id: Int, val name: String, val description: String, val creationDate: Long, val rating: Int, val difficulty: String, val user: User, val category: String)

data class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?,val description:String)

data class CycleInfo(val name: String, val exList: List<ExInfo>, val cycleReps: Int)