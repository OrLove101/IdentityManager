package com.OrLove.peoplemanager.ui.features.addperson.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.data.repositories.PeopleManagerRepository
import com.OrLove.peoplemanager.utils.UnidirectionalViewModel
import com.OrLove.peoplemanager.utils.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    private val peopleManagerRepository: PeopleManagerRepository
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
                viewModelScope.launch {
                    val photoUri = state.value.photo ?: run {
                        updateUiState {
                            copy(
                                errorTextRes = R.string.add_photo_warning
                            )
                        }
                        return@launch
                    }
                    val isSuccessfullySaved = peopleManagerRepository
                        .saveIdentifiedPerson(
                            name = state.value.name,
                            surname = state.value.surname,
                            position = state.value.position,
                            photoUri = photoUri
                        )
                    if (isSuccessfullySaved) {
                        if (state.value.validation.isSaveAttemptAvailable) {
                            emitSideEffect(
                                AddPersonScreenContract.Effect.PersonAddedEffect
                            )
                        }
                    } else {
                        updateUiState {
                            copy(
                                errorTextRes = R.string.cant_identify_face_from_photo
                            )
                        }
                    }
                }
            }

            AddPersonScreenContract.Event.ClosePermissionDialog -> {
                updateUiState {
                    copy(isCameraPermissionDialog = false)
                }
            }

            AddPersonScreenContract.Event.CloseWarningDialog -> {
                updateUiState {
                    copy(errorTextRes = null)
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
                viewModelScope.launch {
                    val photoUri = peopleManagerRepository
                        .saveBitmapTemporarilyAndReturnUri(photoBitmap = event.photo)
                    updateUiState {
                        copy(
                            photo = photoUri,
                            isCameraOpened = false
                        )
                    }
                }
            }
        }
    }
}