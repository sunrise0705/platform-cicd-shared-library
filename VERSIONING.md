# Versioning Policy

- 본 Shared Library는 **SemVer**를 권장합니다.
- `vMAJOR.MINOR.PATCH`
  - MAJOR: Stage 순서/필수 보안 게이트 변경 등 Breaking change
  - MINOR: 기능 추가(기본값 유지)
  - PATCH: 버그 수정/로그 개선 등

## 서비스 적용 원칙
- 서비스 Jenkinsfile은 항상 명시적으로 버전을 고정합니다.
  - 예: `@Library('platform-cicd@v1.2.0') _`
- 조직 정책상 중앙 강제 업그레이드가 필요한 경우,
  - `v1` 브랜치를 유지하고 `v1.x.y` 태그로 릴리스 관리하는 방식을 권장합니다.
