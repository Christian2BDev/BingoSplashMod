plugins {
    java
    `java-library`
}


repositories {
    mavenCentral()
    // Add repositories to retrieve artifacts from in here.
    maven(url = "https://repo.essential.gg/repository/maven-public")
}

dependencies{
    api("org.java-websocket:Java-WebSocket:1.5.4")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    api("gg.essential:vigilance:${project.property("vigilance_version")}"){
        exclude(group = "gg.essential", module = "elementa")
    }
    api("gg.essential:elementa:${project.property("elementa_version")}")
}

java {
    withSourcesJar()
}