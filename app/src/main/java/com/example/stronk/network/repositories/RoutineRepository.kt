package com.example.stronk.network.repositories

import com.example.stronk.network.datasources.RoutineDataSource
import com.example.stronk.network.dtos.*
import com.example.stronk.state.Category
import com.example.stronk.state.CycleInfo
import com.example.stronk.state.ExInfo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RoutineRepository(private val remoteDataSource: RoutineDataSource) {
    private val mutex = Mutex()

    private var userFavourites: List<RoutineData> = mutableListOf()
    private var refresh = false

    private val pageSize = 50
    private val favouritesSize = 200

    suspend fun getRoutines(
        page: Int? = null,
        size: Int? = null,
        category: Int? = null,
        userId: Int? = null,
        difficulty: String? = null,
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
        direction
    )

    suspend fun getRoutine(id: Int) = remoteDataSource.getRoutine(id)

    suspend fun getRoutineCycles(
        id: Int,
    ): List<CycleInfo> {
        val cycleList = mutableListOf<CycleInfo>()
        val res = remoteDataSource.getCycles(id, page = 0, size = pageSize, null, null)
        val cycleData = res.content.sortedBy { cycle -> cycle.order }

        for (cycle in cycleData) {
            val exerciseInfo: MutableList<ExInfo> = mutableListOf()
            val exercises =
                remoteDataSource
                    .getExercisesFromCycle(
                        cycle.id, page = 0, pageSize, null, null
                    )
            val content = exercises.content.sortedBy { exercise -> exercise.order }
            for (exData in content) {
                val images = getExerciseImages(exData.exercise.id)
                var imageUrl: String? = null
                if (images.content.isNotEmpty()) {
                    imageUrl = images.content[0].url
                }
                exerciseInfo.add(
                    ExInfo(
                        name = exData.exercise.name,
                        reps = if(exData.repetitions == 999) null else exData.repetitions ,
                        duration = if(exData.duration == 999) null else exData.duration,
                        imageUrl = imageUrl,
                        description = exData.exercise.detail
                    )
                )
            }
            cycleList.add(
                CycleInfo(
                    name = cycle.name,
                    exList = exerciseInfo,
                    cycleReps = cycle.repetitions
                )
            )
        }
        return cycleList
    }

    private suspend fun getExerciseImages(
        exerciseId: Int,
    ) = remoteDataSource.getExercisesImages(exerciseId, 0, 1)

    suspend fun getMyRoutines(
        difficulty: String? = null,
        search: String? = null,
        page: Int? = null,
        size: Int? = null,
        orderBy: String? = null,
        direction: String? = null
    ) = remoteDataSource.getMyRoutines(difficulty, search, page, size, orderBy, direction)

    suspend fun getFavouriteRoutines(
        page: Int? = null, size: Int? = null
    ) = remoteDataSource.getFavourites(page, size)

    suspend fun rateRoutine(routineId: Int, rating: RatingDTO) = remoteDataSource.rateRoutine(routineId, rating)

    suspend fun getAllFavouriteRoutines(): List<RoutineData> {
        if (userFavourites.isEmpty() || refresh) {
            val favourites = remoteDataSource.getFavourites(0, favouritesSize).content
            mutex.withLock {
                userFavourites = favourites
                refresh = false
            }
        }
        return mutex.withLock {
            userFavourites
        }
    }

    suspend fun favouriteRoutine(routineId: Int) {
        remoteDataSource.postFavouriteRoutine(routineId)
        mutex.withLock {
            refresh = true
        }
    }

    suspend fun unfavouriteRoutine(routineId: Int) {
        remoteDataSource.removeFavouriteRoutine(routineId)

        mutex.withLock {
            refresh = true
        }
    }

    suspend fun getCategories(): List<Category> {
        val out = mutableListOf<Category>()
        var aux: Paginated<CategoryData>
        var lastPage: Boolean
        do {
            aux = remoteDataSource.getCategories(page = out.size / 10, size = 10)
            lastPage = aux.isLastPage
            out.addAll(aux.content.map { Category(it.id, it.name, it.detail) })
        } while (!lastPage)

        return out
    }
}