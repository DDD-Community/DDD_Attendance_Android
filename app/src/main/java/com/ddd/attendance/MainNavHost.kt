package com.ddd.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ddd.attendance.feature.admin.AdminScreen
import com.ddd.attendance.feature.home.HomeScreen
import com.ddd.attendance.feature.login.LoginScreen
import com.ddd.attendance.feature.onboarding.OnBoardingScreen
import com.ddd.attendance.feature.splash.SplashScreen
import com.ddd.attendance.ui.theme.DddBackgroundDark

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
            .background(DddBackgroundDark)
            .fillMaxSize()
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            composable(route = ScreenName.SPLASH.name) {
                SplashScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.ON_BOARDING.name) {
                OnBoardingScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.HOME.name) {
                HomeScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.LOGIN.name) {
                LoginScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.ADMIN.name) {
                AdminScreen(
                    navController = navigator.navController)
            }
        }
    }
}
