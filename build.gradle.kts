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

val osName = System.getProperty("os.name").lowercase()
val osArch = System.getProperty("os.arch").lowercase()

val currentPlatform = when {
    osName.contains("win") -> "win"
    osName.contains("nix") || osName.contains("nux") || osName.contains("aix") -> "linux"
    osName.contains("mac") -> if (osArch == "aarch64" || osArch.contains("arm")) "mac-aarch64" else "mac"
    else -> "linux"
}
// ------------------------------------------------------------------

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    for (module in javaFXModules) {
        implementation("org.openjfx:javafx-$module:$javaFxVersion:$currentPlatform")
    }

    testImplementation(platform("org.junit:junit-bom:6.0.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
