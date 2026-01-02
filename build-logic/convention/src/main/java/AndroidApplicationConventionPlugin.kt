import com.android.build.api.dsl.ApplicationExtension
import com.ddd.attendance.ExtensionType
import com.ddd.attendance.configureBuildTypes
import com.ddd.attendance.configureKotlinAndroid
import com.ddd.attendance.findVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = findVersion("projectApplicationId")
                    versionCode = findVersion("projectVersionCode").toInt()
                    versionName = findVersion("projectVersionName")
                    targetSdk = findVersion("projectTargetSdk").toInt()
                }

                buildFeatures {
                    buildConfig = true
                }

                configureKotlinAndroid()
                configureBuildTypes(commonExtension = this, extensionType = ExtensionType.APPLICATION)
            }
        }
    }
}

