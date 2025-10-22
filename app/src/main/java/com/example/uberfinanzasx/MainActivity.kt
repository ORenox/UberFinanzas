package com.example.uberfinanzasx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uberfinanzasx.ui.screen.MainScreen
import com.example.uberfinanzasx.ui.theme.UberFinanzasXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UberFinanzasXTheme {
                MainScreen()
            }
        }
    }
}
