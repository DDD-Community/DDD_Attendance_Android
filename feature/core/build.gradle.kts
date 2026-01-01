import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.library)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("feature.core")

dependencies {
    implementation(projects.feature.designsystem)
    
    // QR Code generation and scanning
    implementation("com.google.zxing:core:3.5.2")
    
    // Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}