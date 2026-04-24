pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "rahulkr1510/springboot-cicd-k8s"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        SONAR_HOST_URL = "http://172.31.28.226:9000"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                    mvn sonar:sonar \
                      -Dsonar.projectKey=springboot-cicd-k8s \
                      -Dsonar.projectName=springboot-cicd-k8s
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
                sh 'docker tag $DOCKER_IMAGE:$DOCKER_TAG $DOCKER_IMAGE:latest'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    docker push $DOCKER_IMAGE:$DOCKER_TAG
                    docker push $DOCKER_IMAGE:latest
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'CI passed. PR can be merged.'
        }
        failure {
            echo 'CI failed. PR should not be merged.'
        }
    }
}
