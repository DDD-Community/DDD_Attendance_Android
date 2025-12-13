import com.ddd.attendance.setNamespace

plugins {
    id("attendance.android.library")
    id("attendance.android.hilt")
    id("attendance.android.googlelogin")
}

setNamespace("data.google")

dependencies {
    implementation(projects.domain)
    implementation(projects.data)
}