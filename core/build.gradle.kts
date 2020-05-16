import com.novoda.gradle.release.PublishExtension

plugins {
    kotlin("jvm")
    id("com.novoda.bintray-release")
}

group = "com.costular"
version = "0.2.1"

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

configure<PublishExtension> {
    userOrg = "costular"
    groupId = "com.costular"
    artifactId = "sunkalc"
    publishVersion = version.toString()
    desc = "A tiny Kotlin library for calculating sun/moon positions and phases based on mourner/suncalc"
    website = "https://github.com/costular/sunkalc"
    bintrayUser = System.getenv("BINTRAY_USER")
    bintrayKey = System.getenv("BINTRAY_KEY")
    dryRun = false
}