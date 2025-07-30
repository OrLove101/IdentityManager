package com.orlove.recognise_person.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.orlove.recognise_person.screens.RecognisePersonScreen

private const val RECOGNISE_PERSON_ROUTE = "route.recognisePerson"

fun NavGraphBuilder.recognisePersonScreen() {
    composable(route = RECOGNISE_PERSON_ROUTE) {
        RecognisePersonScreen()
    }
}

fun NavController.navigateToRecognisePerson() {
    this.navigate(RECOGNISE_PERSON_ROUTE)
}