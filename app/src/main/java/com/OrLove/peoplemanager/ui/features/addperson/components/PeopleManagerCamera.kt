package com.OrLove.peoplemanager.ui.features.addperson.components

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.OrLove.peoplemanager.utils.components.CameraPreview
import java.util.concurrent.Executors

@Composable
fun PeopleManagerCamera(
    makePhoto: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        Button(
            onClick = {
                controller.takePicture(
                    Executors.newSingleThreadExecutor(),
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            super.onCaptureSuccess(image)
                            makePhoto(image.toBitmap())
                            image.close()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            super.onError(exception)
                            Log.d("WaybillCamera","Couldn't save bitmap.")
                        }
                    }
                )
            },
            content = { Text(text = "Take Photo") },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}