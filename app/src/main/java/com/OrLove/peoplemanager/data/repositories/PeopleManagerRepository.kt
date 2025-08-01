package com.OrLove.peoplemanager.data.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.OrLove.peoplemanager.ui.models.IdentifiedPerson

interface PeopleManagerRepository {
    suspend fun saveIdentifiedPerson(
        name: String,
        surname: String,
        position: String,
        photoUri: Uri
    ): Boolean

    suspend fun saveBitmapTemporarilyAndReturnUri(photoBitmap: Bitmap): Uri

    suspend fun identifyAndGetPersonByPhoto(bitmap: Bitmap): IdentifiedPerson?

    suspend fun identifyAndGetPersonByPhoto(uri: Uri): IdentifiedPerson?
}