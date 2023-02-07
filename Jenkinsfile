pipeline {
    agent {node 'jenkins_node'}
    stages {
         stage("test") {
            steps {
                script {
                    echo "Testing the application"
                    echo "Executing pipeline for branch $BRANCH_NAME" 
                }
            }
        }
        stage("build") {
            when {
                expression {
                    BRNACH_NAME == 'master'
                }
            }
            steps {
                script {
                    echo "Building the application"
                }
            }
        }
        stage("deploy") {
            when {
                expression {
                    BRNACH_NAME == 'master'
                }
            }
            steps {
                script {
                    echo "deploying the application"
                }
            }
        }
    }
        
}
