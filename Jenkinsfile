pipeline {
  agent any
  stages {
    stage('Test') {
      properties([pipelineTriggers([[$class: 'GitHubPushTrigger'], pollSCM('H/15 * * * *')])])
      steps {
        echo 'Testing..'
        sh './gradlew test'
        junit 'report/junit/*xml'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}