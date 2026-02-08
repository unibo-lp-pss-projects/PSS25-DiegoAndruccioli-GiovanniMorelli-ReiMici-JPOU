plugins {
    java
    application
    jacoco
    id("com.gradleup.shadow") version "9.3.1"
    id("org.danilopianini.gradle-java-qa") version "1.165.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

val javaFxVersion = "23.0.2"
val javaFXModules = listOf("base", "controls", "fxml", "swing", "graphics")
// Platforms to support: standard ones + mac-aarch64 (M1/M2/M3)
val supportedPlatforms = listOf("linux", "mac", "win", "mac-aarch64")

val crossPlatformLibs by configurations.creating

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    // Rilevamento automatico del sistema corrente per il RUN locale
    val osName = System.getProperty("os.name").lowercase()
    val osArch = System.getProperty("os.arch").lowercase()
    val currentPlatform = when {
        osName.contains("win") -> "win"
        osName.contains("nux") || osName.contains("nix") || osName.contains("aix") -> "linux"
        osName.contains("mac") && (osArch == "aarch64" || osArch.contains("arm")) -> "mac-aarch64"
        osName.contains("mac") -> "mac"
        else -> "linux" // Fallback
    }

    for (module in javaFXModules) {
        implementation("org.openjfx:javafx-$module:$javaFxVersion:$currentPlatform")
    }

    val allPlatforms = listOf("linux", "mac", "win", "mac-aarch64")
    for (platform in allPlatforms) {
        if (platform != currentPlatform) {
            for (module in javaFXModules) {
                crossPlatformLibs("org.openjfx:javafx-$module:$javaFxVersion:$platform")
            }
        }
    }

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.shadowJar {
    configurations = listOf(project.configurations.runtimeClasspath.get(), crossPlatformLibs)
}
tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

application {
    mainClass.set("it.unibo.jpou.Launcher")
}