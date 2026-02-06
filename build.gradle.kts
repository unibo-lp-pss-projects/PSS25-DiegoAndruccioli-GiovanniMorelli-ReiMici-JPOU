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
val supportedPlatforms = listOf("linux", "mac", "win")

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    // Add JavaFX dependencies for all supported platforms
    for (platform in supportedPlatforms) {
        for (module in javaFXModules) {
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
        }
    }
    // Explicitly add Mac Aarch64 support (Apple Silicon)
    for (module in javaFXModules) {
        implementation("org.openjfx:javafx-$module:$javaFxVersion:mac-aarch64")
    }

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.code.gson:gson:2.10.1")
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