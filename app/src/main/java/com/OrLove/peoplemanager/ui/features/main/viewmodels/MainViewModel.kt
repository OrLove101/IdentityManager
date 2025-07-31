package com.OrLove.peoplemanager.ui.features.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            MainScreenContract.Event.ClosePermissionDialog -> {
                updateUiState {
                    copy(
                        isNotificationPermissionDialog = false
                    )
                }
            }

            MainScreenContract.Event.ShowCameraPermissionRationaleEvent -> {
                updateUiState {
                    copy(
                        isNotificationPermissionDialog = true
                    )
                }
            }
        }
    }
}