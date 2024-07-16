plugins {
    id("java")
}

group = "de.swiesend"
version = "2.0.1-alpha"

java {
    modularity.inferModulePath = true
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("at.favre.lib:hkdf")
    implementation("com.github.hypfvieh:dbus-java-core:5.0.0")
    implementation("com.github.hypfvieh:dbus-java-transport-native-unixsocket:5.0.0")
    implementation("org.slf4j:slf4j-api:2.0.13")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
