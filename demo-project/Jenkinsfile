node('master'){
    # Especificar la ruta adecuada en cada caso
    def mavenFolfer = "C:/Users/{{user}}/.m2";
    stage('checkout') {
        # Es necesario configurar la credenciales previamente en el jenkins
        git branch: 'master', credentialsId: 'GitLab', url: 'https://gitlab.sngular.com/sngulartech/devops_jenkins_and_docker'
    }
    stage('compile and save files'){
        dir("./demo-project") {
            bat 'docker run -i --rm --name maven-image -v /var/run/docker.sock:/var/run/docker.sock -v "'+mavenFolfer+'":/root/.m2 -v "%cd%":/usr/src/app  -w /usr/src/app maven:3.5.2-jdk-8-alpine mvn clean package -DskipTests'       
        }
    }
    stage('unit tests'){
        dir("./demo-project") {
            bat 'docker run -i --rm --name my-maven-test -v "%cd%":/usr/src/app -v "'+mavenFolfer+'":/root/.m2 -w /usr/src/app maven:3.5.2-jdk-8-alpine mvn test -Pcucumber'           
        }
    }
    stage('Result'){
        junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts artifacts: 'demo-project/target*//*.jar', followSymlinks: false, onlyIfSuccessful: true
    }
    stage('sonar'){
        dir("./demo-project") {
            def localUrl = InetAddress.localHost.hostAddress
            URLSONAR = "http://"+localUrl+":9000"
            bat 'docker run -i --rm --name my-maven-project -v "%cd%":/usr/src/app  -v "'+mavenFolfer+'":/root/.m2 -e SONAR_HOST_URL='+URLSONAR+' -w /usr/src/app maven:3.5.2-jdk-8-alpine mvn sonar:sonar'    
        }
        
    }
    stage('docker up app'){
        dir("./demo-project") {
            bat 'docker-compose -f docker-compose.yml up -d'       
        }
    }
    stage('cucumber'){
        dir("./demo-project") {
            bat 'docker run -i --rm --name my-maven-test -v "%cd%":/usr/src/app -v "'+mavenFolfer+'":/root/.m2 -w /usr/src/app maven:3.5.2-jdk-8-alpine mvn test'           
        }
       cucumber 'demo-project/target/surefire-reports/cucumber.json'
    }
    stage('integration tests with postman'){
        dir("./demo-project") {
            bat 'docker-compose -f docker-compose-integration-tests.yml up'
        }
   }
   stage("docker down"){
       dir("./demo-project") {
            bat 'docker-compose down --remove-orphans'
       }
   }
}