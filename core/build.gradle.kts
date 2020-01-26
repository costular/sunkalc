plugins {
    kotlin("jvm")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.threeten:threetenbp:1.4.1")
    implementation("org.threeten:threeten-extra:1.5.0")
    implementation("org.apache.commons:commons-math3:3.6.1")


    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}