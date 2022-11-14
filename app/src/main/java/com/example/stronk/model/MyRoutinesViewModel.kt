package com.example.stronk.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stronk.StronkApplication
import com.example.stronk.network.dtos.Paginated
import com.example.stronk.network.dtos.RoutineData
import com.example.stronk.network.repositories.RoutineRepository
import com.example.stronk.state.MyRoutinesState
import com.example.stronk.state.Routine
import com.example.stronk.state.User
import com.example.stronk.state.UserRoutine
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MyRoutinesViewModel(private val routineRepository: RoutineRepository) : ViewModel() {
    var uiState by mutableStateOf(MyRoutinesState())
        private set

    private var fetchJob:Job? = null

    init {
        fetchFirstRoutines()
    }

//    val routinesTest= listOf<Routine>(Routine(id=1,name="Abdos en 15mins",
//        "abods", Date(165432697),4,"Avanzado",
//        UserRoutine(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",Date(165432697)),
//        "Abdominales"),
//        Routine(id=2,name="Abdos en 15mins",
//            "abods",Date(165432697),4,"Avanzado",
//            UserRoutine(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",Date(165432697)),
//            "Abdominales"),
//        Routine(id=3,name="Abdos en 15mins",
//            "abods",Date(165432697),4,"Avanzado",
//            UserRoutine(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",Date(165432697)),
//            "Abdominales"),
//        )

    fun moreFavouriteRoutines(){
        if(uiState.isLastPageFav){
            return
        }
        var routineList = uiState.favouriteRoutines
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            runCatching {
                routineRepository.getFavouriteRoutines(page= if(routineList.size%3==0) uiState.favouriteRoutinesPage+1 else uiState.favouriteRoutinesPage , size=3)
            }.onSuccess { routines ->
                val auxRoutines= ( if(routineList.size%3!=0) routineList.slice(0 until ( routineList.size- routineList.size%3) ) else routineList) + routines.content.map { it.asModel() }
                uiState = uiState.copy(favouriteRoutines = auxRoutines, favouriteRoutinesPage = auxRoutines.size/3 , isLastPageFav = routines.isLastPage, loadState = ApiState(ApiStatus.SUCCESS))
            }.onFailure { e->
                uiState = uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "Falló el fetch de myRoutines ${e.message}"))
                return@launch
            }
        }
    }

    fun moreMyRoutines(){
        if(uiState.isLastPageMyRoutines){
            return
        }
        var routineList = uiState.myRoutines
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            runCatching {
                routineRepository.getMyRoutines(page= if(routineList.size%3==0) uiState.myRoutinesPage+1 else uiState.myRoutinesPage , size=3)
            }.onSuccess { routines ->
                val auxRoutines=(if(routineList.size%3!=0) routineList.slice(0 until ( routineList.size- routineList.size%3) ) else routineList) + routines.content.map { it.asModel() }
                uiState = uiState.copy(myRoutines = auxRoutines, myRoutinesPage = auxRoutines.size/3 , isLastPageMyRoutines = routines.isLastPage, loadState = ApiState(ApiStatus.SUCCESS))
            }.onFailure { e->
                uiState = uiState.copy(loadState = ApiState(ApiStatus.FAILURE, "Falló el fetch de myRoutines ${e.message}"))
                return@launch
            }
        }
    }
    fun fetchFirstRoutines(){
        fetchJob?.cancel()
        if(uiState.myRoutines.isNotEmpty() || uiState.favouriteRoutines.isNotEmpty()){
            return
        }
        fetchJob = viewModelScope.launch {
                runCatching {
                    routineRepository.getMyRoutines(page = 0, size = 3)
                }.onSuccess { routines ->
                    uiState = uiState.copy(
                        myRoutines = routines.content.map { it.asModel() },
                        myRoutinesPage = 1,
                        isLastPageMyRoutines = routines.isLastPage
                    )
                }.onFailure { e ->
                    uiState = uiState.copy(
                        loadState = ApiState(
                            ApiStatus.FAILURE,
                            "Falló el fetch de myRoutines ${e.message}"
                        )
                    )
                    return@launch
                }
                runCatching {
                    routineRepository.getFavouriteRoutines(page = 0, size = 3)
                }.onSuccess { routines ->
                    uiState = uiState.copy(
                        favouriteRoutines = routines.content.map { it.asModel() },
                        favouriteRoutinesPage = 1,
                        isLastPageFav = routines.isLastPage,
                        loadState = ApiState(ApiStatus.SUCCESS)
                    )
                }.onFailure { e ->
                    uiState = uiState.copy(
                        loadState = ApiState(
                            ApiStatus.FAILURE,
                            "Falló el fetch de Favorites ${e.message}"
                        )
                    )
                }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                MyRoutinesViewModel(application.routineRepository)
            }
        }
    }
}