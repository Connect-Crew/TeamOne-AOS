pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
            setUrl("https://devrepo.kakao.com/nexus/content/groups/public/")
        }
    }
}

rootProject.name = "TeamOne"
include(":app")
include(":domain")
include(":data")
include(":presentation")
