pipeline {
  agent any
  triggers {
    cron('H * * * *')
  }
  stages {
    stage('Build') {
      steps {
        echo 'Building......'
        echo 'Building......'
        sh './gradlew clean build'
      }
      post {
        always {
          junit 'build/test-results/test/TEST-*.xml'
          findbugs(pattern: 'build/reports/findbugs/*.xml')
          archive 'build/libs/*.jar'
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