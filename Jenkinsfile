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
        stage("Build Docker + Push") {
            steps {
                script {
                    my_groovy.buildDockerImage()
                }
              
                }
        }
        stage("Deployment") {
            steps {
                script {
                    my_groovy.deployStaging()     
                }
            }
        }
        stage("Commit new version pom.xml") {
            steps {
                script {
                        my_groovy.pushNewpom()
                    }
                }
        }
        }
        
    }
