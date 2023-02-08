def incrementJar() {
    echo "Incrementing app vesion"
    sh 'mvn build-helper:parse-versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.MinorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit'
    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
    def version = matcher[0][1]
    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}
def buildJar() {
    echo "building the application...please wait"
    sh 'mvn clean package'
}

def buildDockerImage() {
    echo "building docker image"
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh "docker build -t ilovekharkiv/ilovekharkiv:$IMAGE_NAME ."
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh "docker push ilovekharkiv/ilovekharkiv$IMAGE_NAME"
}
}

def deployStaging() {
    echo "deployment, please wait..."
    sh "sleep 6"
}

return this
