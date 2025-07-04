plugins {
    java
    `java-library`
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("maven-publish")

}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

loom {

    splitEnvironmentSourceSets()

    mods {
        create("bingosplash") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])


        }

    }

}

repositories {
    // Add repositories to retrieve artifacts from in here.
    maven(url = "https://repo.essential.gg/repository/maven-public")
}

dependencies {

    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation (include("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")!!)
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("gg.essential:vigilance:${project.property("vigilance_version")}"){
        exclude(group = "gg.essential", module = "elementa")
        include("gg.essential:vigilance:${project.property("vigilance_version")}")
    }
    modImplementation(include("gg.essential:elementa:${project.property("elementa_version")}")!!)
    modImplementation(include("gg.essential:universalcraft-1.21.5-fabric:${project.property("uc_version")}")!!)
    implementation (project(":common"))
    include(project(":common"))
    //modImplementation(include(project(":common"))!!)
    modImplementation(include("org.java-websocket:Java-WebSocket:1.5.4")!!)
    include("org.java-websocket:Java-WebSocket:1.5.4")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version")
        )
    }
}

val targetJavaVersion = 21

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}




publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}