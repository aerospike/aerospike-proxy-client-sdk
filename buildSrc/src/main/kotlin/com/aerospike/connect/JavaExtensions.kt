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

package com.aerospike.connect

import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Setup Java tasks and compiler arguments.
 */
fun Project.setupJavaBuild() {
    val javaTargetVersion = "1.8"
    project.extra["javaTargetVersion"] = javaTargetVersion

    val compileJava: JavaCompile by tasks
    compileJava.sourceCompatibility =
        javaTargetVersion
    compileJava.targetCompatibility =
        javaTargetVersion
    compileJava.options.apply {
        compilerArgs.add("-Xlint:all")
        compilerArgs.add("-Werror")
        compilerArgs.add("-Xlint:-processing")
    }


    val compileTestJava: JavaCompile by tasks
    compileTestJava.sourceCompatibility =
        javaTargetVersion
    compileTestJava.targetCompatibility =
        javaTargetVersion
    compileTestJava.options.apply {
        compilerArgs.add("-Xlint:all")
        compilerArgs.add("-Werror")
    }

    project.afterEvaluate {
        if (tasks.findByName("compileKotlin") != null) {
            /**
             * Ensure code is compiled for java 8 target.
             */
            val compileKotlin: KotlinCompile by tasks
            compileKotlin.kotlinOptions {
                jvmTarget = javaTargetVersion
                allWarningsAsErrors = true
            }
            val compileTestKotlin: KotlinCompile by tasks
            compileTestKotlin.kotlinOptions {
                jvmTarget = javaTargetVersion
                allWarningsAsErrors = true
            }
        }

        tasks.withType<Jar> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        tasks.withType<org.gradle.jvm.tasks.Jar> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }

    val java = (project.extensions["java"] as JavaPluginExtension)
    java.apply {
        withJavadocJar()
        withSourcesJar()
    }

    tasks.getByName("javadoc", Javadoc::class) {
        options {
            this as StandardJavadocDocletOptions

            // Suppress Javadoc lint errors as gRPC generated stubs don't have
            // proper Javadoc.
            addBooleanOption("Xdoclint:none", true)
        }
    }
}
