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

// always back to you my friend
// how can i forgive myself when im still in love with you