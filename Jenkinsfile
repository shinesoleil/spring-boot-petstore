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
          archive 'build/libs/*.jar'
          junit 'build/test-results/test/TEST-*.xml'
          findbugs(pattern: 'build/reports/findbugs/*.xml')
        }
      }
    }
    stage('Test') {
      steps {
        parallel(
          "Test1": {
            echo 'Testing1......'
          },
          "Test2": {
            echo 'Testing2......'
          }
        )
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying......'
      }
    }
  }
}