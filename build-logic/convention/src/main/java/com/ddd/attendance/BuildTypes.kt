package com.ddd.attendance

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.util.Properties

/**
 * 모듈 타입(Application/Library)에 맞춰 debug·release 빌드 타입을 설정하는 함수
 **/
internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*,*,*,*,*, *>,
    extensionType: ExtensionType
){
    commonExtension.run {
        val projectVersionName = findVersion("projectVersionName")
        when(extensionType){
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(projectVersionName)
                            isDebuggable = true
                        }
                        release {
                            configureReleaseBuildType(commonExtension, projectVersionName)
                            isDebuggable = false
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(projectVersionName)
                        }
                        release {
                            // 라이브러리 모듈에서는 minify 비활성화 (app 모듈에서만 적용)
                            isMinifyEnabled = false
                            buildConfigField("String", "APP_VERSION", "\"$projectVersionName\"")
                            consumerProguardFiles("consumer-rules.pro")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 여러 properties 파일(local/secrets 등)에서 특정 key 값을 찾아 반환하는 유틸 함수
 **/
private fun Project.getPropertyFromFiles(propertyName: String): String {
    val propertiesFiles = listOf("local.defaults.properties", "secrets.properties", "local.properties")
    val properties = Properties()

    propertiesFiles.forEach { fileName ->
        val file = File(rootDir, fileName)
        if (file.exists()) {
            file.inputStream().use { properties.load(it) }
        }
    }

    return properties.getProperty(propertyName) ?: throw kotlin.IllegalStateException("Property $propertyName not found in any properties file.")
}

/**
 * Debug 빌드에서 난독화 등 불필요한 최적화를 끄는 설정
 **/
private fun BuildType.configureDebugBuildType(projectVersionName: String){
    isMinifyEnabled = false
    buildConfigField("String", "APP_VERSION", "\"$projectVersionName\"")
}

/**
 * Release 빌드에서 난독화·최적화·Proguard 규칙을 적용하는 설정
 **/
private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    projectVersionName: String
){
    isMinifyEnabled = true
    buildConfigField("String", "APP_VERSION", "\"$projectVersionName\"")
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}