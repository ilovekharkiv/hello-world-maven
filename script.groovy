def autoIncrement() {
    echo "Autoincrementing app version. Please wait"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
    def parser = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = parser[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}



def buildJar() {
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

def deployStaging() {
        echo "Waiting for ec2-instance init"
        //sleep(time: 70, unit: "SECONDS")

        echo "Deploying docker image to EC2 instance. Public IP - ${EC2_PUBLIC_IP}"

        def shellCmd = "bash ./server-cmds.sh ilovekharkiv/ilovekharkiv:$IMAGE_NAME ${DOCKER_CREDS_USR} ${DOCKER_CREDS_PSW}"
        def ec2dev = "ec2-user@${EC2_PUBLIC_IP}"
        sshagent(['dev-server-ssh-key']) {
            sh "scp -o StrictHostKeyChecking=no server-cmds.sh ${ec2dev}:/home/ec2-user"
            sh "scp -o StrictHostKeyChecking=no docker-compose.yaml ${ec2dev}:/home/ec2-user"
            sh "ssh -o StrictHostKeyChecking=no ${ec2dev} ${shellCmd}"
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
