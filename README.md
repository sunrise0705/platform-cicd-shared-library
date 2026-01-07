# platform-cicd-shared-library (SSoT for Jenkins)

본 저장소는 Jenkins **Shared Library** 기반의 **Single Source of Truth(SSoT)** 역할을 수행합니다.
서비스 저장소의 Jenkinsfile은 파이프라인 로직을 소유하지 않고, 본 라이브러리의 표준 엔트리포인트(`standardCicd`)를 호출합니다.

## 목표
- Golden Path(권장 경로) 제공: 대부분 서비스가 가장 빠르고 안전하게 프로덕션으로 가는 기본 파이프라인
- Self-Service: 서비스 팀은 최소 파라미터만 제공(서비스명/이미지/배포환경 등)
- DevSecOps by default: SAST/SCA/Secret/Image Scan을 표준 단계로 강제

## Jenkins 설정(필수)
1) Jenkins > **Manage Jenkins** > **Configure System** > **Global Pipeline Libraries**
- Name: `platform-cicd`
- Default version: `v1` (권장: 태그/브랜치로 고정)
- Retrieval method: Git
- Project Repository: (본 저장소 URL)
- Load implicitly: (선택) 조직 표준이면 체크 권장

2) 서비스 Jenkinsfile에서 사용 예:
```groovy
@Library('platform-cicd@v1') _
standardCicd(
  serviceName: 'orders',
  imageRepo: 'registry.yourorg.com/orders',
  deployEnv: 'staging'
)
```

## 디렉터리 구조
- `vars/` : 서비스 Jenkinsfile에서 호출하는 공개 API(엔트리포인트)
- `src/`  : 내부 구현(스텝/유틸)
- `resources/` : 템플릿 리소스(예: Kubernetes 매니페스트 스니펫 등)

## 버전 정책
- SemVer 권장: `vMAJOR.MINOR.PATCH`
- Breaking change는 Major에서만 수행
