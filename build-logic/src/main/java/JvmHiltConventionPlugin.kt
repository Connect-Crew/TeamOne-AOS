import com.connectcrew.teamone.convention.implementation
import com.connectcrew.teamone.convention.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal class JvmHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            dependencies {
                implementation(libs.findLibrary("hilt.core").get())
                ksp(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}