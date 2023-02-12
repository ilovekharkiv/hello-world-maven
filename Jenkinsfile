def my_groovy
pipeline {
    agent {node 'jenkins_node'}
    tools {
        maven 'maven-3.6.3'
    }
    stages {
        stage("Groovy Init") {
            steps {
                script {
                    my_groovy = load "script.groovy"
                }
            }
        }
        stage("Version incrementation") {
            steps {
                script {
                    my_groovy.autoIncrement()
                }
            }
        }
        stage("Build .jar") {
            steps {
                script {
                    my_groovy.buildJar()
                }
            }
        }
        stage("Docker Build  + Push") {
            steps {
                script {
                    my_groovy.buildDockerImage()
                }
              
                }
        }
        stage("Instance provisioning") {
            steps {
                script {
                    my_groovy.provisionInstance()     
                }
            }
        }
        stage("Deploy to EC2") {
            environment {
                DOCKER_CREDS = credentials('dockerhub')
            }
            steps {
                script {
                    my_groovy.deployStaging()     
                }
            }
        }
        stage("Destroy Infrastructure") {
            steps {
                script {
                    my_groovy.destroyInfrastructure()     
                }
            }
        }
        
        /*stage("Commit new version pom.xml") {
            steps {
                script {
                        my_groovy.pushNewpom()
                    }
                }
        }
        }*/
        
    }
}
