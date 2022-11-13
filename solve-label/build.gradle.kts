

plugins {
    id("java")
}

group = "it.unibo.tuprolog"
version = "0.1.0-archeo+457155cc"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":solve"))
    implementation(project(":solve-classic"))
    implementation(project(":core"))
    implementation(project(":utils"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}