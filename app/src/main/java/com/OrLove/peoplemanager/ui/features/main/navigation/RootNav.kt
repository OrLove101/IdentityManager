package com.OrLove.peoplemanager.ui.features.main.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.OrLove.peoplemanager.ui.features.main.screens.MainScreen
import com.OrLove.peoplemanager.ui.features.recognise_person.navigation.navigateToRecognisePerson
import com.OrLove.peoplemanager.ui.features.recognise_person.navigation.recognisePersonScreen
import com.orlove.addperson.navigation.addPersonScreen
import com.orlove.addperson.navigation.navigateToAddPerson

private const val MAIN_SCREEN_ROUTE = "main_screen"

@Composable
fun RootNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MAIN_SCREEN_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable(MAIN_SCREEN_ROUTE) {
            MainScreen(
                openAddPerson = navController::navigateToAddPerson,
                openRecognisePerson = navController::navigateToRecognisePerson,
            )
        }
        recognisePersonScreen()
        addPersonScreen(
            popBack = navController::popBackStack
        )
    }
}