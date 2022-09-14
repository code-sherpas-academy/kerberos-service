import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.2"
	id("io.spring.dependency-management") version "1.0.12.RELEASE"
	id("org.flywaydb.flyway") version "9.1.2"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "rocks.codesherpas.academy.accelerate.backend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	runtimeOnly("org.postgresql:postgresql:42.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.rest-assured:rest-assured:5.1.1")
	testImplementation("io.rest-assured:xml-path:5.1.1")
	testImplementation("io.rest-assured:json-path:5.1.1")
	testImplementation("io.rest-assured:kotlin-extensions:5.1.1")
	testImplementation("io.rest-assured:json-schema-validator:5.1.1")
	testImplementation("io.rest-assured:spring-mock-mvc:5.1.1")
	testImplementation("io.mockk:mockk:1.12.7")
	testImplementation("com.google.code.gson:gson:2.9.1")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

flyway {
	val databaseHost = System.getenv("DATABASE_HOST") ?: "localhost"
	val databasePort = System.getenv("DATABASE_PORT") ?: "5432"
	val databaseName = System.getenv("DATABASE_NAME") ?: "kerberos"

	url = "jdbc:postgresql://$databaseHost:$databasePort/$databaseName"
	user = System.getenv("POSTGRES_USER") ?: "compose-postgres"
	password= System.getenv("POSTGRES_PASSWORD") ?: "compose-postgres"
	defaultSchema = "public"
	schemas = arrayOf(defaultSchema)
	cleanDisabled = false
}
