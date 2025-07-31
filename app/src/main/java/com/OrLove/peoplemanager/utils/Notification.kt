package com.OrLove.peoplemanager.utils

import android.app.NotificationManager

enum class AppNotificationChannel(
    val id: String,
    val channelName: String,
    val importance: Int
) {
    IDENTIFICATION(
        id = "identification",
        channelName = "Identification Notifications",
        importance = NotificationManager.IMPORTANCE_HIGH
    )
}

enum class AppNotification(
    val id: Int
) {
    IDENTIFICATION_FAILED(id = 1)
}