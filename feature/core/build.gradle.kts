import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("feature.core")

dependencies {
    implementation(projects.feature.designsystem)
}