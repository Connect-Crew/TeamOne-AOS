import com.connectcrew.teamone.convention.implementation
import com.connectcrew.teamone.convention.ksp
import com.connectcrew.teamone.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidHiltConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                implementation(libs.hilt.android)
                ksp(libs.hilt.android.compiler)
            }
        }
    }
}
