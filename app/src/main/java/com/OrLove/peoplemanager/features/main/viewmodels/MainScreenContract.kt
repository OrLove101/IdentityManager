package com.OrLove.peoplemanager.features.main.viewmodels

interface MainScreenContract {
    data class State(
        val isLoading: Boolean = false
    )

    sealed interface Event {
        data object OnAddPersonClick: Event
        data object OnRecognisePersonClick: Event
    }

    sealed interface Effect {
        data object OpenAddPersonScreenEffect: Effect
        data object OpenRecognisePersonScreenEffect: Effect
    }
}