package com.orlove.addperson.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.orlove.addperson.screens.AddPersonScreen

private const val ADD_PERSON_ROUTE = "route.addPerson"

fun NavGraphBuilder.addPersonScreen() {
    composable(route = ADD_PERSON_ROUTE) {
        AddPersonScreen()
    }
}

fun NavController.navigateToAddPerson() {
    this.navigate(ADD_PERSON_ROUTE)
}