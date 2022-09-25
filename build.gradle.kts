val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hikaricp_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val koin_version: String by project

val flyway_version: String by project


plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
    id("org.flywaydb.flyway") version "9.3.0"

}

group = "com.shevapro"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    google()
}

dependencies {

    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    implementation("org.flywaydb:flyway-core:$flyway_version")

    // serializer

    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.1")

    implementation("org.postgresql:postgresql:$postgres_version")
    // session

//logger
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
// Koin for Ktor
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
// SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
//jwt
    // bcrypt
    implementation("de.nycode:bcrypt-jvm:2.3.0")
    // freemarker
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.39.2")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.0.2")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
}

tasks.test {
    filter {
        includeTestsMatching("*MyClassPostfix")
    }
    useJUnitPlatform()

}