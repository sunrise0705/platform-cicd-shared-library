package org.yourorg.cicd

class Security {
  static void runSast(Map c) {
    println "[Security] SAST 실행 (예: Semgrep/CodeQL)"
    // strict 모드에서는 High/Critical 실패 처리 등 정책 반영
  }
  static void runSca(Map c) {
    println "[Security] SCA/License 검사 (예: Trivy fs, osv-scanner)"
  }
  static void runSecretScan(Map c) {
    println "[Security] Secret Scan (예: gitleaks)"
  }
  static void runImageScan(Map c, String image) {
    println "[Security] Image Scan: ${image} (예: Trivy image)"
  }
}
