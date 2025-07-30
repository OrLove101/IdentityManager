package com.OrLove.peoplemanager.features.addperson.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.OrLove.peoplemanager.utils.UnidirectionalViewModel
import com.OrLove.peoplemanager.utils.mvi
import com.OrLove.peoplemanager.utils.rotate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel(),
    UnidirectionalViewModel<AddPersonScreenContract.State, AddPersonScreenContract.Event, AddPersonScreenContract.Effect>
    by mvi(AddPersonScreenContract.State()) {

    override fun event(event: AddPersonScreenContract.Event) {
        when (event) {
            is AddPersonScreenContract.Event.NameChangedEvent -> {
                updateUiState {
                    copy(
                        name = event.name,
                        validation = validation.copy(
                            isNameValid = event.name.isNotBlank()
                        )
                    )
                }
            }

            is AddPersonScreenContract.Event.PhotoChangedFromGalleryEvent -> {
                updateUiState {
                    copy(
                        photo = event.photo
                    )
                }
            }

            is AddPersonScreenContract.Event.PositionChangedEvent -> {
                updateUiState {
                    copy(
                        position = event.position,
                        validation = validation.copy(
                            isPositionValid = event.position.isNotBlank()
                        )
                    )
                }
            }

            is AddPersonScreenContract.Event.SurnameChangedEvent -> {
                updateUiState {
                    copy(
                        surname = event.surname,
                        validation = validation.copy(
                            isSurnameValid = event.surname.isNotBlank()
                        )
                    )
                }
            }

            AddPersonScreenContract.Event.SaveUserClickedEvent -> {
                updateUiState {
                    validate()
                }
                // validate photo
                // TODO
                if (state.value.validation.isSaveAttemptAvailable) {
                    viewModelScope.emitSideEffect(
                        AddPersonScreenContract.Effect.PersonAddedEffect
                    )
                }
            }

            AddPersonScreenContract.Event.ClosePermissionDialog -> {
                updateUiState {
                    copy(isCameraPermissionDialog = false)
                }
            }

            AddPersonScreenContract.Event.OpenCameraEvent -> {
                updateUiState {
                    copy(isCameraOpened = true)
                }
            }

            AddPersonScreenContract.Event.ShowCameraPermissionRationaleEvent -> {
                updateUiState {
                    copy(isCameraPermissionDialog = true)
                }
            }

            is AddPersonScreenContract.Event.PhotoChangedFromCameraEvent -> {
                val uri = File(context.cacheDir, "temp_photo.jpg").apply {
                    deleteOnExit()
                    outputStream().use { stream ->
                        if (!event.photo.rotate(90F)
                                .compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        ) {
                            Log.d("AddPersonViewModel", "Failed to compress photo")
                        }
                    }
                }.toUri()
                updateUiState {
                    copy(
                        photo = uri,
                        isCameraOpened = false
                    )
                }
            }
        }
    }

    // make photo or choose from gallery

    // enter PIB and position (3 fields name, surname, position + photo preview)

    // save photo and PIB with position in room
}