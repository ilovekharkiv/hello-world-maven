def autoIncrement() {
    echo "Autoincrementing app version. Please wait"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
    def parser = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = parser[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}



def buildWar() {
    echo "building the application...please wait"
    sh 'mvn package -Dmaven.test.skip'
    
}

def buildDockerImage() {
    echo "building docker image"
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh "docker build -t ilovekharkiv/ilovekharkiv:$IMAGE_NAME ."
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh "docker push ilovekharkiv/ilovekharkiv:$IMAGE_NAME"
}
}

def provisionInstance() {
    environment {
                AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
                AWS_SECRET_ACCESS_KEY_ID = credentials('AWS_SECRET_ACCESS_KEY_ID')
                TF_VAR_env_prefix = 'test'
            }
    dir('terraform') {
        sh "terraform init -no-color"
        sh "terraform apply -no-color --auto-approve"
        EC2_PUBLIC_IP = sh (
            script: "terraform output ec2_public_ip",
            returnStdout: true
        ).trim()
    }
}

def runAnsible() {
            dir('ansible') {
            withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            sh "echo $PASS | ansible-playbook --inventory ${EC2_PUBLIC_IP}, --private-key /home/ubuntu/.ssh/ansible_server.pem --user ec2-user deploy-docker-ec2user.yaml -e docker_password=$PASS -e docker_image=$IMAGE_NAME"

        }                     
    }
}

def pushNewpom() {
    withCredentials([usernamePassword(credentialsId: '937066cb-a19e-47cb-986f-8eb4879f86ac', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                    sh "git remote set-url origin https://${USER}:${PASS}@github.com/ilovekharkiv/hello-world-maven.git"
                    sh 'git add .'
                    sh 'git commit -m "CI - version update"'
                    sh 'git push origin HEAD:master'
                    }
}

return this
