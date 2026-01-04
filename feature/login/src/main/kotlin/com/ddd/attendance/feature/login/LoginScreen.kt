package com.ddd.attendance.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ddd.attendance.domain.model.LoginType
import com.ddd.attendance.domain.model.NavigationDestination

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val navigationDestination = viewModel.navigationDestination.collectAsStateWithLifecycle(null)
    
    LaunchedEffect(navigationDestination.value) {
        navigationDestination.value?.let { destination ->
            when (destination) {
                is NavigationDestination.Login -> {
                    // 이미 로그인 화면에 있으므로 아무것도 하지 않음
                }
                is NavigationDestination.OnBoarding -> {
                    navController.navigate("ON_BOARDING") {
                        popUpTo("LOGIN") { inclusive = false }
                    }
                }
                is NavigationDestination.Member -> {
                    navController.navigate("MEMBER_MAIN") {
                        popUpTo("LOGIN") { inclusive = true }
                    }
                }
                is NavigationDestination.Manager -> {
                    // TODO: Manager 화면이 만들어지면 해당 route로 변경
                    navController.navigate("MEMBER_MAIN") {
                        popUpTo("LOGIN") { inclusive = true }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo",
            modifier = Modifier.align(Alignment.Center)
        )
        
        Button(
            onClick = { viewModel.login(LoginType.GOOGLE) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_login),
                    contentDescription = "Google Login",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google로 시작하기",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}