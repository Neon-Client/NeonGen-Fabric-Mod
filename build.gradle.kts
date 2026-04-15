plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val minecraftVersion = "26.1.1"
val loaderVersion = "0.18.5"
val fabricVersion = "0.145.3+26.1.1"

group = "me.rhys"
version = "1.2"

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
    testCompileOnly("org.projectlombok:lombok:1.18.44")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.44")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withSourcesJar()
}

loom {
    accessWidenerPath.set(file("src/main/resources/neon-gen.classtweaker"))
}

base {
    archivesName.set("NeonGen")
}

tasks {
    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }

        exclude("lombok.config")
    }

    withType(JavaCompile::class.java) {
        options.release.set(25)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_NeonGen" }
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    named<Jar>("sourcesJar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}