package com.romsalva.recipeschallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.romsalva.recipeschallenge.ui.composable.screen.ChallengeApp
import com.romsalva.recipeschallenge.ui.theme.ChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeTheme {
                ChallengeApp()
            }
        }
    }
}