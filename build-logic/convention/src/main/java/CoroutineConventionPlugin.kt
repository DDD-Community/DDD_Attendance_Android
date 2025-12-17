import com.ddd.attendance.configureCoroutineKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class CoroutineConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureCoroutineKotlin()
        }
    }
}