pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "BingoSplashMod"

include(
    "common",
    "1.21.5-fabric"
)