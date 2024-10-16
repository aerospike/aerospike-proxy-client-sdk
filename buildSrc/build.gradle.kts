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

plugins {
    `kotlin-dsl`
    groovy
    `java-gradle-plugin`
    `lifecycle-base`
}

repositories {
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    api("net.researchgate:gradle-release:2.8.1")
    api("io.snyk.gradle.plugin.snykplugin:io.snyk.gradle.plugin.snykplugin.gradle.plugin:0.6.1")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    api("com.github.ben-manes:gradle-versions-plugin:+")
}
