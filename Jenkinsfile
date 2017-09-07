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
        parallel(
          "Test": {
            echo 'Testing......'
            
          },
          "test parallel pipeline": {
            echo 'testing parallel'
            
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