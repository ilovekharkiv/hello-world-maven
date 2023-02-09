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
        stage("commit new version update") {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github_token', passwordVariable: 'PASS', usernameVariable: 'USER')])
                    sh 'git config --global user.email "jenkins@example.com"'
                    sh 'git config --global user.name "jenkins"'
                    sh 'git status'
                    sh 'git branch'
                    sh 'git config --list'
                    sh "git remote set-url origin https://${USER}:${PASS}@github.com/ilovekharkiv/hello-world-maven.git"
                    sh 'git add .'
                    sh 'git commit -m "CI - version update"'
                    sh 'git push origin HEAD:master'   
                }
            }
        }
        }
        
    }
