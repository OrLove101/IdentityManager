package com.orlove.addperson.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.ui.features.addperson.components.PeopleManagerCamera
import com.OrLove.peoplemanager.ui.features.addperson.viewmodels.AddPersonScreenContract
import com.OrLove.peoplemanager.ui.features.addperson.viewmodels.AddPersonViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.OrLove.peoplemanager.utils.collectInLaunchedEffect
import com.OrLove.peoplemanager.utils.components.CameraPermissionDialog
import com.OrLove.peoplemanager.utils.components.ProgressLoader
import com.OrLove.peoplemanager.utils.components.WarningDialog
import com.OrLove.peoplemanager.utils.use
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddPersonScreen(
    popBack: () -> Unit,
    viewModel: AddPersonViewModel = hiltViewModel()
) {
    val (state, event, effect) = use(viewModel = viewModel)
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { photo ->
            photo?.let {
                event(AddPersonScreenContract.Event.PhotoChangedFromGalleryEvent(photo = it))
            }
        }
    val cameraPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA) { permissionData ->
            event(
                if (!permissionData) {
                    AddPersonScreenContract.Event.ShowCameraPermissionRationaleEvent
                } else {
                    AddPersonScreenContract.Event.OpenCameraEvent
                }
            )
        }

    effect.collectInLaunchedEffect { collectedEffect ->
        when (collectedEffect) {
            AddPersonScreenContract.Effect.PersonAddedEffect -> popBack()
        }
    }

    if (state.isLoading) {
        ProgressLoader()
    }
    if (state.isCameraPermissionDialog && cameraPermissionState.status.shouldShowRationale) {
        CameraPermissionDialog(
            onDismiss = { event(AddPersonScreenContract.Event.ClosePermissionDialog) },
            actionText = stringResource(R.string.ok),
            actionClick = {
                cameraPermissionState.launchPermissionRequest()
            }
        )
    }
    if (state.errorTextRes != null) {
        WarningDialog(
            onDismiss = {
                event(AddPersonScreenContract.Event.CloseWarningDialog)
            },
            actionClick = {
                event(AddPersonScreenContract.Event.CloseWarningDialog)
            },
            contentText = stringResource(state.errorTextRes)
        )
    }
    if (state.isCameraOpened) {
        PeopleManagerCamera(
            makePhoto = { photo ->
                event(
                    AddPersonScreenContract.Event.PhotoChangedFromCameraEvent(photo = photo)
                )
            }
        )
    } else {
        ScreenFieldsContent(
            state = state,
            callback = event,
            cameraPermissionState = cameraPermissionState,
            galleryLauncher = galleryLauncher
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
private fun ScreenFieldsContent(
    state: AddPersonScreenContract.State,
    callback: (AddPersonScreenContract.Event) -> Unit,
    cameraPermissionState: PermissionState,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    val event by rememberUpdatedState(callback)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = state.photo,
            contentDescription = "Person photo",
            modifier = Modifier.size(size = 300.dp)
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.NONE)
            it.skipMemoryCache(true)
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
        OutlinedTextField(
            value = state.name,
            onValueChange = { event(AddPersonScreenContract.Event.NameChangedEvent(it)) },
            label = { Text(text = stringResource(R.string.name_no_arg)) },
            isError = !state.validation.isNameValid
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        OutlinedTextField(
            value = state.surname,
            onValueChange = { event(AddPersonScreenContract.Event.SurnameChangedEvent(it)) },
            label = { Text(text = stringResource(R.string.surname_no_arg)) },
            isError = !state.validation.isSurnameValid
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        OutlinedTextField(
            value = state.position,
            onValueChange = { event(AddPersonScreenContract.Event.PositionChangedEvent(it)) },
            label = { Text(text = stringResource(R.string.position_no_arg)) },
            isError = !state.validation.isPositionValid
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            onClick = {
                if (cameraPermissionState.status.isGranted) {
                    event(AddPersonScreenContract.Event.OpenCameraEvent)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },
            content = { Text(text = stringResource(R.string.make_photo_with_camera)) }
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            content = { Text(text = stringResource(R.string.select_photo_from_gallery)) }
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            onClick = { event(AddPersonScreenContract.Event.SaveUserClickedEvent) },
            enabled = state.validation.isSaveAttemptAvailable,
            content = { Text(text = stringResource(R.string.save)) }
        )
    }
}

