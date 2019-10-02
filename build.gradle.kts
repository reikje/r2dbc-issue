import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.3.50"
	kotlin("plugin.spring") version "1.3.50"
}

group = "me.reik"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.data:spring-data-r2dbc:1.0.0.M2")
	implementation("org.springframework:spring-jdbc:5.2.0.M2")
	implementation("org.springframework.boot:spring-boot-starter:2.2.0.M6")
	implementation("org.springframework.boot:spring-boot-starter-webflux:2.2.0.M6")

	implementation("org.postgresql:postgresql:42.2.8")
	implementation("io.r2dbc:r2dbc-postgresql:0.8.0.M8")
	implementation("org.flywaydb:flyway-core:6.0.3")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.3.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.0.M6") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")

	testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:5.2.0")

	testImplementation("org.testcontainers:postgresql:1.12.1")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.5.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
	testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.5.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
