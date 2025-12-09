import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.application)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("app")

dependencies {
    implementation(projects.feature.home)
}