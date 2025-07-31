package com.OrLove.peoplemanager.ui.features.recognise_person.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.data.NoIdentityFoundException
import com.OrLove.peoplemanager.data.repositories.PeopleManagerRepository
import com.OrLove.peoplemanager.utils.UnidirectionalViewModel
import com.OrLove.peoplemanager.utils.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecognisePersonViewModel @Inject constructor(
    private val peopleManagerRepository: PeopleManagerRepository
) : ViewModel(),
    UnidirectionalViewModel<RecognisePersonScreenContract.State, RecognisePersonScreenContract.Event, RecognisePersonScreenContract.Effect>
    by mvi(RecognisePersonScreenContract.State()) {

    override fun event(event: RecognisePersonScreenContract.Event) {
        when (event) {
            RecognisePersonScreenContract.Event.ClosePermissionDialog -> {
                updateUiState {
                    copy(isCameraPermissionDialog = false)
                }
            }

            RecognisePersonScreenContract.Event.CloseWarningDialog -> {
                updateUiState {
                    copy(errorTextRes = null)
                }
            }

            RecognisePersonScreenContract.Event.OpenCameraEvent -> {
                updateUiState {
                    copy(isCameraOpened = true)
                }
            }

            is RecognisePersonScreenContract.Event.PhotoChangedFromCameraEvent ->
                viewModelScope.launch {
                    updateUiState {
                        copy(
                            isLoading = true,
                            isCameraOpened = false
                        )
                    }
                    try {
                        val identifiedPerson = peopleManagerRepository
                            .identifyAndGetPersonByPhoto(
                                bitmap = event.photo,
                                isMadeFromBackCamera = state.value.isBackCameraActive
                            )
                        if (identifiedPerson != null) {
                            updateUiState {
                                copy(
                                    identifiedPerson = identifiedPerson
                                )
                            }
                        } else {
                            updateUiState {
                                copy(
                                    errorTextRes = R.string.cant_identify_face_from_photo
                                )
                            }
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is NoIdentityFoundException -> {
                                updateUiState {
                                    copy(
                                        errorTextRes = R.string.cant_find_such_identity_in_db_pleas_try_again
                                    )
                                }
                            }

                            else -> {
                                updateUiState {
                                    copy(
                                        errorTextRes = R.string.something_went_wrong
                                    )
                                }
                            }
                        }
                    } finally {
                        updateUiState {
                            copy(
                                isLoading = false
                            )
                        }
                    }
                }

            is RecognisePersonScreenContract.Event.PhotoChangedFromGalleryEvent ->
                viewModelScope.launch {
                    updateUiState {
                        copy(
                            isLoading = true,
                            isCameraOpened = false
                        )
                    }
                    try {
                        val identifiedPerson = peopleManagerRepository
                            .identifyAndGetPersonByPhoto(uri = event.photo)
                        if (identifiedPerson != null) {
                            updateUiState {
                                copy(
                                    identifiedPerson = identifiedPerson
                                )
                            }
                        } else {
                            updateUiState {
                                copy(
                                    errorTextRes = R.string.cant_identify_face_from_photo
                                )
                            }
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is NoIdentityFoundException -> {
                                updateUiState {
                                    copy(
                                        errorTextRes = R.string.cant_find_such_identity_in_db_pleas_try_again
                                    )
                                }
                            }

                            else -> {
                                updateUiState {
                                    copy(
                                        errorTextRes = R.string.something_went_wrong
                                    )
                                }
                            }
                        }
                    } finally {
                        updateUiState {
                            copy(
                                isLoading = false
                            )
                        }
                    }
                }

            RecognisePersonScreenContract.Event.ShowCameraPermissionRationaleEvent -> {
                updateUiState {
                    copy(isCameraPermissionDialog = true)
                }
            }

            is RecognisePersonScreenContract.Event.BackCameraActiveEvent -> {
                updateUiState {
                    copy(
                        isBackCameraActive = event.isActive
                    )
                }
            }
        }
    }
}