pipeline { 
    agent any   
    stages { 
        stage("SCM") {
            steps { 
                  checkout scm: [$class: 'GitSCM',
                       branches: [[name: 'master']],
                       userRemoteConfigs: [[url: 'https://github.com/ParadoxZero/GameMenu-cpp.git']],
                       extensions: [[$class: 'CloneOption', timeout: 120]]
                      ]
            }
        }
	stage("SonarQube analysis") {
            environment {
            scannerHome = tool 'sonarqube_scanner'
            }
            steps { 
                    withSonarQubeEnv('sonarqube_scanner') {
                             sh "${scannerHome}/bin/sonar-scanner -X  -Dsonar.host.url=http://sonarqube:9000  -Dsonar.language=c++ -Dsonar.projectName=test -Dsonar.lang.patterns.c++=**/*.cxx,**/*.cpp,**/*.cc,**/*.c,**/*.hxx,**/*.hpp,**/*.hh,**/*.h -Dsonar.projectVersion=1.1 -Dsonar.projectKey=My_project   -Dsonar.sources=src -Dsonar.tests=. -Dsonar.test.inclusions=**/*Test*/** -Dsonar.exclusions=**/*Test*/**"
                }
            }
        }
        stage('Sonarqube Quality Gates.') {
            options {
                timeout(time: 15, unit: 'MINUTES') 
            }
            steps{ 
                sh 'curl "http://admin:admin@sonarqube:9000/api/webhooks/create" -X POST -d "name=jenkins&url=http://jenkins:8080/sonarqube-webhook/"'
                script {
                    def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
                        if (qg.status != 'OK') {
                             error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }  
                    }
                }
            }
        stage('build c++ code'){
                steps { 
                    sh 'make'
                    sh 'cppcheck --enable=information --xml --xml-version=2 src 2> cppcheck.xml'
                }
                post {
                    always {
                    archiveArtifacts artifacts: 'build/GameMenu/GameMenu.o', fingerprint: true
                    }
                }
            }
        stage("Uniti test") { 
            steps { 
                sh 'ls -l .'
                publishCppcheck pattern:'cppcheck.xml'
                }
            }
	stage('Atrifactory'){
	    steps {
	        script {
                    def server = Artifactory.server 'artifactory'
                    def uploadSpec = """{
                        "files": [
                            {
                                "pattern": "*.o",
                                "target": "example-repo-local/Lib"
                            }
                        ]
                    }"""
                    server.upload spec: uploadSpec, failNoOp: true
                }
	    }
        }
    }
}

