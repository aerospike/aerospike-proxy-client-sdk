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
                        sh "file proto/build/libs/aerospike-proxy-proto-*.jarr || exit 0"
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
            archiveArtifacts artifacts: '**/build/test-results/**/*.xml'
            archiveArtifacts artifacts: '**/build/libs/**/*.jar,**/build/distributions/*', fingerprint: true
            archiveArtifacts artifacts: '**/build/reports/**/*', fingerprint: true
            junit testResults: '**/build/test-results/**/*.xml', keepLongStdio: true
            step([$class: 'JacocoPublisher'])
        }
        cleanup {
            cleanWs()
        }
    }
}
