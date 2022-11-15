package com.example.stronk.state

import androidx.compose.ui.res.stringResource
import com.example.stronk.R
import java.util.*

data class User(
    val id: Int,
    val username: String,
    val gender: String?,
    val avatarUrl: String?,
    val email: String,
    val birthdate: Date? = null,
)

data class Category(val id: Int, val name: String, val detail: String?)

val Category.nameStringResourceId : Int get() = when (name) {
    "Abdominales" -> R.string.abs
    "Brazos" -> R.string.arms
    "Espalda" -> R.string.back
    "Piernas" -> R.string.legs
    "Pecho" -> R.string.chest
    else -> R.string.full_body
}

data class UserRoutine(
    val id: Int,
    val username: String,
    val avatarUrl: String?,
    val date: Date
)

data class Routine(
    val id: Int,
    val name: String,
    val description: String?,
    val creationDate: Date,
    val rating: Int,
    val difficulty: String,
    val user: UserRoutine?,
    val category: Category
)

val Routine.difficultyStringResourceId : Int get() = when (difficulty) {
    "beginner" -> R.string.beginner
    "intermediate" -> R.string.intermediate
    else -> R.string.advanced
}

data class ExInfo(
    val name: String,
    val reps: Int?,
    val duration: Int?,
    val imageUrl: String?,
    val description: String?
)

data class CycleInfo(val name: String, val exList: List<ExInfo>, val cycleReps: Int)