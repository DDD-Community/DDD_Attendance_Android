import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("feature.admin.profile")

dependencies {
    implementation(projects.feature.designsystem)
    api(projects.feature.core)
}