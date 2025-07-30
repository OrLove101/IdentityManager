package com.OrLove.peoplemanager.ui.features.recognise_person.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun RecognisePersonScreen(modifier: Modifier = Modifier) {
    Text("Recognise Person Screen")
    // make photo from camera and compare with db

    // show info from db by photo or error message

    // show notification on identification error
    // tests
    // handle errors when face not found or photo is not appropriate
}

// use ml kit face detection, camerax, glide or picasso