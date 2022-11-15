package com.example.stronk.network.dtos

import com.example.stronk.state.Routine
import com.example.stronk.state.UserRoutine
import com.google.gson.annotations.SerializedName
import java.util.*

enum class CycleType {
    WARMUP, EXERCISE, COOLDOWN
}

enum class ExerciseType {
    exercise, rest
}

data class RoutineUserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("date") val date: Date
) {
    fun asModel(): UserRoutine {
        return UserRoutine(id, username, avatarUrl, date)
    }
}

data class RoutineData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("detail") val description: String,
    @SerializedName("date") val creationDate: Date,
    @SerializedName("score") val rating: Int,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("user") val user: RoutineUserData?,
    @SerializedName("category") val category: CategoryData
) {
    fun asModel(): Routine {
        return Routine(
            id,
            name,
            description,
            creationDate,
            rating,
            difficulty,
            user?.asModel(),
            category.asModel()
        )
    }
}

data class CycleData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("type") val type: CycleType,
    @SerializedName("order") val order: Int,
    @SerializedName("repetitions") val repetitions: Int
)

//data class ExInfo(val name: String, val reps: Int?, val duration: Int?, val imageUrl: String?,val description:String)

data class ExerciseData(
    @SerializedName("name") val name: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: ExerciseType,
)

data class ExerciseCycleData(
    @SerializedName("exercise") val exercise: ExerciseData,
    @SerializedName("order") val order: Int?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("repetitions") val repetitions: Int?
)

data class ExerciseImageData(
    @SerializedName("id") val id: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("url") val url: String
)

