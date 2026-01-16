import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.kotlin.library.parcelize.serialization)
    alias(libs.plugins.attendance.android.retrofit)
}

setNamespace("data")

dependencies {
    implementation(projects.domain)
    implementation(projects.data.datastore)
}