package com.ddd.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ddd.attendance.feature.admin.main.AdminScreen
import com.ddd.attendance.feature.admin.profile.AdminProfileScreen
import com.ddd.attendance.feature.home.HomeScreen
import com.ddd.attendance.feature.login.LoginScreen
import com.ddd.attendance.feature.member.attendance.MemberAttendanceScreen
import com.ddd.attendance.feature.member.main.MemberMainScreen
import com.ddd.attendance.feature.member.profile.MemberProfileScreen
import com.ddd.attendance.feature.onboarding.OnBoardingScreen
import com.ddd.attendance.feature.splash.SplashScreen
import com.ddd.attendance.ui.theme.DddBackgroundDark

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onRestart: () -> Unit

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

            composable(
                route = "${ScreenName.ON_BOARDING.name}?isProfileEdit={isProfileEdit}",
                arguments = listOf(
                    navArgument("isProfileEdit") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->

                val isProfileEdit = backStackEntry.arguments?.getBoolean("isProfileEdit") ?: false

                OnBoardingScreen(
                    navController = navigator.navController,
                    isProfileEdit = isProfileEdit,
                    onRestart = onRestart
                )
            }

            composable(route = ScreenName.HOME.name) {
                HomeScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.LOGIN.name) {
                LoginScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.ADMIN_MAIN.name) {
                AdminScreen(
                    navController = navigator.navController)
            }

            composable(route = ScreenName.ADMIN_PROFILE.name) {
                AdminProfileScreen(
                    navController = navigator.navController
                )
            }

            composable(route = ScreenName.MEMBER_MAIN.name) {
                MemberMainScreen(
                    onNavigateToProfile = {
                        navigator.navController.navigate(ScreenName.MEMBER_PROFILE.name)
                    },
                    onNavigateToAttendance = {
                        navigator.navController.navigate(ScreenName.MEMBER_ATTENDANCE.name)
                    },
                    onLogout = {
                        navigator.navController.navigate("LOGIN") {
                            popUpTo(navigator.navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = ScreenName.MEMBER_PROFILE.name) {
                MemberProfileScreen(navController = navigator.navController)
            }

            composable(route = ScreenName.MEMBER_ATTENDANCE.name) {
                MemberAttendanceScreen(navController = navigator.navController)
            }
        }
    }
}
