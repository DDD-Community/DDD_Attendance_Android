import com.ddd.attendance.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidGoogleLoginConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(findLibrary("google.credentials"))
                "implementation"(findLibrary("google.credentials.play.services.auth"))
                "implementation"(findLibrary("google.identity.googleid"))
            }
        }
    }
}