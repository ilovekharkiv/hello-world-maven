def autoIncrement() {
    echo "Autoincrementing app version. Please wait"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
    def parser = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = parser[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}



def buildJar() {
    echo "building the application...please wait"
    sh 'mvn package'
    
}

def buildDockerImage() {
    echo "building docker image"
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh "docker build -t ilovekharkiv/ilovekharkiv:$IMAGE_NAME ."
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh "docker push ilovekharkiv/ilovekharkiv:$IMAGE_NAME"
}
}

def deployStaging() {
        sshagent(['development_server']) {
            def dockerCmd = 'docker run -p 8080:8080 -d ilovekharkiv/ilovekharkiv:myjava-1.0'
            sh "ssh -o StrictHostKeyChecking=no ubuntu@3.69.169.187 ${dockerCmd}"
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
