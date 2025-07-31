package com.OrLove.peoplemanager.ui.features.recognise_person.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.StringRes
import com.OrLove.peoplemanager.ui.models.IdentifiedPerson

interface RecognisePersonScreenContract {
    data class State(
        val isLoading: Boolean = false,
        val identifiedPerson: IdentifiedPerson = IdentifiedPerson(),
        val isCameraPermissionDialog: Boolean = false,
        @StringRes val errorTextRes: Int? = null,
        val isCameraOpened: Boolean = false,
        val isBackCameraActive: Boolean = false
    )

    sealed interface Event {
        data class PhotoChangedFromGalleryEvent(val photo: Uri) : Event
        data class PhotoChangedFromCameraEvent(val photo: Bitmap) : Event
        data object OpenCameraEvent : Event
        data object ShowCameraPermissionRationaleEvent : Event
        data object ClosePermissionDialog : Event
        data object CloseWarningDialog : Event
        data class BackCameraActiveEvent(val isActive: Boolean) : Event
    }

    sealed interface Effect
}