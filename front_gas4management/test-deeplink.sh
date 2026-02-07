#!/bin/bash

# 딥링크 테스트 스크립트
# 사용법: ./test-deeplink.sh [platform] [url]
# 예시: ./test-deeplink.sh android "gasmanagement://main"

PLATFORM=${1:-android}
URL=${2:-"gasmanagement://"}

echo "딥링크 테스트 시작..."
echo "플랫폼: $PLATFORM"
echo "URL: $URL"
echo ""

if [ "$PLATFORM" = "android" ]; then
    echo "Android 기기/에뮬레이터에서 딥링크 실행 중..."
    # 올바른 패키지명: com.joainfo.gas_management
    adb shell am start -a android.intent.action.VIEW -d "$URL" com.joainfo.gas_management
    echo "✅ 딥링크 전송 완료"
    echo ""
    echo "앱이 실행되고 딥링크를 처리하는지 확인하세요."
    echo "로그 확인: adb logcat | grep -i '딥링크\|deeplink\|appUrlOpen'"
    
elif [ "$PLATFORM" = "ios" ]; then
    echo "iOS 시뮬레이터에서 딥링크 실행 중..."
    xcrun simctl openurl booted "$URL"
    echo "✅ 딥링크 전송 완료"
    echo ""
    echo "앱이 실행되고 딥링크를 처리하는지 확인하세요."
    echo "로그 확인: Console.app에서 앱 로그 확인"
    
else
    echo "❌ 잘못된 플랫폼입니다. 'android' 또는 'ios'를 입력하세요."
    exit 1
fi


