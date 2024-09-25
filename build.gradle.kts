plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.2"
    id("io.freefair.lombok") version "8.10"
}

group = "ac.grim.grimac"
version = "2.3.67"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot

    maven("https://jitpack.io/") { // Grim API
        content {
            includeGroup("com.github.ItsClairton")
        }
    }

    maven("https://repo.viaversion.com") // ViaVersion
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://nexus.scarsz.me/content/repositories/releases") // Configuralize
    maven("https://repo.opencollab.dev/maven-snapshots/") // Floodgate
    maven("https://repo.opencollab.dev/maven-releases/") // Cumulus (for Floodgate)
    maven("https://repo.codemc.io/repository/maven-snapshots/") // Packetevents
    mavenCentral() // FastUtil, Discord-Webhooks
}

dependencies {
    implementation("com.github.retrooper:packetevents-spigot:2.5.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("club.minnced:discord-webhooks:0.8.0") // Newer versions include kotlin-stdlib, which leads to incompatibility with plugins that use Kotlin
    implementation("it.unimi.dsi:fastutil:8.5.13")
    implementation("github.scarsz:configuralize:1.4.0")

    implementation("com.github.ItsClairton:GrimAPI:e691d1ae36")

    implementation("org.jetbrains:annotations:24.1.0")
    compileOnly("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.viaversion:viaversion-api:4.9.4-SNAPSHOT")
    //
    compileOnly("io.netty:netty-all:4.1.85.Final")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing.publications.create<MavenPublication>("maven") {
    artifact(tasks["shadowJar"])
}

tasks.shadowJar {
    minimize()

    archiveFileName.set("${project.name}.jar")
    destinationDirectory.set(project.findProperty("outputJar")?.let { file(it) })

    if (System.getProperty("dev") == null) { // You can't use hotswap with relocate =(
        relocate("io.github.retrooper.packetevents", "ac.grim.grimac.shaded.io.github.retrooper.packetevents")
        relocate("com.github.retrooper.packetevents", "ac.grim.grimac.shaded.com.github.retrooper.packetevents")
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
}

tasks.processResources {
    if (gradle.startParameter.taskNames.contains("build")) {
        outputs.upToDateWhen { false }
    }

    val version = project.version

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}