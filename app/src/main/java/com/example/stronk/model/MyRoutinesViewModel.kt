package com.example.stronk.model

import androidx.lifecycle.ViewModel
import com.example.stronk.state.MyRoutinesState
import com.example.stronk.state.Routine
import com.example.stronk.state.User

class MyRoutinesViewModel : ViewModel() {
    var uiState = MyRoutinesState()

    val routinesTest= listOf<Routine>(Routine(id=1,name="Abdos en 15mins",
        "abods",165432697,4,"Avanzado",
        User(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",165432697),
        "Abdominales"),
        Routine(id=2,name="Abdos en 15mins",
            "abods",165432697,4,"Avanzado",
            User(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",165432697),
            "Abdominales"),
        Routine(id=3,name="Abdos en 15mins",
            "abods",165432697,4,"Avanzado",
            User(1,"Jorge","M","https://i.pinimg.com/originals/7a/0d/0d/7a0d0d8b1b0c1b0c1b0c1b0c1b0c1b0c.jpg",165432697),
            "Abdominales"),
        )

    fun moreMyRoutines(){
        //Pido mas rutinas al repositorio
        uiState = uiState.copy(myRoutines = routinesTest,myRoutinesPage = uiState.myRoutinesPage + 1)
    }

    fun moreFavouriteRoutines(){
        //Pido mas rutinas favoritas al repositorio
        uiState = uiState.copy(myRoutines = routinesTest,favouriteRoutinesPage = uiState.favouriteRoutinesPage + 1)
    }
    fun fetchFirstRoutines(){
        //Pido las primeras rutinas al repositorio si ya tengo, no hago nada
        if (uiState.myRoutinesPage == 0){
            uiState = uiState.copy(myRoutines = routinesTest,myRoutinesPage =1)
        }
        if (uiState.favouriteRoutinesPage== 0){
            uiState = uiState.copy(favouriteRoutines = routinesTest,favouriteRoutinesPage =1)
        }
    }
}