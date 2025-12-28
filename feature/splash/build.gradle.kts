import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
    alias(libs.plugins.attendance.android.coil)
}

setNamespace("feature.splash")

dependencies {
    implementation(projects.domain)
}