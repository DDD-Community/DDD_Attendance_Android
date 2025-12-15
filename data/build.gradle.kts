import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.kotlin.library.parcelize.serialization)
}

setNamespace("data")

dependencies {
    implementation(projects.domain)
}