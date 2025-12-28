package com.ddd.attendance.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.ddd.attendance.domain.model.NavigationDestination

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val navigationDestination = viewModel.navigationDestination.collectAsStateWithLifecycle(null)
    
    LaunchedEffect(navigationDestination.value) {
        navigationDestination.value?.let { destination ->
            when (destination) {
                is NavigationDestination.Login -> {
                    navController.navigate("LOGIN") {
                        popUpTo("SPLASH") { inclusive = true }
                    }
                }
                is NavigationDestination.OnBoarding -> {
                    navController.navigate("ON_BOARDING") {
                        popUpTo("SPLASH") { inclusive = true }
                    }
                }
                is NavigationDestination.Member -> {
                    navController.navigate("MEMBER_MAIN") {
                        popUpTo("SPLASH") { inclusive = true }
                    }
                }
                is NavigationDestination.Manager -> {
                    // TODO: Manager 화면이 만들어지면 해당 route로 변경
                    navController.navigate("MEMBER_MAIN") {
                        popUpTo("SPLASH") { inclusive = true }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.raw.ddd_splash,
            contentDescription = "DDD Splash Animation",
            modifier = Modifier.size(200.dp)
        )
    }
}