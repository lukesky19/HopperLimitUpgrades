plugins {
    java
}

group = "com.github.lukesky19"
version = "1.1.0.0"

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
        name = "bentobox-addons-repo"
    }

    mavenLocal()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("com.github.lukesky19:SkyLib:1.3.0.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    // Hooks
    compileOnly("world.bentobox:bentobox:3.1.0-SNAPSHOT")
    compileOnly("world.bentobox:limits:1.27.0-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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