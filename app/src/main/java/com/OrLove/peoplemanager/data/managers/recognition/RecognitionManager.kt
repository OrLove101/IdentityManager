package com.OrLove.peoplemanager.data.managers.recognition

import android.graphics.PointF
import android.net.Uri
import com.OrLove.peoplemanager.ui.models.CoreFaceFeatures
import com.OrLove.peoplemanager.ui.models.FaceComparisonResult

interface RecognitionManager {
    suspend fun compareFaces(
        features1: CoreFaceFeatures,
        features2: CoreFaceFeatures
    ): FaceComparisonResult

    suspend fun extractCoreFeatures(uri: Uri): CoreFaceFeatures?

    fun calculateCoreSimilarity(
        features1: CoreFaceFeatures,
        features2: CoreFaceFeatures
    ): Float

    fun distance(p1: PointF, p2: PointF): Float
}