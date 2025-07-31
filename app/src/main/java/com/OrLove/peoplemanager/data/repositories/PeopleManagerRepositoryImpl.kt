package com.OrLove.peoplemanager.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.OrLove.peoplemanager.data.local.dao.IdentitiesDao
import com.OrLove.peoplemanager.data.local.entity.IdentityEntity
import com.OrLove.peoplemanager.data.managers.notification.AppNotificationManager
import com.OrLove.peoplemanager.data.managers.recognition.RecognitionManager
import com.OrLove.peoplemanager.ui.models.toEntity
import com.OrLove.peoplemanager.utils.AppDispatchers
import com.OrLove.peoplemanager.utils.Dispatcher
import com.OrLove.peoplemanager.utils.rotate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PeopleManagerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(AppDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val recognitionManager: RecognitionManager,
    private val appNotificationManager: AppNotificationManager,
    private val identitiesDao: IdentitiesDao
) : PeopleManagerRepository {
    override suspend fun saveIdentifiedPerson(
        name: String,
        surname: String,
        position: String,
        photoUri: Uri
    ): Boolean {
        return withContext(dispatcher) {
            val faceFeatures = recognitionManager.extractCoreFeatures(uri = photoUri)
            if (faceFeatures == null) {
                appNotificationManager.showCantIdentifyFaceNotification()
                return@withContext false
            }
            identitiesDao.insert(
                IdentityEntity(
                    name = name,
                    surname = surname,
                    position = position,
                    faceFeatures = faceFeatures.toEntity()
                )
            )
            return@withContext true
        }
    }

    override suspend fun saveBitmapTemporarilyAndReturnUri(photoBitmap: Bitmap): Uri {
        val uri = File(context.cacheDir, "temp_photo.jpg").apply {
            deleteOnExit()
            outputStream().use { stream ->
                if (!photoBitmap.rotate(90F)
                        .compress(Bitmap.CompressFormat.JPEG, 100, stream)
                ) {
                    Log.d("AddPersonViewModel", "Failed to compress photo")
                }
            }
        }.toUri()
        return uri
    }
}