package com.ddd.attendance

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import kotlin.apply

/**
 * BOM으로 버전 통일 후 실제 Compose 라이브러리 묶음(bundle)을 추가하는 구성
 **/
internal fun Project.configureComposeAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        dependencies {
            val bom = findLibrary("androidx-compose-bom")
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            "implementation"(findBundle("compose"))
        }

        // Compose 디버깅 시 UI 요소가 어느 Composable 소스에서 왔는지 추적할 수 있게 메타정보를 포함하도록 설정
        extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
            includeSourceInformation.set(true)
        }
    }
}