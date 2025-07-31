package com.OrLove.peoplemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.OrLove.peoplemanager.ui.features.main.navigation.RootNav
import com.OrLove.peoplemanager.ui.theme.PeopleManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeopleManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RootNav(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}