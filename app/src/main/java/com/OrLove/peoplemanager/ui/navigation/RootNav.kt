package com.OrLove.peoplemanager.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.OrLove.peoplemanager.ui.viewmodels.MainScreen
import com.orlove.addperson.navigation.addPersonScreen
import com.orlove.addperson.navigation.navigateToAddPerson
import com.orlove.recognise_person.navigation.navigateToRecognisePerson
import com.orlove.recognise_person.navigation.recognisePersonScreen

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
        addPersonScreen()
    }
}