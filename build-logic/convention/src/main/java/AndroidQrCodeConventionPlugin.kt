import com.ddd.attendance.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidQrCodeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(findLibrary("zxing-core"))
                
                // Camera dependencies for QR scanning
                "implementation"(findLibrary("androidx-camera-camera2"))
                "implementation"(findLibrary("androidx-camera-lifecycle"))
                "implementation"(findLibrary("androidx-camera-view"))
            }
        }
    }
}