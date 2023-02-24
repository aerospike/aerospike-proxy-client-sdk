pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    stages {
        stage("Pipeline") {
            stages {
                stage("Checkout") {
                    steps {
                        checkout([
                                $class           : 'GitSCM',
                                branches         : scm.branches,
                                extensions       : scm.extensions + [[$class: 'CleanBeforeCheckout']],
                                userRemoteConfigs: scm.userRemoteConfigs
                        ])
                    }
                }

                stage("Build") {
                    steps {
                        echo "Building.."
                        sh "./gradlew --no-daemon clean build"
                        sh "ls proto/build/libs/aerospike-proxy-proto-*.jar"
                    }
                }

                stage("Upload") {
                    steps {
                        echo "Uploading archives.."
                        sh "./gradlew --no-daemon publish"
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/build/libs/**/*.jar', fingerprint: true
            step([$class: 'JacocoPublisher'])
        }
        cleanup {
            cleanWs()
        }
    }
}
