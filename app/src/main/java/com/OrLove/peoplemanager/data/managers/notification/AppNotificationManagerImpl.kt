package com.OrLove.peoplemanager.data.managers.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.OrLove.peoplemanager.R
import com.OrLove.peoplemanager.utils.AppNotification
import com.OrLove.peoplemanager.utils.AppNotificationChannel
import dagger.hilt.android.qualifiers.ApplicationContext

class AppNotificationManagerImpl(
    @ApplicationContext private val context: Context
) : AppNotificationManager {

    init {
        AppNotificationChannel.entries.forEach { channel ->
            NotificationManagerCompat.from(context).createNotificationChannel(
                NotificationChannelCompat.Builder(
                    channel.id,
                    channel.importance
                )
                    .setName(channel.channelName)
                    .setLightsEnabled(true)
                    .setVibrationPattern(longArrayOf(2000L, 300L))
                    .setVibrationEnabled(true)
                    .build()
            )
        }
    }

    override fun showCantIdentifyFaceNotification() {
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(
                AppNotification.IDENTIFICATION_FAILED.id,
                NotificationCompat
                    .Builder(
                        context,
                        AppNotificationChannel.IDENTIFICATION.id
                    )
                    .setContentTitle(
                        context.getString(R.string.cant_identify_face_from_photo)
                    )
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_identity)
                    .setStyle(
                        NotificationCompat.BigTextStyle().bigText(
                            context.getString(R.string.retake_photo)
                        )
                    )
                    .setAutoCancel(true)
                    .build()
            )
        }
    }
}