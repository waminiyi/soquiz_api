
plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.0-rc-1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "com.soquiz"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-host-common-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-status-pages-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-auth-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-netty-jvm:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-config-yaml:3.0.0-rc-1")
    implementation("io.ktor:ktor-server-test-host-jvm:3.0.0-rc-1")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.jetbrains.exposed:exposed-core:0.53.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.53.0")



    implementation("org.jetbrains.exposed:exposed-dao:0.53.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-server-swagger: 2.3.12")



    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

tasks.withType<JavaExec> {
    environment("POSTGRES_USER", System.getenv("POSTGRES_USER"))
    environment("POSTGRES_PASSWORD", System.getenv("POSTGRES_PASSWORD"))
    environment("POSTGRES_URL", System.getenv("POSTGRES_URL"))
    environment("JWT_SECRET", System.getenv("JWT_SECRET"))
}
