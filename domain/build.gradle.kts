plugins {
    alias(libs.plugins.attendance.jvm.library)
    alias(libs.plugins.attendance.coroutine)
}

dependencies {
    implementation(libs.inject)
}