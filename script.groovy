def buildJar() {
    echo "building the application...please wait"
    sh 'mvn build-helper:parse-version versions:set \
    - DnewVersion=\${parsedVersion.majorVersion}. \${parsedVersion.minorVersion}. \${parsedVersion.nextIncrementalVersion} versions:commit'
    sh 'mvn clean package'
    
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
    sh "sleep 6"
}

return this
