import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("feature.member.main")

dependencies {
    implementation(projects.feature.designsystem)
    implementation(projects.feature.core)
}