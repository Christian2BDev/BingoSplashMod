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
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("gg.essential:vigilance:${project.property("vigilance_version")}"){
        exclude(group = "gg.essential", module = "elementa")
    }
    implementation("gg.essential:elementa:${project.property("elementa_version")}")
}

java {
    withSourcesJar()
}