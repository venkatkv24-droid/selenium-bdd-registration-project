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
        }
    }
}