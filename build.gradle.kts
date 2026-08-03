plugins {
    id("java")
}

group = "de.swiesend"
version = "3.0.0-alpha"

java {
    modularity.inferModulePath = true
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.hypfvieh:dbus-java-core:5.2.0")
    implementation("com.github.hypfvieh:dbus-java-transport-native-unixsocket:5.2.0")
    implementation("org.slf4j:slf4j-api:2.0.17")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
