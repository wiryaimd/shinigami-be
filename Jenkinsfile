pipeline {
    agent none
    stages{
        stage('checkout'){
//             agent {
//                 docker{
//                     image 'openjdk:17-jdk-slim'
//                 }
//             }

            steps{
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }
//         stage('build'){
//             steps{
//                 sh './mvnw clean package -DskipTests'
//             }
//         }
    }
}

// always back to you my friend
// how can i forgive myself when im still in love with you