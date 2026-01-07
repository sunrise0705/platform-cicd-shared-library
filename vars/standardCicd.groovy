/**
 * Jenkins Shared Library Entry Point
 * - 서비스 저장소 Jenkinsfile은 이 함수를 호출하기만 하면 됩니다.
 *
 * cfg 예:
 *   serviceName        (required)
 *   imageRepo          (required)
 *   dockerfilePath     (optional, default: "Dockerfile")
 *   deployEnv          (optional, default: "staging")
 *   qualityGateLevel   (optional, default: "strict")
 *   deployStrategy     (optional, default: "rolling") // rolling|canary|bluegreen
 *   gitops             (optional, default: true)
 */
def call(Map cfg = [:]) {
  def c = org.yourorg.cicd.Config.normalize(cfg)

  pipeline {
    agent any

    options {
      timestamps()
      disableConcurrentBuilds()
      ansiColor('xterm')
      buildDiscarder(logRotator(numToKeepStr: '30'))
    }

    environment {
      SERVICE_NAME = "${c.serviceName}"
      IMAGE_REPO   = "${c.imageRepo}"
      DOCKERFILE   = "${c.dockerfilePath}"
      DEPLOY_ENV   = "${c.deployEnv}"
      STRATEGY     = "${c.deployStrategy}"
      GITOPS       = "${c.gitops}"
      QUALITY_GATE = "${c.qualityGateLevel}"
    }

    stages {
      stage('Preflight') {
        steps {
          script {
            org.yourorg.cicd.Preflight.validate(c)
          }
        }
      }

      stage('Checkout') {
        steps {
          checkout scm
        }
      }

      stage('Build') {
        steps {
          script {
            env.IMAGE = org.yourorg.cicd.Build.buildImage(c)
          }
        }
      }

      stage('Test') {
        steps {
          script {
            org.yourorg.cicd.Test.runUnitTests(c)
          }
        }
      }

      stage('Security') {
        steps {
          script {
            // DevSecOps by default: 서비스별로 끌 수 없도록 설계(원칙)
            org.yourorg.cicd.Security.runSast(c)
            org.yourorg.cicd.Security.runSca(c)
            org.yourorg.cicd.Security.runSecretScan(c)
            org.yourorg.cicd.Security.runImageScan(c, env.IMAGE)
          }
        }
      }

      stage('Package & Provenance') {
        steps {
          script {
            // 권장: SBOM 생성 + 이미지 서명/출처정보(Provenance)
            org.yourorg.cicd.Provenance.generateSbom(c, env.IMAGE)
            org.yourorg.cicd.Provenance.signImage(c, env.IMAGE)
          }
        }
      }

      stage('Deploy (Non-Prod)') {
        when {
          expression { return c.deployEnv != null && c.deployEnv.trim() != '' }
        }
        steps {
          script {
            org.yourorg.cicd.Deploy.deploy(c, env.IMAGE)
            org.yourorg.cicd.Deploy.smokeTest(c)
          }
        }
      }

      stage('Promote & Approve') {
        when {
          expression { return c.deployEnv?.toLowerCase() in ['prod','production'] }
        }
        steps {
          // 프로덕션은 승인 게이트(예: input step) + 변경관리 연동을 여기서 수행
          input message: "승인 필요: ${c.serviceName} 를 프로덕션에 배포하시겠습니까?", ok: "승인"
        }
      }

      stage('Deploy (Prod)') {
        when {
          expression { return c.deployEnv?.toLowerCase() in ['prod','production'] }
        }
        steps {
          script {
            org.yourorg.cicd.Deploy.deploy(c, env.IMAGE)
            org.yourorg.cicd.Deploy.verify(c)
          }
        }
      }

      stage('Post-Deploy') {
        steps {
          script {
            org.yourorg.cicd.Notify.publishDeploymentEvent(c, env.IMAGE)
          }
        }
      }
    }

    post {
      always {
        archiveArtifacts artifacts: 'reports/**', allowEmptyArchive: true
      }
      failure {
        script {
          org.yourorg.cicd.Notify.onFailure(c)
        }
      }
    }
  }
}
