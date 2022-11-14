package com.example.stronk.network.datasources

import com.example.stronk.network.RemoteDataSource
import com.example.stronk.network.dtos.CycleData
import com.example.stronk.network.dtos.ExerciseImageData
import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import com.example.stronk.network.services.FavouriteApiService
import com.example.stronk.network.services.RoutineApiService
import retrofit2.http.Path
import retrofit2.http.Query

class RoutineDataSource(private val routineApiService: RoutineApiService, private val favouriteApiService: FavouriteApiService) : RemoteDataSource() {
    //Si no se quiere usar el parametro no se usa y se manda null, el parametro es opcional
    suspend fun getRoutines(category: Int?=null,
                            userId: Int?=null,
                            difficulty: Int?=null,
                            score: Int?=null,
                            search: String?=null,
                            page: Int?=null,
                            size: Int?=null,
                            orderBy: String?=null,
                            direction: String?=null
    ): List<Paginated<RoutineData>> {

      return handleApiResponse {
            routineApiService.getRoutines(category, userId, difficulty, score, search, page, size, orderBy, direction)
        }
    }
    suspend fun getMyRoutines(difficulty: Int?=null,
                               search: String?=null,
                               page: Int?=null,
                               size: Int?=null,
                               orderBy: String?=null,
                               direction: String ?=null
    ):Paginated<RoutineData>{
       return handleApiResponse {
            routineApiService.getMyRoutines(difficulty, search, page, size, orderBy, direction)
        }
    }

    suspend fun getCycles(routineId: Int,
                          page: Int?,
                          size: Int?,
                          orderBy: String?,
                          direction: String?,
    ):Paginated<CycleData>{
        return handleApiResponse {
            routineApiService.getCycles(routineId, page, size, orderBy, direction)
        }
    }
    suspend fun getRoutine(id: Int): RoutineData {
        return handleApiResponse {
            routineApiService.getRoutine(id)
        }
    }
    suspend fun getExercisesImages(exerciseId: Int,
                                   page: Int?=null,
                                   size: Int?=null,
                                   orderBy: String?=null,
                                   direction: String?=null,
    ):Paginated<List<ExerciseImageData>>{
        return handleApiResponse {
            routineApiService.getExercisesImages(exerciseId, page, size, orderBy, direction)
        }
    }
    suspend fun getFavourites(page:Int?=null,size:Int?=null): Paginated<RoutineData> {
        return handleApiResponse {
            favouriteApiService.getFavourites(page, size)
        }
    }
    suspend fun postFavouriteRoutine(routineId: Int): Unit {
        return handleApiResponse {
            favouriteApiService.postFavouriteRoutine(routineId)
        }
    }
    suspend fun removeFavouriteRoutine(routineId: Int): Any {
        return handleApiResponse {
            favouriteApiService.removeFavouriteRoutine(routineId)
        }
    }

}