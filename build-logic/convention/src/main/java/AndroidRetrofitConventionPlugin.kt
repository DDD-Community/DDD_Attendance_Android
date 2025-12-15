import com.ddd.attendance.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            
            dependencies {
                "implementation"(findLibrary("retrofit"))
                "implementation"(findLibrary("retrofit.converter.kotlinx.serialization"))
                "implementation"(findLibrary("okhttp"))
                "implementation"(findLibrary("okhttp.logging.interceptor"))
                "implementation"(findLibrary("kotlinx.serialization.json"))
            }
        }
    }
}