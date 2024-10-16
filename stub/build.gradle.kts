import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    id("com.google.protobuf")
    java
}

apply(plugin = "com.google.protobuf")

dependencies {
    protobuf("com.aerospike:aerospike-proxy-proto:1.1.1")
    api(
        "com.google.protobuf:protobuf-java:${
            project
                .extra["protobufVersion"]
        }"
    )
    api("io.grpc:grpc-netty:${project.extra["grpcVersion"]}") {
        exclude(group = "io.netty", module = "netty-codec-http2")
    }
    api(
        "io.netty:netty-codec-http2:${project.ext["grpcNettyVersion"]}"
    )


    api("io.grpc:grpc-stub:${project.extra["grpcVersion"]}")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.extra["coroutinesVersion"]}"
    )

    implementation("io.grpc:grpc-api:${project.extra["grpcVersion"]}")
    implementation("com.google.guava:guava:${project.extra["guavaVersion"]}")
    implementation("io.grpc:grpc-protobuf:${project.extra["grpcVersion"]}")
    implementation(
        "com.google.protobuf:protobuf-java-util:${
            project
                .extra["protobufVersion"]
        }"
    )
    implementation(
        "com.google.protobuf:protobuf-kotlin:${project.extra["protobufVersion"]}"
    )
    implementation(
        "io.grpc:grpc-kotlin-stub:${project.extra["grpcKotlinVersion"]}"
    )
}

// this makes it so IntelliJ picks up the sources but then ktlint complains
sourceSets {
    val main by getting { }
    main.java.srcDirs("build/generated/source/proto/main/java")
    main.java.srcDirs("build/generated/source/proto/main/grpc")
    main.java.srcDirs("build/generated/source/proto/main/kotlin")
    main.java.srcDirs("build/generated/source/proto/main/grpckt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    compilerOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

protobuf {
    protoc {
        artifact =
            "com.google.protobuf:protoc:${project.extra["protobufVersion"]}"
    }
    plugins {
        id("grpc") {
            artifact =
                "io.grpc:protoc-gen-grpc-java:${project.extra["grpcVersion"]}"
        }
        id("grpckt") {
            artifact =
                "io.grpc:protoc-gen-grpc-kotlin:${project.extra["grpcKotlinVersion"]}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
