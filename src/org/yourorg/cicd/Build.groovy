package org.yourorg.cicd

class Build {
  static String buildImage(Map c) {
    def sha = System.getenv('GIT_COMMIT') ?: 'dev'
    def image = "${c.imageRepo}:${sha}"
    // 실제 환경에서는 Kaniko/BuildKit/Podman 등 표준화 가능
    def dockerfile = c.dockerfilePath
    println "[Build] docker build -f ${dockerfile} -t ${image} ."
    // Jenkins Pipeline step 실행은 vars/에서 sh(...)로 호출하는 방식도 가능하나,
    // 본 샘플은 구조 제시 목적이라 출력 중심으로 둡니다.
    return image
  }
}
