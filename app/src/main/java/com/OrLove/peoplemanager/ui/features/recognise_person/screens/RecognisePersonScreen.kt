package com.OrLove.peoplemanager.ui.features.recognise_person.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import com.OrLove.peoplemanager.ui.features.recognise_person.viewmodels.RecognisePersonScreenContract
import com.OrLove.peoplemanager.ui.features.recognise_person.viewmodels.RecognisePersonViewModel
import com.OrLove.peoplemanager.ui.models.IdentifiedPerson
import com.OrLove.peoplemanager.utils.components.CameraPermissionDialog
import com.OrLove.peoplemanager.utils.components.ProgressLoader
import com.OrLove.peoplemanager.utils.components.WarningDialog
import com.OrLove.peoplemanager.utils.use
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun RecognisePersonScreen(
    viewModel: RecognisePersonViewModel = hiltViewModel(),
) {
    val (state, event, _) = use(viewModel = viewModel)
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { photo ->
            photo?.let {
                event(RecognisePersonScreenContract.Event.PhotoChangedFromGalleryEvent(photo = it))
            }
        }
    val cameraPermissionState =
        rememberPermissionState(Manifest.permission.CAMERA) { permissionData ->
            event(
                if (!permissionData) {
                    RecognisePersonScreenContract.Event.ShowCameraPermissionRationaleEvent
                } else {
                    RecognisePersonScreenContract.Event.OpenCameraEvent
                }
            )
        }

    if (state.isLoading) {
        ProgressLoader()
    }
    if (state.isCameraPermissionDialog && cameraPermissionState.status.shouldShowRationale) {
        CameraPermissionDialog(
            onDismiss = { event(RecognisePersonScreenContract.Event.ClosePermissionDialog) },
            actionText = stringResource(R.string.ok),
            actionClick = {
                cameraPermissionState.launchPermissionRequest()
            }
        )
    }
    if (state.errorTextRes != null) {
        WarningDialog(
            onDismiss = {
                event(RecognisePersonScreenContract.Event.CloseWarningDialog)
            },
            actionClick = {
                event(RecognisePersonScreenContract.Event.CloseWarningDialog)
            },
            contentText = stringResource(state.errorTextRes)
        )
    }
    if (state.isCameraOpened) {
        PeopleManagerCamera(
            makePhoto = { photo ->
                event(
                    RecognisePersonScreenContract.Event.PhotoChangedFromCameraEvent(photo = photo)
                )
            }
        )
    } else {
        ScreenFieldsContent(
            identifiedPerson = state.identifiedPerson,
            callback = event,
            cameraPermissionState = cameraPermissionState,
            galleryLauncher = galleryLauncher
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScreenFieldsContent(
    identifiedPerson: IdentifiedPerson,
    callback: (RecognisePersonScreenContract.Event) -> Unit,
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
        if (identifiedPerson.name.isNotEmpty()) {
            Text(text = stringResource(R.string.name, identifiedPerson.name))
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        if (identifiedPerson.surname.isNotEmpty()) {
            Text(text = stringResource(R.string.surname, identifiedPerson.surname))
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        if (identifiedPerson.position.isNotEmpty()) {
            Text(text = stringResource(R.string.position, identifiedPerson.position))
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        Spacer(modifier = Modifier.height(height = 24.dp))
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            content = { Text(text = stringResource(R.string.select_photo_from_gallery)) }
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Button(
            onClick = {
                if (cameraPermissionState.status.isGranted) {
                    event(RecognisePersonScreenContract.Event.OpenCameraEvent)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },
            content = { Text(text = stringResource(R.string.make_photo_to_identify_person)) }
        )
    }
}