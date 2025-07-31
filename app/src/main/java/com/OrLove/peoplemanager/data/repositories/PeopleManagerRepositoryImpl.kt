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
import com.OrLove.peoplemanager.ui.models.IdentifiedPerson
import com.OrLove.peoplemanager.ui.models.toEntity
import com.OrLove.peoplemanager.ui.models.toUi
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

    override suspend fun saveBitmapTemporarilyAndReturnUri(
        photoBitmap: Bitmap,
        isMadeFromBackCamera: Boolean
    ): Uri {
        val uri = File(context.cacheDir, "temp_photo.jpg").apply {
            deleteOnExit()
            outputStream().use { stream ->
                val bitmap = if (isMadeFromBackCamera) {
                    photoBitmap.rotate(90F)
                } else {
                    photoBitmap.rotate(-90f)
                }
                if (!bitmap
                        .compress(Bitmap.CompressFormat.JPEG, 100, stream)
                ) {
                    Log.d("AddPersonViewModel", "Failed to compress photo")
                }
            }
        }.toUri()
        return uri
    }

    override suspend fun identifyAndGetPersonByPhoto(
        bitmap: Bitmap,
        isMadeFromBackCamera: Boolean
    ): IdentifiedPerson? {
        return withContext(dispatcher) {
            val photoUri = saveBitmapTemporarilyAndReturnUri(
                photoBitmap = bitmap,
                isMadeFromBackCamera = isMadeFromBackCamera
            )
            return@withContext identifyAndGetPersonByPhoto(photoUri)
        }
    }

    override suspend fun identifyAndGetPersonByPhoto(uri: Uri): IdentifiedPerson? {
        val faceFeaturesToCompare = recognitionManager
            .extractCoreFeatures(uri = uri)
        return if (faceFeaturesToCompare != null) {
            val savedIdentities = identitiesDao.getAll()
            savedIdentities
                .map { identity ->
                    identity to recognitionManager
                        .compareFaces(identity.faceFeatures.toUi(), faceFeaturesToCompare)
                }
                .filter { comparisonResult -> comparisonResult.second.isTheSamePerson }
                .maxByOrNull { it.second.similarityMeasure }
                ?.first
                ?.toUi()
        } else {
            // TODO show notification
            Log.d(TAG, "identifyAndGetPersonByPhoto: cant identify null returned")
            null
        }
    }
}

private const val TAG = "PeopleManagerRepository"