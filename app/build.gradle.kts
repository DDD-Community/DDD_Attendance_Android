import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.application)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("app")

dependencies {
    implementation(projects.data)
    implementation(projects.data.api)
    implementation(projects.domain)
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.splash)
}