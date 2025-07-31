package com.OrLove.peoplemanager.data.managers.recognition

import com.OrLove.peoplemanager.ui.models.CoreFaceFeatures
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class RecognitionManagerImplTest {

    private lateinit var recognitionManager: RecognitionManager

    @Before
    fun setUp() {
        recognitionManager = RecognitionManagerImpl(mock())
    }

    @Test
    fun `calculateCoreSimilarity should return 1 for identical features`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 1.3f,
            noseToEyesRatio = 0.8f,
            eyeToMouthRatio = 0.6f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 1.3f,
            noseToEyesRatio = 0.8f,
            eyeToMouthRatio = 0.6f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(1.0f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should return 0 when all features are negative`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = -0.1f,
            faceAspectRatio = -0.2f,
            noseToEyesRatio = -0.3f,
            eyeToMouthRatio = -0.4f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = -0.5f,
            faceAspectRatio = -0.6f,
            noseToEyesRatio = -0.7f,
            eyeToMouthRatio = -0.8f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(0.0f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should handle mixed positive and negative values`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = -1.0f,
            noseToEyesRatio = 0.8f,
            eyeToMouthRatio = -0.5f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.52f,
            faceAspectRatio = 1.3f,
            noseToEyesRatio = 0.82f,
            eyeToMouthRatio = 0.6f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)

        val expectedEyeSimilarity = 1f - (0.02f / 0.04f)
        val expectedNoseSimilarity = 1f - (0.02f / 0.08f)
        val expectedResult = (expectedEyeSimilarity * 5f + expectedNoseSimilarity * 1.5f) / (5f + 1.5f)

        assertEquals(expectedResult, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should handle maximum differences correctly`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.0f,
            faceAspectRatio = 0.0f,
            noseToEyesRatio = 0.0f,
            eyeToMouthRatio = 0.0f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.04f,
            faceAspectRatio = 0.15f,
            noseToEyesRatio = 0.08f,
            eyeToMouthRatio = 0.08f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(0.0f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should handle differences beyond maximum`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.0f,
            faceAspectRatio = 0.0f,
            noseToEyesRatio = 0.0f,
            eyeToMouthRatio = 0.0f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.1f,
            faceAspectRatio = 0.3f,
            noseToEyesRatio = 0.2f,
            eyeToMouthRatio = 0.2f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(0.0f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should calculate weighted average correctly`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 1.0f,
            noseToEyesRatio = 0.8f,
            eyeToMouthRatio = 0.6f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.52f,
            faceAspectRatio = 1.075f,
            noseToEyesRatio = 0.84f,
            eyeToMouthRatio = 0.64f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(0.5f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should handle edge case with zero values`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.0f,
            faceAspectRatio = 0.0f,
            noseToEyesRatio = 0.0f,
            eyeToMouthRatio = 0.0f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.0f,
            faceAspectRatio = 0.0f,
            noseToEyesRatio = 0.0f,
            eyeToMouthRatio = 0.0f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertEquals(1.0f, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should prioritize eyeDistanceRatio due to highest weight`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 0.0f,
            noseToEyesRatio = 0.0f,
            eyeToMouthRatio = 0.0f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 1.0f,
            noseToEyesRatio = 1.0f,
            eyeToMouthRatio = 1.0f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        val expectedResult = (1.0f * 5f + 0f * 3f + 0f * 1.5f + 0f * 0.5f) / (5f + 3f + 1.5f + 0.5f)
        assertEquals(expectedResult, result, 0.001f)
    }

    @Test
    fun `calculateCoreSimilarity should handle small differences accurately`() {
        val features1 = CoreFaceFeatures(
            eyeDistanceRatio = 0.5f,
            faceAspectRatio = 1.3f,
            noseToEyesRatio = 0.8f,
            eyeToMouthRatio = 0.6f
        )
        val features2 = CoreFaceFeatures(
            eyeDistanceRatio = 0.501f,
            faceAspectRatio = 1.301f,
            noseToEyesRatio = 0.801f,
            eyeToMouthRatio = 0.601f
        )
        val result = recognitionManager.calculateCoreSimilarity(features1, features2)
        assertTrue("Result should be very close to 1.0", result > 0.95f)
        assertTrue("Result should be less than 1.0", result < 1.0f)
    }
}