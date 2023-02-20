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
                    my_groovy.buildWar()
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
        stage("Run Ansible playbook") {
                steps {
                        script {
                            my_groovy.runAnsible()
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
