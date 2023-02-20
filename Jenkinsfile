def my_groovy
pipeline {
    agent {node 'jenkins_node'}
    tools {
        maven 'maven-3.6.3'
    }
    stages {
        stage("Groovy Init + Version increment") {
            steps {
                script {
                    my_groovy = load "script.groovy"
                    my_groovy.autoIncrement()
                }
            }
        }
        stage("Build .war file - epam.war") {
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
            steps {
                environment {
                    DOCKER_CREDS = credentials('dockerhub')
                }
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
