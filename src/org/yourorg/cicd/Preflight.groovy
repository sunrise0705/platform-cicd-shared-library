package org.yourorg.cicd

class Preflight {
  static void validate(Map c) {
    // Golden Path 강제: 필수 파라미터 및 정책 검증
    println "[Preflight] serviceName=${c.serviceName}, imageRepo=${c.imageRepo}"
    // 예: 브랜치 정책, 태깅 정책, 배포 환경 제한 등을 추가
  }
}
