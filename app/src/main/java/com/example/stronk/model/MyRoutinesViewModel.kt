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

    private var fetchJob: Job? = null


    val routinesTest = listOf<Routine>(
        Routine(
            id = 1, name = "Abdos en 15mins",
            "abods", Date(165432697), 4, "Avanzado",
            UserRoutine(
                5,
                "Jorge",
                "M",
                "https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",
                Date(165432697)
            ),
            "Abdominales"
        ),
        Routine(
            id = 2, name = "Abdos en 15mins",
            "abods", Date(165432697), 4, "Avanzado",
            UserRoutine(
                1,
                "Jorge",
                "M",
                "https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",
                Date(165432697)
            ),
            "Abdominales"
        ),
        Routine(
            id = 3, name = "Abdos en 15mins",
            "abods", Date(165432697), 4, "Avanzado",
            UserRoutine(
                1,
                "Jorge",
                "M",
                "https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",
                Date(165432697)
            ),
            "Abdominales"
        ),
    )

    fun moreMyRoutines() {
        //Pido mas rutinas al repositorio
        uiState =
            uiState.copy(myRoutines = routinesTest, myRoutinesPage = uiState.myRoutinesPage + 1)
    }

    fun moreFavouriteRoutines() {
        //Pido mas rutinas favoritas al repositorio
        uiState = uiState.copy(
            myRoutines = routinesTest,
            favouriteRoutinesPage = uiState.favouriteRoutinesPage + 1
        )
    }

    fun fetchFirstRoutines() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            //uiState = uiState.copy(isLoading = true)
            runCatching {
                /*  val apiService=RetrofitClient.getApiService()
                val response = apiService.getRoutines()
                */
            }.onSuccess { // response->
                //uiState=uiState.copy(myRoutines = response.body(),myRoutinesPage = 1,isLoading = false)
            }.onFailure {//e->
                //  uiState = uiState.copy(message = e.message, isLoading = false)
            }
            try {
            } catch (e: Exception) {
                //  uiState = uiState.copy(message = "Error al obtener las rutinas", isLoading = false)
            }
        }
        //Pido las primeras rutinas al repositorio si ya tengo, no hago nada
        if (uiState.myRoutinesPage == 0) {
            uiState = uiState.copy(myRoutines = routinesTest, myRoutinesPage = 1)
        }
        if (uiState.favouriteRoutinesPage == 0) {
            uiState = uiState.copy(favouriteRoutines = routinesTest, favouriteRoutinesPage = 1)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StronkApplication)
                MyRoutinesViewModel(application.routineRepository)
            }
        }
    }
}