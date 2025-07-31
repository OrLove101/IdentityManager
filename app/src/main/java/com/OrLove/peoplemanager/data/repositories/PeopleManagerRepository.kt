package com.OrLove.peoplemanager.data.repositories

import android.graphics.Bitmap
import android.net.Uri

interface PeopleManagerRepository {
    suspend fun saveIdentifiedPerson(
        name: String,
        surname: String,
        position: String,
        photoUri: Uri
    ): Boolean

    suspend fun saveBitmapTemporarilyAndReturnUri(photoBitmap: Bitmap): Uri
}