enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "DDDAtendanceAndroid"

include(":app")
include(":data")
include(":data:api")
include(":data:google")
include(":domain")
include(":feature:designsystem")
include(":feature:home")
include(":feature:login")
include(":feature:member:attendance")
include(":feature:member:main")
include(":feature:member:profile")
include(":feature:onboarding")
include(":feature:splash")
include(":feature:designsystem")
include(":feature:admin")
include(":feature:core")
