package org.yourorg.cicd

class Notify {
  static void publishDeploymentEvent(Map c, String image) {
    println "[Notify] Publish deployment event: ${c.serviceName} -> ${image} (env=${c.deployEnv})"
    // 예: Slack/Webhook/EventBus 연동
  }

  static void onFailure(Map c) {
    println "[Notify] Pipeline failed for ${c.serviceName}"
  }
}
