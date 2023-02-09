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
    echo "deployment, please wait..."
    sh "sleep 4"
    echo "$IMAGE_NAME"
}

return this
