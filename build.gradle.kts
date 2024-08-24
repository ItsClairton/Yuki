plugins {
    id("java")
}

allprojects {
    group = "dev.clairton.yuki"
    version = "2.3.67"
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