package com.ddd.attendance

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Android 모듈에서 필요한 코루틴 의존성 추가 + 공통 코루틴 설정 적용
 **/
internal fun Project.configureCoroutineAndroid() {
    configureCoroutineKotlin()
    dependencies {
        "implementation"(findLibrary("coroutines.android"))
    }
}

/**
 * 모든 Kotlin 모듈에서 공통으로 사용하는 코루틴 의존성 설정
 **/
internal fun Project.configureCoroutineKotlin() {
    dependencies {
        "implementation"(findLibrary("coroutines.core"))
        "testImplementation"(findLibrary("coroutines.test"))
    }
}