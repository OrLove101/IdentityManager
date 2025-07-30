package com.OrLove.peoplemanager.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.OrLove.peoplemanager.ui.viewmodels.MainScreenContract
import com.orlove.core.UnidirectionalViewModel
import com.orlove.core.mvi
import javax.inject.Inject

class MainViewModel @Inject constructor(

) : ViewModel(),
    UnidirectionalViewModel<MainScreenContract.State, MainScreenContract.Event, MainScreenContract.Effect>
    by mvi(
        MainScreenContract.State(
            isLoading = false
        )
    ) {

    override fun event(event: MainScreenContract.Event) {
        when (event) {
            MainScreenContract.Event.OnAddPersonClick -> viewModelScope.emitSideEffect(
                MainScreenContract.Effect.OpenAddPersonScreenEffect
            )

            MainScreenContract.Event.OnRecognisePersonClick -> viewModelScope.emitSideEffect(
                MainScreenContract.Effect.OpenRecognisePersonScreenEffect
            )
        }
    }
}