pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        echo 'Testing..'
        sh './gradlew build'
        sh 'junit \'report/junit/*xml\''
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}