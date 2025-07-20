package com.example.bharatcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import com.example.bharatcheck.presentations.imageVerificationScreen
import com.example.bharatcheck.presentations.linkVerificationScreen
import com.example.bharatcheck.presentations.textVerificationScreen
import com.example.bharatcheck.ui.theme.BharatCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
        )
        setContent {
            BharatCheckTheme {
                MainApp()
            }
        }
    }
}
