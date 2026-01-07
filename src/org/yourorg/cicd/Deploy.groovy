package org.yourorg.cicd

class Deploy {
  static void deploy(Map c, String image) {
    // GitOps 권장: 배포는 선언형 변경(PR/commit)으로 수행
    if (c.gitops) {
      println "[Deploy] GitOps update for ${c.serviceName} -> ${image} (env=${c.deployEnv}, strategy=${c.deployStrategy})"
    } else {
      println "[Deploy] Direct deploy for ${c.serviceName} -> ${image} (env=${c.deployEnv}, strategy=${c.deployStrategy})"
    }
  }

  static void smokeTest(Map c) {
    println "[Deploy] Smoke test for ${c.serviceName} (env=${c.deployEnv})"
  }

  static void verify(Map c) {
    println "[Deploy] Verify prod rollout for ${c.serviceName} (strategy=${c.deployStrategy})"
  }
}
