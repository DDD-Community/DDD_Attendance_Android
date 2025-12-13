import com.ddd.attendance.setNamespace

plugins {
    id("attendance.android.library")
    id("attendance.android.hilt")
    id("attendance.android.retrofit")
    // todo find reason for cannot use alias
    // alias(libs.plugins.attendance.android.library)
    // alias(libs.plugins.attendance.android.hilt)
    // alias(libs.plugins.attendance.android.retrofit)
}

setNamespace("data.api")

dependencies {
    implementation(projects.data)
}