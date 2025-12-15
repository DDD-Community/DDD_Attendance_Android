import com.ddd.attendance.setNamespace

plugins {
    id("attendance.android.library")
    id("attendance.android.hilt")
    id("attendance.android.retrofit")
}

setNamespace("data.api")

dependencies {
    implementation(projects.data)
}