package com.ddd.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ddd.attendance.feature.home.HomeScreen
import com.ddd.attendance.feature.login.LoginScreen
import com.ddd.attendance.feature.splash.SplashScreen
import com.ddd.attendance.util.SystemBarController

@Composable
internal fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(padding)
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            composable(route = ScreenName.SPLASH.name) {
                SystemBarController(screenName = ScreenName.SPLASH)
                SplashScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.ON_BOARDING.name) {
                SystemBarController(screenName = ScreenName.ON_BOARDING)
            }

            composable(route = ScreenName.HOME.name) {
                SystemBarController(screenName = ScreenName.HOME)
                HomeScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.LOGIN.name) {
                SystemBarController(screenName = ScreenName.LOGIN)
                LoginScreen(navController = navigator.navController)
            }
        }
    }
}
