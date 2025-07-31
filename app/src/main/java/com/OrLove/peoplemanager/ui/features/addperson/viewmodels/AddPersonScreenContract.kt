package com.OrLove.peoplemanager.ui.features.addperson.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes

interface AddPersonScreenContract {
    data class State(
        val isLoading: Boolean = false,
        val name: String = "",
        val surname: String = "",
        val position: String = "",
        val photo: Uri? = null,
        val isCameraPermissionDialog: Boolean = false,
        @StringRes val errorTextRes: Int? = null,
        val isCameraOpened: Boolean = false,
        val validation: Validation = Validation()
    ) {
        data class Validation(
            val isNameValid: Boolean = true,
            val isSurnameValid: Boolean = true,
            val isPositionValid: Boolean = true,
            val isPhotoValid: Boolean = true
        ) {
            val isSaveAttemptAvailable: Boolean
                get() = isNameValid && isSurnameValid && isPositionValid
        }

        fun validate() = copy(
            validation = validation.copy(
                isNameValid = name.isNotBlank(),
                isSurnameValid = surname.isNotBlank(),
                isPositionValid = position.isNotBlank()
            )
        )
    }

    sealed interface Event {
        data class NameChangedEvent(val name: String) : Event
        data class SurnameChangedEvent(val surname: String) : Event
        data class PositionChangedEvent(val position: String) : Event
        data class PhotoChangedFromGalleryEvent(val photo: Uri) : Event
        data class PhotoChangedFromCameraEvent(val photo: Bitmap) : Event
        data object SaveUserClickedEvent : Event
        data object OpenCameraEvent : Event
        data object ShowCameraPermissionRationaleEvent : Event
        data object ClosePermissionDialog : Event
        data object CloseWarningDialog : Event
    }

    sealed interface Effect {
        data object PersonAddedEffect : Effect
    }
}