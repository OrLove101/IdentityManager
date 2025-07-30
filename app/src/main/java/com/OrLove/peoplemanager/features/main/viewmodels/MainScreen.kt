package com.OrLove.peoplemanager.features.main.viewmodels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.OrLove.peoplemanager.MainViewModel
import com.OrLove.peoplemanager.utils.collectInLaunchedEffect
import com.OrLove.peoplemanager.utils.use

@Composable
fun MainScreen(
    openAddPerson: () -> Unit,
    openRecognisePerson: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val (_, event, effect) = use(viewModel = viewModel)

    effect.collectInLaunchedEffect { collectedEffect ->
        when (collectedEffect) {
            MainScreenContract.Effect.OpenAddPersonScreenEffect -> openAddPerson()
            MainScreenContract.Effect.OpenRecognisePersonScreenEffect -> openRecognisePerson()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                event(MainScreenContract.Event.OnAddPersonClick)
            },
            content = {
                Text(text = "Add Person")
            }
        )
        Button(
            onClick = {
                event(MainScreenContract.Event.OnRecognisePersonClick)
            },
            content = {
                Text(text = "Recognise Person")
            }
        )
    }
}