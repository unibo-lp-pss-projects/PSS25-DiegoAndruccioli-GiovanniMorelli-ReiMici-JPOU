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

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    val javaFxVersion = "23.0.2"
    val javaFxPlatform = "mac-aarch64"

    implementation("org.openjfx:javafx-base:$javaFxVersion:$javaFxPlatform")
    implementation("org.openjfx:javafx-controls:$javaFxVersion:$javaFxPlatform")
    implementation("org.openjfx:javafx-fxml:$javaFxVersion:$javaFxPlatform")
    implementation("org.openjfx:javafx-swing:$javaFxVersion:$javaFxPlatform")
    implementation("org.openjfx:javafx-graphics:$javaFxVersion:$javaFxPlatform")

    implementation("org.openjfx:javafx-controls:$javaFxVersion")

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
