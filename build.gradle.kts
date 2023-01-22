import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven {
        url = uri("https://eldonexus.de/repository/maven-public")
    }
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    implementation("de.chojo.sadu:sadu:1.2.0")
    implementation("net.dv8tion:JDA:5.0.0-beta.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.1.2")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    relocate("net.dv8tion.jda", "dev.amonhtm.verify")
}