import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.LibraryExtension
import com.ddd.attendance.configureComposeAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            plugins.withId("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    configureComposeAndroid(commonExtension = this)
                }
            }

            plugins.withId("com.android.library") {
                extensions.configure<LibraryExtension> {
                    configureComposeAndroid(commonExtension = this)
                }
            }
        }
    }
}

