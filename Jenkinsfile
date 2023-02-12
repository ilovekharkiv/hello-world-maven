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
            environment {
                AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
                AWS_SECRET_ACCESS_KEY_ID = credentials('AWS_SECRET_ACCESS_KEY_ID')
                TF_VAR_env_prefix = 'test'
            }
            steps {
                script {
                    my_groovy.provisionInstance()     
                }
            }
        }
        stage("Deploy to EC2") {
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
