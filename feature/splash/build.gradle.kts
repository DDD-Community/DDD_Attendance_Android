import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
    alias(libs.plugins.attendance.android.coil)
}

dependencies {
    implementation(projects.feature.core)
    implementation(projects.domain)
}

setNamespace("feature.splash")