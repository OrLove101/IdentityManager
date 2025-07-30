package com.OrLove.peoplemanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.OrLove.peoplemanager.features.main.viewmodels.MainScreenContract
import com.OrLove.peoplemanager.utils.UnidirectionalViewModel
import com.OrLove.peoplemanager.utils.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(),
    UnidirectionalViewModel<MainScreenContract.State, MainScreenContract.Event, MainScreenContract.Effect>
    by mvi(MainScreenContract.State()) {

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