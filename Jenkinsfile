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
                sh "docker run --name shinigami-cicd1 -p 8080:8080 --network shinigami1 -d wiryaimd/shinigami-cicd:${env.BUILD_NUMBER} --server.port=8080 --spring.datasource.url=jdbc:mysql://27b3e296e514:3306/db_shinigami_8ce117 --spring.datasource.password=9x29I2ZPuLN6M8FkfUO --logging.file.name=logs/shinigami_app.log --midtrans.secretkey=Mid-server-M0YlsNpDY_0fTcDjbrXuzu58 --spring.datasource.hikari.minimum-idle=20 --spring.datasource.hikari.maximum-pool-size=25 --spring.datasource.hikari.idle-timeout=200000 --spring.datasource.hikari.connection-timeout=250000 --spring.jpa.open-in-view=false"

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
