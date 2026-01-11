package com.ddd.attendance.feature.splash

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.ddd.attendance.domain.model.NavigationDestination
import com.ddd.attendance.feature.core.permission.PermissionUtils

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navigateToLogin = viewModel.navigationDestination.collectAsStateWithLifecycle(null)

    // 카메라 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // 권한 허용/거부 여부에 관계없이 스플래시 시작
        viewModel.startSplashAfterPermission()
    }

    LaunchedEffect(navigateToLogin.value) {
        when (navigateToLogin.value) {
            NavigationDestination.Login -> {
                navController.navigate("LOGIN") {
                    popUpTo("SPLASH") { inclusive = true }
                }
            }

            NavigationDestination.Manager -> {
                navController.navigate("ADMIN_MAIN") {
                    popUpTo("SPLASH") { inclusive = true }
                }
            }
            NavigationDestination.Member -> {
                navController.navigate("MEMBER_MAIN") {
                    popUpTo("SPLASH") { inclusive = true }
                }
            }
            NavigationDestination.OnBoarding -> {
                navController.navigate("ON_BOARDING") {
                    popUpTo("SPLASH") { inclusive = true }
                }
            }
            null -> {}
        }
    }

    // 스플래시 화면이 시작되면 카메라 권한 요청
    LaunchedEffect(Unit) {
        when (PermissionUtils.getCameraPermissionState(context)) {
            is com.ddd.attendance.feature.core.permission.CameraPermissionState.Granted -> {
                // 이미 권한이 있으면 바로 스플래시 시작
                viewModel.startSplashAfterPermission()
            }

            else -> {
                // 권한 요청
                permissionLauncher.launch(PermissionUtils.CAMERA_PERMISSION)
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