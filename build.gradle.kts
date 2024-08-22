plugins {
    id("java")
}

allprojects {
    version = "2.3.67"
    description = "Libre simulation anticheat designed for 1.20 with 1.8-1.20 support, powered by PacketEvents 2.0."
}

subprojects {
    afterEvaluate {
        java.sourceCompatibility = JavaVersion.VERSION_17

        repositories {
            maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
            mavenCentral()
        }

        dependencies {
            compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
            compileOnly("org.jetbrains:annotations:24.1.0")
        }

    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

}