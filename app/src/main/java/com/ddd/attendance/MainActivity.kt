package com.ddd.attendance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.ddd.attendance.ui.theme.DDDAtendanceAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Black.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Black.toArgb()
            )
        )
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            DDDAtendanceAndroidTheme {
                MainScreen()
            }
        }
    }
}