plugins {
    `java-library`
}

group = "com.github.lukesky19"
version = "1.3.1.0"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }

    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }

    maven("https://jitpack.io") {
        name = "jitpack"
    }

    maven("https://repo.codemc.org/repository/maven-public/") {
        name = "codemc"
    }

    maven("https://repo.codemc.org/repository/bentoboxworld/") {
        name = "bentobox-repo"
    }

    maven("https://repo.rosewooddev.io/repository/public/") {
        name = "RoseWood"
    }

    mavenLocal()
}

dependencies {
    // Paper
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    // SkyLib
    compileOnly("com.github.lukesky19:SkyLib:2.0.2.0")

    // Integration
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("world.bentobox:bentobox:3.18.0-SNAPSHOT")
    compileOnly("world.bentobox:limits:1.28.2-SNAPSHOT")
    compileOnly("org.black_ixx:playerpoints:3.3.3")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    // This allows usage of @apiNode in javadocs
    javadoc {
        (options as StandardJavadocDocletOptions).tags("apiNote:a:API Note:")
    }

    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
        archiveClassifier.set("")
    }

    build {
        dependsOn(javadoc)
    }
}