pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
}
rootProject.name = "RootProject"
include("common")
include("1.21.5-fabric")
include("1.8.9-forge")

