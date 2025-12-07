package com.ddd.attendance

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.support.delegates.DependencyHandlerDelegate
import kotlin.apply
import kotlin.text.toInt

/**
 * Android 모듈의 Kotlin/Android 기본 설정
 */
internal fun Project.configureKotlinAndroid() {
    androidExtension.apply {
        compileSdk = findVersion("projectCompileSdk").toInt()
        defaultConfig.minSdk = findVersion("projectMinSdk").toInt()

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }

        testOptions {
            unitTests.isIncludeAndroidResources = true
        }

        buildFeatures {
            buildConfig = true
        }
    }
}

/**
 * 순수 Kotlin/JVM 모듈의 Java 호환 버전 설정
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

/**
 * Android Library 모듈의 Build Features 설정
 */
fun Project.configureBuildFeatures() {
    extensions.configure<LibraryExtension> {
        buildFeatures.buildConfig = true
    }
}