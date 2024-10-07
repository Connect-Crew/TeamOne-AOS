import com.connectcrew.teamone.convention.implementation
import com.connectcrew.teamone.convention.ksp
import com.connectcrew.teamone.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class JvmHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            dependencies {
                implementation(libs.hilt.core)
                ksp(libs.hilt.compiler)
            }
        }
    }
}