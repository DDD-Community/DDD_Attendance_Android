import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("feature.admin.main")

dependencies {
    implementation(projects.feature.admin.attendance)
    implementation(projects.feature.admin.schedule)
    implementation(projects.feature.admin.profile)
    implementation(projects.feature.designsystem)
    implementation(projects.domain)
    implementation(libs.androidx.compose.foundation.layout)
}