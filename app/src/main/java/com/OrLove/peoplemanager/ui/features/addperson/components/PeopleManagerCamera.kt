package com.OrLove.peoplemanager.ui.features.addperson.components

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
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
import androidx.compose.ui.res.stringResource
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.utils.components.CameraPreview
import com.OrLove.peoplemanager.utils.rotate
import java.util.concurrent.Executors

@Composable
fun PeopleManagerCamera(
    makePhoto: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
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
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        } else {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        }
                },
                content = { Text(text = stringResource(R.string.change_camera)) },
            )
            Button(
                onClick = {
                    controller.takePicture(
                        Executors.newSingleThreadExecutor(),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                super.onCaptureSuccess(image)
                                image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
                                    .let { makePhoto(it) }
                                image.close()
                            }

                            override fun onError(exception: ImageCaptureException) {
                                super.onError(exception)
                                Log.d("WaybillCamera", "Couldn't save bitmap.")
                            }
                        }
                    )
                },
                content = { Text(text = stringResource(R.string.take_photo)) },
            )
        }
    }

}