import com.ddd.attendance.setNamespace

plugins {
    alias(libs.plugins.attendance.android.application)
    alias(libs.plugins.attendance.android.hilt)
    alias(libs.plugins.attendance.android.compose) //임시
}

setNamespace("app")