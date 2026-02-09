# Agent Instructions

이 프로젝트는 React 기반의 애플리케이션 개발을 포함하고 있습니다.
**모든 응답과 설명은 반드시 한국어로 제공해야 합니다.**

## 프로젝트 구조 및 컨텍스트
- **React 프로젝트**: `react_new/` 디렉토리에 위치합니다.
- **레거시 모바일 앱**: `front_gas4management/` (jQuery Mobile + Capacitor)
- **백엔드**: Java Spring/Servlet 기반 (`src/main/java`)

## 규칙 (Rules)
1. **언어**: 사용자와의 모든 대화, 설명, 주석은 **한국어**를 사용합니다.
2. **프레임워크 식별**: 사용자가 "React 앱"이라고 지칭할 때, 실제로 `react_new`를 의미하는지 아니면 기존 `front_gas4management`를 React로 착각하거나 마이그레이션 하려는지 문맥을 확인합니다.
3. **라우팅**: React 프로젝트(`react_new`)의 라우팅은 `react-router-dom`을 사용하며, 모바일 뒤로가기 처리에 주의합니다.
