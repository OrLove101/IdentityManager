package com.OrLove.peoplemanager.ui.features.main.viewmodels

interface MainScreenContract {
    data class State(
        val isLoading: Boolean = false,
        val isNotificationPermissionDialog: Boolean = false
    )

    sealed interface Event {
        data object OnAddPersonClick : Event
        data object OnRecognisePersonClick : Event
        data object ClosePermissionDialog : Event
        data object ShowCameraPermissionRationaleEvent : Event
    }

    sealed interface Effect {
        data object OpenAddPersonScreenEffect : Effect
        data object OpenRecognisePersonScreenEffect : Effect
    }
}