pipeline {
    agent any

    tools {
        // These names must match exactly what you name the installations
        // under Manage Jenkins -> Tools
        jdk 'jdk25'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                // Pulls whatever branch/commit triggered this build - matches
                // what "Pipeline script from SCM" already checked out, but
                // explicit here for clarity/readability.
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                // testFailureIgnore is already true in pom.xml, so this
                // completes even when scenarios fail - the pipeline itself
                // should still run reporting/archiving stages after.
                bat 'mvn -B test'
            }
        }

        stage('Archive Reports') {
            steps {
                // Makes the HTML reports downloadable/viewable from the Jenkins
                // build page itself, not just on your local disk.
                archiveArtifacts artifacts: 'test-output/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo "Build finished with status: ${currentBuild.currentResult}"
        emailext(
            to: 'venkat.kv24@gmail.com',
            subject: "Jenkins Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}: ${env.JOB_NAME}",
            body: """
                <p>Build <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b> finished with status: <b>${currentBuild.currentResult}</b></p>
                <p>View full build online: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
            """,
            mimeType: 'text/html',
            attachmentsPattern: 'test-output/**/*.html'
        )
        }
    }
}