package com.example.stronk.network.repositories

import com.example.stronk.network.datasources.RoutineDataSource

class RoutineRepository(private val remoteDataSource: RoutineDataSource) {

    suspend fun getRoutines(
        page: Int? = null,
        size: Int? = null,
        category: Int? = null,
        userId: Int? = null,
        difficulty: Int? = null,
        score: Int? = null,
        search: String? = null,
        orderBy: String? = null,
        direction: String? = null
    ) = remoteDataSource.getRoutines(
        category,
        userId,
        difficulty,
        score,
        search,
        page,
        size,
        orderBy,
        direction)[0]

    suspend fun getRoutine(id: Int) = remoteDataSource.getRoutine(id)

    suspend fun getRoutineCycles(
        id: Int,
        page: Int? = null,
        size: Int? = null,
        orderBy: String? = null,
        direction: String? = null
    ) = remoteDataSource.getCycles(id, page, size, orderBy, direction)

    suspend fun getExerciseImages(
        exerciseId: Int,
        page: Int? = null,
        size: Int? = null,
        orderBy: String? = null,
        direction: String? = null
    ) = remoteDataSource.getExercisesImages(exerciseId, page, size, orderBy, direction)

    suspend fun getMyRoutines(
        difficulty: Int? = null,
        search: String? = null,
        page: Int? = null,
        size: Int? = null,
        orderBy: String? = null,
        direction: String? = null
    ) = remoteDataSource.getMyRoutines(difficulty, search, page, size, orderBy, direction)

    suspend fun getFavouriteRoutines(page: Int? = null, size: Int? = null
    ) = remoteDataSource.getFavourites(page, size)

    suspend fun favouriteRoutine(routineId: Int) = remoteDataSource.postFavouriteRoutine(routineId)

    suspend fun unfavouriteRoutine(routineId: Int) = remoteDataSource.removeFavouriteRoutine(routineId)


}