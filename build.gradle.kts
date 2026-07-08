plugins {
    id("org.jetbrains.intellij.platform") version "2.17.0"
    java
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://maven.mooc.fi/releases")
    maven("https://maven.mooc.fi/snapshots")
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.1.4")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()
    }

    implementation("fi.helsinki.cs.tmc:core:0.10.5-SNAPSHOT")
    implementation("fi.helsinki.cs.tmc:tmc-langs-util:0.8.7-SNAPSHOT")
    implementation("org.apache.commons:commons-lang3:3.5")
    implementation("jdom:jdom:1.1")
    implementation("org.picocontainer:picocontainer:2.15")
    implementation("org.slf4j:slf4j-api:1.7.21")


}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("tmc-plugin-intellij/src/main/java"))
        resources.setSrcDirs(listOf("tmc-plugin-intellij/resources"))
    }
    test {
        java.setSrcDirs(listOf("tmc-plugin-intellij/src/test/java"))
    }
}
