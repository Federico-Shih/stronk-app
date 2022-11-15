package com.example.stronk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stronk.R
import com.example.stronk.RoutineButton
import com.example.stronk.model.MyRoutinesViewModel
import com.example.stronk.network.services.FavouriteApiService
import com.example.stronk.network.services.RoutineApiService
import com.example.stronk.state.Routine
import com.example.stronk.ui.components.LoadDependingContent

@Composable
fun myRoutinesScreen(onNavigateToViewRoutine: (routineId: Int) -> Unit,
                     myRoutinesViewModel:MyRoutinesViewModel = viewModel(factory = MyRoutinesViewModel.Factory)
) {
  val state=myRoutinesViewModel.uiState

  Column(modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())) {
    LoadDependingContent(loadState = state.loadState) {
      routineList(state.myRoutines, stringResource(R.string.MyRoutines),
        { myRoutinesViewModel.moreMyRoutines()},onNavigateToViewRoutine,state.isLastPageMyRoutines)
      routineList(state.favouriteRoutines, stringResource(R.string.FavRoutines),
        { myRoutinesViewModel.moreFavouriteRoutines()},onNavigateToViewRoutine,state.isLastPageFav)
    }

  }
}

@Composable
fun routineList(routines:List<Routine> = listOf(),title:String="Rutinas",onShowMore:()->Unit={}, onNavigateToViewRoutine:(routineId:Int)->Unit={},
isLastPage:Boolean=false) {
  Column(modifier = Modifier
    .padding(10.dp)
    .wrapContentHeight()
    .fillMaxWidth()) {
    Row(modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(
        text = title,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .padding(bottom = 4.dp)
          .alignByBaseline()
      )
      if(!isLastPage) {
        Button(onClick = {onShowMore()}, modifier = Modifier.alignByBaseline()) {
          Text(text = stringResource(R.string.ShowMore),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold)
        }
      }
    }
    Column(modifier= Modifier
      .fillMaxWidth()
      .wrapContentHeight()) {
      routines.forEach{ routine->
        RoutineButton(routine.id,R.drawable.abdos,routine.name, onNavigateToViewRoutine=onNavigateToViewRoutine,
        modifierButton = Modifier
          .fillMaxWidth()
          .height(150.dp)
          .padding(bottom = 5.dp))
      }
    }

  }

}



