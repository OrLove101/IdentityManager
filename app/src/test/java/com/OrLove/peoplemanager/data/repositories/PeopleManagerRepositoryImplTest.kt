package com.OrLove.peoplemanager.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.OrLove.peoplemanager.data.NoIdentityFoundException
import com.OrLove.peoplemanager.data.local.dao.IdentitiesDao
import com.OrLove.peoplemanager.data.local.entity.IdentityEntity
import com.OrLove.peoplemanager.data.managers.notification.AppNotificationManager
import com.OrLove.peoplemanager.data.managers.recognition.RecognitionManager
import com.OrLove.peoplemanager.ui.models.CoreFaceFeatures
import com.OrLove.peoplemanager.ui.models.IdentifiedPerson
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class PeopleManagerRepositoryImplTest {
    private val mockContext = mockk<Context>()
    private val mockRecognitionManager = mockk<RecognitionManager>()
    private val mockAppNotificationManager = mockk<AppNotificationManager>(relaxed = true)
    private val mockIdentitiesDao = mockk<IdentitiesDao>(relaxed = true, relaxUnitFun = true)
    private val mockBitmap = mockk<Bitmap>()
    private val mockUri = mockk<Uri>()
    private val mockFile = mockk<File>()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PeopleManagerRepositoryImpl
    private val testName = "John"
    private val testSurname = "Doe"
    private val testPosition = "Developer"
    private val testCoreFaceFeatures = mockk<CoreFaceFeatures>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = PeopleManagerRepositoryImpl(
            context = mockContext,
            dispatcher = testDispatcher,
            recognitionManager = mockRecognitionManager,
            appNotificationManager = mockAppNotificationManager,
            identitiesDao = mockIdentitiesDao
        )
        every { mockContext.cacheDir } returns mockFile
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkConstructor(File::class)
    }

    @Test
    fun `saveIdentifiedPerson should return false when face features extraction fails`() = runTest {
        coEvery { mockRecognitionManager.extractCoreFeatures(uri = mockUri) } returns null
        every { mockAppNotificationManager.showCantIdentifyFaceNotification() } just Runs
        val result = repository.saveIdentifiedPerson(testName, testSurname, testPosition, mockUri)
        assertFalse(result)
        verify { mockAppNotificationManager.showCantIdentifyFaceNotification() }
        coVerify(exactly = 0) { mockIdentitiesDao.insert(any<IdentityEntity>()) }
    }

    @Test
    fun `returns false and shows notification when face not recognized`() = runTest {
        val uri = Uri.parse("content://test/photo.jpg")

        coEvery { mockRecognitionManager.extractCoreFeatures(uri) } returns null

        val result = repository.saveIdentifiedPerson(
            name = "John",
            surname = "Doe",
            position = "Engineer",
            photoUri = uri
        )

        advanceUntilIdle()

        assertFalse(result)
        coVerify { mockAppNotificationManager.showCantIdentifyFaceNotification() }
        coVerify(exactly = 0) { mockIdentitiesDao.insert(any<IdentityEntity>()) }
    }

    @Test
    fun `identifyAndGetPersonByPhoto with bitmap should call saveBitmapTemporarilyAndReturnUri and delegate to uri method`() = runTest {
        val expectedPerson = mockk<IdentifiedPerson>()
        val repository = spyk(this@PeopleManagerRepositoryImplTest.repository)
        coEvery {
            repository.saveBitmapTemporarilyAndReturnUri(mockBitmap, true)
        } returns mockUri
        coEvery {
            repository.identifyAndGetPersonByPhoto(mockUri)
        } returns expectedPerson
        val result = repository.identifyAndGetPersonByPhoto(
            bitmap = mockBitmap,
            isMadeFromBackCamera = true
        )
        assertEquals(expectedPerson, result)
        coVerify {
            repository.saveBitmapTemporarilyAndReturnUri(mockBitmap, true)
            repository.identifyAndGetPersonByPhoto(mockUri)
        }
    }

    @Test
    fun `identifyAndGetPersonByPhoto with uri should return null and show notification when feature extraction fails`() = runTest {
        coEvery { mockRecognitionManager.extractCoreFeatures(uri = mockUri) } returns null
        every { mockAppNotificationManager.showCantIdentifyFaceNotification() } just Runs
        val result = repository.identifyAndGetPersonByPhoto(mockUri)
        assertNull(result)
        verify { mockAppNotificationManager.showCantIdentifyFaceNotification() }
        coVerify(exactly = 0) { mockIdentitiesDao.getAll() }
    }

    @Test
    fun `identifyAndGetPersonByPhoto with uri should handle empty database`() = runTest {
        coEvery { mockRecognitionManager.extractCoreFeatures(uri = mockUri) } returns testCoreFaceFeatures
        coEvery { mockIdentitiesDao.getAll() } returns emptyList()
        var exception: Exception? = null
        try {
            repository.identifyAndGetPersonByPhoto(mockUri)
        } catch (e: Exception) {
            exception = e
        }
        assertTrue(exception is NoIdentityFoundException)
    }
}