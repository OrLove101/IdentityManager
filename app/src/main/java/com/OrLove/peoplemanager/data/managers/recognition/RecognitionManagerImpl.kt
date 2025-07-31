package com.OrLove.peoplemanager.data.managers.recognition

import android.content.Context
import android.graphics.PointF
import android.net.Uri
import android.util.Log
import com.OrLove.peoplemanager.ui.models.CoreFaceFeatures
import com.OrLove.peoplemanager.ui.models.FaceComparisonResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class RecognitionManagerImpl(
    @ApplicationContext private val context: Context
) : RecognitionManager {

    override suspend fun compareFaces(
        features1: CoreFaceFeatures,
        features2: CoreFaceFeatures
    ): FaceComparisonResult =
        coroutineScope {
            val similarity = calculateCoreSimilarity(features1, features2)
            val isSamePerson = similarity > 0.80f
            Log.d(TAG, "compareFaces: similarity=$similarity isSamePerson=$isSamePerson")
            FaceComparisonResult(
                isTheSamePerson = isSamePerson,
                similarityMeasure = similarity
            )
        }

    override suspend fun extractCoreFeatures(uri: Uri): CoreFaceFeatures? =
        suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromFilePath(context, uri)

            val detector = FaceDetection.getClient(
                FaceDetectorOptions.Builder()
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setMinFaceSize(0.15f)
                    .build()
            )

            detector.process(image)
                .addOnSuccessListener { faces ->
                    try {
                        val face =
                            faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
                        val result = face?.let { calculateCoreFeatures(it) }
                        continuation.resume(result)
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
                .addOnCompleteListener {
                    detector.close()
                }
        }

    private fun calculateCoreFeatures(face: Face): CoreFaceFeatures? {
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position
        val noseBase = face.getLandmark(FaceLandmark.NOSE_BASE)?.position
        val mouthLeft = face.getLandmark(FaceLandmark.MOUTH_LEFT)?.position
        val mouthRight = face.getLandmark(FaceLandmark.MOUTH_RIGHT)?.position

        if (leftEye == null || rightEye == null) return null

        val faceWidth = face.boundingBox.width().toFloat()
        val faceHeight = face.boundingBox.height().toFloat()

        val eyeDistance = distance(leftEye, rightEye)
        val eyeDistanceRatio = eyeDistance / faceWidth
        val faceAspectRatio = faceWidth / faceHeight
        val eyeCenter = PointF((leftEye.x + rightEye.x) / 2, (leftEye.y + rightEye.y) / 2)
        val noseToEyesRatio = if (noseBase != null) {
            distance(eyeCenter, noseBase) / faceHeight
        } else -1f
        val eyeToMouthRatio = if (mouthLeft != null && mouthRight != null) {
            val mouthCenter =
                PointF((mouthLeft.x + mouthRight.x) / 2, (mouthLeft.y + mouthRight.y) / 2)
            distance(eyeCenter, mouthCenter) / faceHeight
        } else -1f

        return CoreFaceFeatures(
            eyeDistanceRatio = eyeDistanceRatio,
            faceAspectRatio = faceAspectRatio,
            noseToEyesRatio = noseToEyesRatio,
            eyeToMouthRatio = eyeToMouthRatio
        )
    }

    private fun calculateCoreSimilarity(
        features1: CoreFaceFeatures,
        features2: CoreFaceFeatures
    ): Float {
        var totalSimilarity = 0f
        var totalWeight = 0f

        fun compareFeature(val1: Float, val2: Float, weight: Float, maxDifference: Float): Float {
            if (val1 < 0f || val2 < 0f) return 0f

            val difference = abs(val1 - val2)
            val similarity = max(0f, 1f - (difference / maxDifference))

            totalSimilarity += similarity * weight
            totalWeight += weight
            return similarity
        }

        compareFeature(features1.eyeDistanceRatio, features2.eyeDistanceRatio, 5f, 0.04f)
        compareFeature(features1.faceAspectRatio, features2.faceAspectRatio, 3f, 0.15f)
        compareFeature(features1.noseToEyesRatio, features2.noseToEyesRatio, 1.5f, 0.08f)
        compareFeature(features1.eyeToMouthRatio, features2.eyeToMouthRatio, 0.5f, 0.08f)

        return if (totalWeight > 0) totalSimilarity / totalWeight else 0f
    }

    private fun distance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }
}

private const val TAG = "RecognitionManagerImpl"