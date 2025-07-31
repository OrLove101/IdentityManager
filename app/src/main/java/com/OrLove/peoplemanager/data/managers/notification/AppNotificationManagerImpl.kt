package com.OrLove.peoplemanager.data.managers.notification

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

class AppNotificationManagerImpl(
    @ApplicationContext private val context: Context
) : AppNotificationManager {
    override fun showCantIdentifyFaceNotification() {
        // TODO
    }
}