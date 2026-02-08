#!/bin/bash

# JSP 재컴파일 스크립트
# 사용법: ./recompile-jsp.sh [jsp-file-name]

echo "=== JSP 재컴파일 스크립트 ==="

# IntelliJ Tomcat work 디렉토리 찾기
TOMCAT_WORK_DIR=$(find ~/Library/Caches/JetBrains/IntelliJIdea*/tomcat* -name "work" -type d 2>/dev/null | head -1)

if [ -z "$TOMCAT_WORK_DIR" ]; then
    echo "❌ IntelliJ Tomcat work 디렉토리를 찾을 수 없습니다."
    echo "IntelliJ에서 Tomcat이 실행 중인지 확인하세요."
    exit 1
fi

echo "📁 Tomcat work 디렉토리: $TOMCAT_WORK_DIR"

# 특정 JSP 파일이 지정된 경우
if [ -n "$1" ]; then
    JSP_NAME="$1"
    echo "🎯 특정 JSP 파일 재컴파일: $JSP_NAME"
    
    # JSP 파일의 컴파일된 클래스 찾기 및 삭제
    find "$TOMCAT_WORK_DIR" -name "*${JSP_NAME//\//_}*.class" -type f -delete 2>/dev/null
    echo "✅ $JSP_NAME 관련 컴파일된 클래스 파일 삭제 완료"
    
    # JSP 파일 터치 (수정 시간 변경)
    JSP_FILE="src/main/webapp/gasapp/$JSP_NAME"
    if [ -f "$JSP_FILE" ]; then
        touch "$JSP_FILE"
        echo "✅ $JSP_FILE 파일 터치 완료"
    else
        echo "⚠️  JSP 파일을 찾을 수 없습니다: $JSP_FILE"
    fi
else
    # 모든 JSP 재컴파일
    echo "🔄 모든 JSP 파일 재컴파일 중..."
    
    # work 디렉토리 내의 모든 컴파일된 JSP 클래스 삭제
    find "$TOMCAT_WORK_DIR" -name "*_jsp.class" -type f -delete 2>/dev/null
    echo "✅ 모든 컴파일된 JSP 클래스 파일 삭제 완료"
    
    # 모든 JSP 파일 터치
    find src/main/webapp -name "*.jsp" -type f -exec touch {} \;
    echo "✅ 모든 JSP 파일 터치 완료"
fi

echo ""
echo "✅ 완료! 브라우저에서 JSP 페이지를 새로고침하면 자동으로 재컴파일됩니다."
echo "💡 팁: IntelliJ에서 Tomcat을 재시작하면 더 확실합니다."
