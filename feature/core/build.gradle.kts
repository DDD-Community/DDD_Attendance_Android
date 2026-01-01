import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.compose)
    alias(libs.plugins.attendance.coroutine)
    alias(libs.plugins.attendance.android.qrcode)
}

setNamespace("feature.core")

dependencies {
    implementation(projects.feature.designsystem)
}