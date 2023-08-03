pipeline {
    agent any
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

        stage('build'){
            steps{
                sh "docker build -t wiryaimd/shinigami-cicd:${env.BUILD_NUMBER} ." // pakai petik " ketika pass variable seperti ${env} agar tidak err: bad substitution

                echo 'hayuuu image build'
            }
        }

        stage('update'){
            steps{
                sh 'docker stop shinigami-cicd1 || true' // berisi "|| true", persis seperti logical operator,
                sh 'docker rm shinigami-cicd1 || true' // agar jika docker stop gagal maka akan execute || true dimana tetap ngeresponse status success (instead fail/false)
                sh "docker run --name shinigami-cicd1 -p 8081:8081 --network shinigami1 -d wiryaimd/shinigami-cicd:${env.BUILD_NUMBER} --server.port=8081 --spring.datasource.url=jdbc:mysql://5650c48b8952:3306/db_shinigami_8ce117 --spring.datasource.password=8ce117da --logging.file.name=logs/shinigami_app.log --midtrans.secretkey=SB-Mid-server-9fZvjySLYHU4aqshNXggkbbU"

                echo 'update success borr yahahah'
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