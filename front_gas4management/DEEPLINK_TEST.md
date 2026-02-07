# 딥링크 테스트 가이드

## 올바른 패키지명
실제 앱의 패키지명은 `com.joainfo.gas_management`입니다.

## Android 테스트 명령어

### 기본 딥링크 테스트
```bash
adb shell am start -a android.intent.action.VIEW -d "gasmanagement://" com.joainfo.gas_management
```

### 메인 페이지로 이동
```bash
adb shell am start -a android.intent.action.VIEW -d "gasmanagement://main" com.joainfo.gas_management
```

### 쿼리 파라미터 포함
```bash
adb shell am start -a android.intent.action.VIEW -d "gasmanagement://?page=main" com.joainfo.gas_management
```

### 고객 페이지로 이동 (예시)
```bash
adb shell am start -a android.intent.action.VIEW -d "gasmanagement://customer/123" com.joainfo.gas_management
```

## 테스트 스크립트 사용

```bash
# Android 테스트
./test-deeplink.sh android "gasmanagement://main"

# iOS 테스트
./test-deeplink.sh ios "gasmanagement://main"
```

## 로그 확인

### 실시간 로그 확인
```bash
adb logcat | grep -i "딥링크\|deeplink\|appUrlOpen\|Capacitor"
```

### 필터링된 로그
```bash
adb logcat -s "chromium:*" "Capacitor:*" | grep -i "딥링크\|appUrlOpen"
```

## 예상 결과

### 성공 시
- 앱이 실행되거나 포그라운드로 이동
- 콘솔에 "딥링크 수신:" 또는 "딥링크 처리 시작:" 로그 출력
- "Warning: Activity not started, its current task has been brought to the front" 메시지는 정상 (앱이 이미 실행 중일 때)

### 실패 시
- "Error: Activity not started, unable to resolve Intent" 에러 발생
- 해결 방법: 앱을 다시 빌드하고 설치

## 앱 재빌드 필요 시

```bash
# Capacitor 동기화
npx cap sync android

# Android Studio에서 빌드하거나
cd android && ./gradlew assembleDebug

# 또는 직접 설치
adb install -r android/app/build/outputs/apk/debug/app-debug.apk
```

## iOS 테스트

```bash
# 시뮬레이터
xcrun simctl openurl booted "gasmanagement://main"

# 실제 기기 (Safari에서)
# 주소창에 입력: gasmanagement://main
```


