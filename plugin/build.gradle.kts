plugins {
    id("java")
    id("io.freefair.lombok") version "8.7.1"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "ac.grim.grimac"
version = "2.3.67"

repositories {
    maven("https://repo.viaversion.com") // ViaVersion
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://nexus.scarsz.me/content/repositories/releases") // Configuralize
    maven("https://repo.opencollab.dev/main/") // Floodgate
    maven("https://repo.codemc.io/repository/maven-snapshots/") // PacketEvents
}

dependencies {
    implementation(project(":api"))

    implementation("com.github.retrooper:packetevents-spigot:2.5.0-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("club.minnced:discord-webhooks:0.8.0") // Newer versions include kotlin-stdlib, which leads to incompatibility with plugins that use Kotlin
    implementation("it.unimi.dsi:fastutil:8.5.13")
    implementation("github.scarsz:configuralize:1.4.0")

    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    compileOnly("com.viaversion:viaversion-api:4.9.4-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.85.Final")
}


tasks.jar {
    enabled = false
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    if (gradle.startParameter.taskNames.contains("build")) {
        outputs.upToDateWhen { false }
    }

    val version = project.version
    val description = project.description

    filesMatching("plugin.yml") {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        expand(
            mapOf(
                "version" to version,
                "description" to description
            )
        )
    }
}

tasks.shadowJar {
    minimize()

    archiveFileName.set("${rootProject.name}-${project.version}.jar")

    relocate("io.github.retrooper.packetevents", "ac.grim.grimac.shaded.packetevents")
    relocate("co.aikar.commands", "ac.grim.grimac.shaded.acf")
    relocate("co.aikar.locale", "ac.grim.grimac.shaded.locale")
    relocate("club.minnced", "ac.grim.grimac.shaded.discord-webhooks")
    relocate("github.scarsz.configuralize", "ac.grim.grimac.shaded.configuralize")
    relocate("com.github.puregero", "ac.grim.grimac.shaded.com.github.puregero")
    relocate("com.google.code.gson", "ac.grim.grimac.shaded.gson")
    relocate("alexh", "ac.grim.grimac.shaded.maps")
    relocate("it.unimi.dsi.fastutil", "ac.grim.grimac.shaded.fastutil")
    relocate("net.kyori", "ac.grim.grimac.shaded.kyori")
    relocate("okhttp3", "ac.grim.grimac.shaded.okhttp3")
    relocate("okio", "ac.grim.grimac.shaded.okio")
    relocate("org.yaml.snakeyaml", "ac.grim.grimac.shaded.snakeyaml")
    relocate("org.json", "ac.grim.grimac.shaded.json")
    relocate("org.intellij", "ac.grim.grimac.shaded.intellij")
    relocate("org.jetbrains", "ac.grim.grimac.shaded.jetbrains")
}
