def my_groovy
pipeline {
    agent any //{node 'jenkins_node'}
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
        stage("Build .jar") {
            steps {
                script {
                    my_groovy.buildJar()
                }
            }
        }
        stage("Build Docker image") {    
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
        }
        
    }
