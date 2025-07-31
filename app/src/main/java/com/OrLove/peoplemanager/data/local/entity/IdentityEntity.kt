package com.OrLove.peoplemanager.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    tableName = "Identity",
    primaryKeys = ["name", "surname"]
)
data class IdentityEntity(
    val name: String,
    val surname: String,
    val position: String,
    @Embedded
    val faceFeatures: FaceFeaturesEntity
) {
    data class FaceFeaturesEntity(
        val eyeDistanceRatio: Float,
        val faceAspectRatio: Float,
        val noseToEyesRatio: Float,
        val eyeToMouthRatio: Float
    )
}