package com.ddd.attendance

import org.gradle.api.Project

/**
 * Android 모듈의 namespace를 "com.ddd.attendance.{name}" 형태로 설정하는 확장 함수
 **/
fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.ddd.attendance.$name"
    }
}