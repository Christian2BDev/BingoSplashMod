plugins {
    // e.g. java or kotlin plugin if needed for subprojects
    java
}


subprojects {
    // Common config for all subprojects
    repositories {
        mavenCentral()
    }
}