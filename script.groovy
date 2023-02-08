def incrementJar() {
    echo "Incrementing app vesion"
    sh 'mvn build-helper:parse-versions:set \
        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.MinorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        versions:commit'
}
def buildJar() {
    echo "building the application...please wait"
    sh 'mvn package'
}

def buildDockerImage() {
    echo "building docker image"
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
    sh 'docker build -t ilovekharkiv/ilovekharkiv:myjava-1.0 .'
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh 'docker push ilovekharkiv/ilovekharkiv:myjava-1.0'
}
}

def deployStaging() {
    echo "deployment, please wait..."
    sh "sleep 5"
}

return this
