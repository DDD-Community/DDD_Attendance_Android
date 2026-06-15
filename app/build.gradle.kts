import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.application)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose)
}

setNamespace("app")

dependencies {
    implementation(libs.material)
    implementation(projects.data)
    implementation(projects.data.api)
    implementation(projects.data.google)
    implementation(projects.data.datastore)
    implementation(projects.domain)
    implementation(projects.feature.core)
    implementation(projects.feature.home)
    implementation(projects.feature.login)
    implementation(projects.feature.splash)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.admin.main)
    implementation(projects.feature.admin.attendance)
    implementation(projects.feature.admin.schedule)
    implementation(projects.feature.admin.profile)
    implementation(projects.feature.member.attendance)
    implementation(projects.feature.member.main)
    implementation(projects.feature.member.profile)
    implementation(projects.feature.member.vote)
}