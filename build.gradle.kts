plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    id("io.ktor.plugin") version "3.0.3"
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.iapprusher"
version = "0.0.1"

application {
    mainClass.set("com.iapprusher.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("fat.jar")
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    implementation(libs.ktor.server.rate.limiting)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.core)
    // implementation(libs.ktor.serialization.kotlinx.json)
    implementation("io.ktor:ktor-serialization-gson:3.0.2")
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.bson)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.request.validation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    implementation("org.mongodb:bson-kotlinx:5.2.1")

}
