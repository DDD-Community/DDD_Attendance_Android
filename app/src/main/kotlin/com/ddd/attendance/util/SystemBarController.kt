package com.ddd.attendance.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.ddd.attendance.ScreenName

data class SystemBarColors(
    val statusBarColor: Color = Color.White,
    val navigationBarColor: Color = Color.White,
    val isLightStatusBars: Boolean = true,
    val isLightNavigationBars: Boolean = true
)

private fun getSystemBarColors(screenName: ScreenName): SystemBarColors {
    return when (screenName) {
        ScreenName.SPLASH -> SystemBarColors(
            statusBarColor = Color.Black,
            navigationBarColor = Color.Black,
            isLightStatusBars = false,
            isLightNavigationBars = false
        )
        ScreenName.LOGIN -> SystemBarColors(
            statusBarColor = Color.White,
            navigationBarColor = Color.White,
            isLightStatusBars = true,
            isLightNavigationBars = true
        )
        ScreenName.HOME -> SystemBarColors(
            statusBarColor = Color.White,
            navigationBarColor = Color.White,
            isLightStatusBars = true,
            isLightNavigationBars = true
        )
        ScreenName.ON_BOARDING -> SystemBarColors(
            statusBarColor = Color.White,
            navigationBarColor = Color.White,
            isLightStatusBars = true,
            isLightNavigationBars = true
        )
    }
}

@Composable
fun SystemBarController(
    screenName: ScreenName
) {
    val view = LocalView.current
    val colors = getSystemBarColors(screenName)
    
    SideEffect {
        try {
            val activity = view.context as Activity
            val window = activity.window
            val insetsController = WindowCompat.getInsetsController(window, view)
            
            println("SystemBarController: Setting colors for ${screenName.name} - status: ${colors.statusBarColor}, nav: ${colors.navigationBarColor}")
            
            // 시스템바 색상 설정
            window.statusBarColor = colors.statusBarColor.toArgb()
            window.navigationBarColor = colors.navigationBarColor.toArgb()
            
            // 아이콘 색상 설정
            insetsController.isAppearanceLightStatusBars = colors.isLightStatusBars
            insetsController.isAppearanceLightNavigationBars = colors.isLightNavigationBars
            
            println("SystemBarController: Colors set successfully for ${screenName.name}")
        } catch (e: Exception) {
            println("SystemBarController error: ${e.message}")
            e.printStackTrace()
        }
    }
}