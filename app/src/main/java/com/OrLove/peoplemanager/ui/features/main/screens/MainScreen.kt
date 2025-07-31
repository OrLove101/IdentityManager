package com.OrLove.peoplemanager.ui.features.main.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.ui.features.main.viewmodels.MainScreenContract
import com.OrLove.peoplemanager.ui.features.main.viewmodels.MainViewModel
import com.OrLove.peoplemanager.utils.collectInLaunchedEffect
import com.OrLove.peoplemanager.utils.components.NotificationPermissionDialog
import com.OrLove.peoplemanager.utils.use
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    openAddPerson: () -> Unit,
    openRecognisePerson: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel = viewModel)
    val notificationPermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { isGranted ->
                if (!isGranted) {
                    event(MainScreenContract.Event.ShowCameraPermissionRationaleEvent)
                }
            }
        } else {
            null
        }

    effect.collectInLaunchedEffect { collectedEffect ->
        when (collectedEffect) {
            MainScreenContract.Effect.OpenAddPersonScreenEffect -> openAddPerson()
            MainScreenContract.Effect.OpenRecognisePersonScreenEffect -> openRecognisePerson()
        }
    }

    LaunchedEffect(Unit) {
        if (notificationPermissionState?.status?.isGranted == false) {
            notificationPermissionState.launchPermissionRequest()
        }
    }

    if (state.isNotificationPermissionDialog && notificationPermissionState?.status?.shouldShowRationale == true) {
        NotificationPermissionDialog(
            onDismiss = { event(MainScreenContract.Event.ClosePermissionDialog) },
            actionText = stringResource(R.string.ok),
            actionClick = {
                notificationPermissionState.launchPermissionRequest()
            }
        )
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
                Text(text = stringResource(R.string.add_person))
            }
        )
        Button(
            onClick = {
                event(MainScreenContract.Event.OnRecognisePersonClick)
            },
            content = {
                Text(text = stringResource(R.string.recognise_person))
            }
        )
    }
}