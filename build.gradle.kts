plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.web3j:core:4.8.7")
    implementation("org.bouncycastle:bcprov-jdk15on:1.68")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}