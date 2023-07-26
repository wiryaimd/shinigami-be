pipeline {
    agent any
    stages{
        stage('checkout'){
            steps{
                sh 'chmod +x mvnw'
            }
        }
        stage('build'){
            steps{
                sh './mvnw clean package -DskipTests'
            }
        }
    }
}