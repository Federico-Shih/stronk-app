package com.example.stronk.state

data class User(val id: Int, val username: String, val gender: String, val avatarUrl: String, val date: Int)

data class Category(val id: Int, val name: String, val detail: String)

data class Routine(val id: Int, val name: String, val detail: String, val date: Int, val score: Int, val difficulty: String, val user: User, val category: User)

data class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?)

data class CycleInfo(val name: String, val exList: List<ExInfo>, val cycleReps: Int)