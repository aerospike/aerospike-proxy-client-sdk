/*
 *
 *  Copyright 2012-2022 Aerospike, Inc.
 *
 *  Portions may be licensed to Aerospike, Inc. under one or more contributor
 *  license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

import com.aerospike.connect.setupJavaBuild
import com.aerospike.connect.setupPublishingTasks
import com.aerospike.connect.setupReleaseTasks
import com.aerospike.connect.setupVulnerabilityScanning
import io.snyk.gradle.plugin.SnykTask

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("io.freefair.gradle:lombok-plugin:6.6.3")
    }
}

plugins {
    jacoco
    id("io.snyk.gradle.plugin.snykplugin")
    id("com.google.protobuf") version "0.9.4" apply false
}

allprojects {
    // Configures the Jacoco tool version to be the same for all projects that have it applied.
    pluginManager.withPlugin("jacoco") {
        // If this project has the plugin applied, configure the tool version.
        jacoco {
            toolVersion = "0.8.7"
        }
    }
}

tasks.withType<SnykTask> {
    onlyIf { false }
}

subprojects {
    apply {
        plugin(JavaPlugin::class.java)
        plugin("java-library")
        plugin("jacoco")
        plugin("signing")
        plugin("maven-publish")
        plugin("net.researchgate.release")
        plugin("io.snyk.gradle.plugin.snykplugin")
    }

    dependencies {
        "dataFiles"("org.json:json:20230227")
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url =
                uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }

    group = "com.aerospike"

    // Common dependency versions
    project.extra["protobufVersion"] = "3.24.0"
    project.extra["protocVersion"] = project.extra["protobufVersion"]
    project.extra["grpcVersion"] = "1.57.2"
    project.extra["coroutinesVersion"] = "1.7.3"
    project.extra["grpcKotlinVersion"] = "1.3.0"

    setupJavaBuild()
    setupReleaseTasks()
    setupPublishingTasks()
    setupVulnerabilityScanning()

    tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
