val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val hikaricp_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val koin_version: String by project



plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
}



group = "com.shevapro"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // serializer

        implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.2")

    implementation("org.postgresql:postgresql:$postgres_version")
    // session

//logger
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
// Koin for Ktor
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
// SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
//jwt
    // bcrypt
    implementation("de.nycode:bcrypt-jvm:2.3.0")
    // freemarker
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.37.3")
    implementation("io.ktor:ktor-server-cors:2.0.2")
    implementation("io.ktor:ktor-server-call-logging:2.0.2")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.2")
    implementation("io.ktor:ktor-server-core-jvm:2.0.2")
//    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.0.2")
    implementation("io.ktor:ktor-server-netty-jvm:2.0.2")
    implementation("io.ktor:ktor-server-sessions-jvm:2.0.2")
    implementation("io.ktor:ktor-server-auth-jvm:2.0.2")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.0.2")
    implementation("io.ktor:ktor-server-freemarker-jvm:2.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.0.2")
}

tasks.test {
    filter {
        includeTestsMatching("*MyClassPostfix")
    }
    useJUnitPlatform()

}