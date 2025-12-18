import com.android.build.api.dsl.LibraryExtension
import com.ddd.attendance.ExtensionType
import com.ddd.attendance.configureBuildTypes
import com.ddd.attendance.configureCoroutineAndroid
import com.ddd.attendance.configureKotlinAndroid
import com.ddd.attendance.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid()
                configureCoroutineAndroid()
                configureBuildTypes(commonExtension = this, extensionType = ExtensionType.LIBRARY)

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                testOptions {
                    unitTests.all { test ->
                        test.useJUnitPlatform()
                    }
                }
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                //앱 내부 동작을 Trace로 기록해 성능 병목을 분석하기 쉽게 만들어주는 KTX 라이브러리
                add("implementation", findLibrary("androidx.tracing.ktx"))

                // UI 상태 리스트 불변성을 위해 ImmutableList 라이브러리 사용
                add("implementation", findLibrary("kotlinx.immutable"))
            }
        }
    }
}
