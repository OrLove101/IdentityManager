package com.OrLove.peoplemanager.data.managers.recognition

import android.net.Uri
import com.OrLove.peoplemanager.ui.models.CoreFaceFeatures

interface RecognitionManager {
    suspend fun compareFaces(
        features1: CoreFaceFeatures,
        features2: CoreFaceFeatures
    ): Pair<Boolean, Float>

    suspend fun extractCoreFeatures(uri: Uri): CoreFaceFeatures?
}