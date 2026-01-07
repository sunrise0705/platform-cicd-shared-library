package org.yourorg.cicd

class Config {
  static Map normalize(Map cfg) {
    def required = ['serviceName','imageRepo']
    required.each { k ->
      if (!cfg.containsKey(k) || "${cfg[k]}".trim().isEmpty()) {
        throw new IllegalArgumentException("Missing required config: ${k}")
      }
    }
    return [
      serviceName: cfg.serviceName,
      imageRepo: cfg.imageRepo,
      dockerfilePath: cfg.get('dockerfilePath','Dockerfile'),
      deployEnv: cfg.get('deployEnv','staging'),
      qualityGateLevel: cfg.get('qualityGateLevel','strict'),
      deployStrategy: cfg.get('deployStrategy','rolling'),
      gitops: cfg.get('gitops', true)
    ]
  }
}
