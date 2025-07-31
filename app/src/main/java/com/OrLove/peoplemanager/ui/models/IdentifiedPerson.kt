package com.OrLove.peoplemanager.ui.models

import com.OrLove.peoplemanager.data.local.entity.IdentityEntity

data class IdentifiedPerson(
    val name: String = "",
    val surname: String = "",
    val position: String = ""
)

fun IdentityEntity.toUi() = IdentifiedPerson(
    name = name,
    surname = surname,
    position = position
)
