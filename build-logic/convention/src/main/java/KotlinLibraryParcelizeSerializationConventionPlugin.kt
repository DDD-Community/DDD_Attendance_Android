import com.ddd.attendance.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinLibraryParcelizeSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.plugin.parcelize")
            apply("kotlinx-serialization")
        }

        dependencies {
            "implementation"(findLibrary("kotlinx-serialization-json"))
        }
    }
}