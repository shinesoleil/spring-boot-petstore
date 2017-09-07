pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Building......'
        sh './gradlew clean build'
      }
      post {
        always {
          junit 'build/test-results/test/TEST-*.xml'
        }
      }
    }
    stage('Test') {
      steps {
        echo 'Testing......'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying......'
      }
    }
  }
}