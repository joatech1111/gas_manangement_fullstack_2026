#!/bin/bash

# 로컬에서 Docker로 테스트하는 스크립트

set -e

# 색상 정의
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}로컬 Docker 테스트 시작${NC}"
echo -e "${GREEN}========================================${NC}"

# 1. Maven 빌드
echo -e "${YELLOW}[1/3] Maven 빌드 중...${NC}"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Maven 빌드 실패!"
    exit 1
fi
echo -e "${GREEN}✓ Maven 빌드 완료${NC}"

# 2. 기존 컨테이너 중지 및 제거
echo -e "${YELLOW}[2/3] 기존 컨테이너 정리 중...${NC}"
docker-compose down 2>/dev/null || true
echo -e "${GREEN}✓ 컨테이너 정리 완료${NC}"

# 3. Docker 이미지 빌드 및 실행
echo -e "${YELLOW}[3/3] Docker 이미지 빌드 및 실행 중...${NC}"
docker-compose up -d --build

# 4. 컨테이너 상태 확인
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}컨테이너 상태:${NC}"
docker ps | grep gas-management-server || echo "컨테이너가 실행되지 않았습니다."

# 5. 로그 확인 (선택사항)
echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}서버 시작 중... (10초 대기)${NC}"
sleep 10

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}테스트 완료!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "서버 접속 URL:"
echo "  http://localhost:9093/kkkkk_m_war/gasapp/home.jsp"
echo ""
echo "명령어:"
echo "  로그 확인: docker-compose logs -f"
echo "  컨테이너 중지: docker-compose down"
echo "  컨테이너 재시작: docker-compose restart"
echo ""
