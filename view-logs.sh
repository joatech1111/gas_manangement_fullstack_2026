#!/bin/bash

# Gas Management Server 로그 보기 스크립트

# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Gas Management Server 로그 보기${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "사용법:"
echo "  $0 [옵션]"
echo ""
echo "옵션:"
echo "  -f, --follow     실시간 로그 보기 (기본값)"
echo "  -t, --tail N     최근 N줄만 보기 (기본: 100)"
echo "  -a, --all        모든 로그 보기"
echo "  -c, --catalina   Tomcat catalina.out 로그만 보기"
echo "  -l, --local      로컬 로그 파일 보기 (./logs/)"
echo "  -h, --help       도움말"
echo ""

# 기본값
FOLLOW=true
TAIL_LINES=100
SHOW_ALL=false
SHOW_CATALINA=false
SHOW_LOCAL=false

# 인자 파싱
while [[ $# -gt 0 ]]; do
    case $1 in
        -f|--follow)
            FOLLOW=true
            shift
            ;;
        -t|--tail)
            TAIL_LINES="$2"
            FOLLOW=false
            shift 2
            ;;
        -a|--all)
            SHOW_ALL=true
            FOLLOW=false
            shift
            ;;
        -c|--catalina)
            SHOW_CATALINA=true
            FOLLOW=false
            shift
            ;;
        -l|--local)
            SHOW_LOCAL=true
            shift
            ;;
        -h|--help)
            exit 0
            ;;
        *)
            echo "알 수 없는 옵션: $1"
            exit 1
            ;;
    esac
done

# 로컬 로그 파일 보기
if [ "$SHOW_LOCAL" = true ]; then
    if [ -d "./logs" ]; then
        echo -e "${BLUE}로컬 로그 파일 목록:${NC}"
        ls -lh ./logs/
        echo ""
        echo -e "${BLUE}최근 로그 파일 보기:${NC}"
        if [ -f "./logs/catalina.out" ]; then
            tail -f ./logs/catalina.out
        else
            echo "로그 파일이 없습니다."
        fi
        exit 0
    else
        echo "logs 디렉토리가 없습니다."
        exit 1
    fi
fi

# 컨테이너 상태 확인
if ! docker ps | grep -q gas-management-server; then
    echo -e "${YELLOW}경고: gas-management-server 컨테이너가 실행 중이 아닙니다.${NC}"
    echo "컨테이너 시작: docker-compose up -d"
    exit 1
fi

# Tomcat catalina.out 로그만 보기
if [ "$SHOW_CATALINA" = true ]; then
    echo -e "${BLUE}Tomcat catalina.out 로그:${NC}"
    docker exec gas-management-server tail -f /usr/local/tomcat/logs/catalina.out
    exit 0
fi

# Docker Compose 로그 보기
echo -e "${BLUE}Docker Compose 로그:${NC}"
if [ "$SHOW_ALL" = true ]; then
    docker-compose logs
elif [ "$FOLLOW" = true ]; then
    docker-compose logs -f
else
    docker-compose logs --tail=$TAIL_LINES
fi
