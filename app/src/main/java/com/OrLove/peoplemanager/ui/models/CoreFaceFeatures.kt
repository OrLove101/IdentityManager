package com.OrLove.peoplemanager.ui.models

import com.OrLove.peoplemanager.data.local.entity.IdentityEntity

data class CoreFaceFeatures(
    val eyeDistanceRatio: Float,
    val faceAspectRatio: Float,
    val noseToEyesRatio: Float,
    val eyeToMouthRatio: Float
)

fun CoreFaceFeatures.toEntity() = IdentityEntity.FaceFeaturesEntity(
    eyeDistanceRatio = eyeDistanceRatio,
    faceAspectRatio = faceAspectRatio,
    noseToEyesRatio = noseToEyesRatio,
    eyeToMouthRatio = eyeToMouthRatio
)