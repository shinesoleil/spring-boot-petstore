pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        echo 'Testing..'
        sh './gradlew clean test'
        junit '**/build/test-results/test/TEST-*.xml'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}