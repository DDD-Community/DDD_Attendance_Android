package com.ddd.attendance.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    OnBoardingScreenContent()
}

@Composable
internal fun OnBoardingScreenContent() {

}
