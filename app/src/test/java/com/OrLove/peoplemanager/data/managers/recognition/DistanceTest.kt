package com.OrLove.peoplemanager.data.managers.recognition

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import android.graphics.PointF
import org.junit.Before
import org.mockito.kotlin.mock

@RunWith(RobolectricTestRunner::class)
class DistanceTestWithRobolectric {

    private lateinit var recognitionManager: RecognitionManager

    @Before
    fun setUp() {
        recognitionManager = RecognitionManagerImpl(mock())
    }

    @Test
    fun `distance should calculate horizontal distance correctly`() {
        val p1 = PointF(0.0f, 5.0f)
        val p2 = PointF(3.0f, 5.0f)
        val result = recognitionManager.distance(p1, p2)
        assertEquals(3.0f, result, 0.001f)
    }

    @Test
    fun `distance should calculate vertical distance correctly`() {
        val p1 = PointF(5.0f, 0.0f)
        val p2 = PointF(5.0f, 4.0f)
        val result = recognitionManager.distance(p1, p2)
        assertEquals(4.0f, result, 0.001f)
    }

    @Test
    fun `distance should calculate diagonal distance correctly`() {
        val p1 = PointF(0.0f, 0.0f)
        val p2 = PointF(3.0f, 4.0f)
        val result = recognitionManager.distance(p1, p2)
        assertEquals(5.0f, result, 0.001f)
    }

    @Test
    fun `distance should handle negative coordinates`() {
        val p1 = PointF(-3.0f, -4.0f)
        val p2 = PointF(0.0f, 0.0f)
        val result = recognitionManager.distance(p1, p2)
        assertEquals(5.0f, result, 0.001f)
    }

    @Test
    fun `distance should handle mixed positive and negative coordinates`() {
        val p1 = PointF(-1.0f, 2.0f)
        val p2 = PointF(2.0f, -2.0f)
        val result = recognitionManager.distance(p1, p2)
        assertEquals(5.0f, result, 0.001f)
    }
}
