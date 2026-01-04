package com.ddd.attendance.domain.model

sealed interface NavigationDestination {
    object OnBoarding : NavigationDestination
    object Login : NavigationDestination
    object Member : NavigationDestination
    object Manager : NavigationDestination
}