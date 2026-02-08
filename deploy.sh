#!/bin/bash

# Gas Management Server Docker 배포 스크립트
# 사용법: ./deploy.sh [remote_host] [remote_user]

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 변수 설정
REMOTE_HOST=${1:-""}
REMOTE_USER=${2:-"ubuntu"}
PROJECT_NAME="gas-management"
IMAGE_NAME="gas-management:latest"
CONTAINER_NAME="gas-management-server"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Gas Management Server 배포 시작${NC}"
echo -e "${GREEN}========================================${NC}"

# 1. Maven 빌드
echo -e "${YELLOW}[1/5] Maven 빌드 중...${NC}"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo -e "${RED}Maven 빌드 실패!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven 빌드 완료${NC}"

# 2. WAR 파일 확인
if [ ! -f "target/kkkkk_m.war" ]; then
    echo -e "${RED}WAR 파일을 찾을 수 없습니다: target/kkkkk_m.war${NC}"
    exit 1
fi
echo -e "${GREEN}✓ WAR 파일 확인: target/kkkkk_m.war${NC}"

# 3. Docker 이미지 빌드
echo -e "${YELLOW}[2/5] Docker 이미지 빌드 중...${NC}"
docker build -t $IMAGE_NAME .
if [ $? -ne 0 ]; then
    echo -e "${RED}Docker 이미지 빌드 실패!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker 이미지 빌드 완료${NC}"

# 4. 원격 서버로 배포
if [ -z "$REMOTE_HOST" ]; then
    echo -e "${YELLOW}[3/5] 로컬에서 Docker Compose로 실행...${NC}"
    docker-compose down
    docker-compose up -d
    echo -e "${GREEN}✓ 로컬 배포 완료${NC}"
    echo -e "${GREEN}서버 주소: http://localhost:8080/kkkkk_m_war/gasapp/home.jsp${NC}"
else
    echo -e "${YELLOW}[3/5] 원격 서버($REMOTE_HOST)로 배포 중...${NC}"
    
    # Docker 이미지를 tar로 저장
    echo -e "${YELLOW}[4/5] Docker 이미지 압축 중...${NC}"
    docker save $IMAGE_NAME | gzip > ${PROJECT_NAME}.tar.gz
    echo -e "${GREEN}✓ 이미지 압축 완료${NC}"
    
    # 원격 서버로 전송
    echo -e "${YELLOW}[5/5] 원격 서버로 전송 중...${NC}"
    scp ${PROJECT_NAME}.tar.gz docker-compose.yml deploy-remote.sh ${REMOTE_USER}@${REMOTE_HOST}:/tmp/
    
    # 원격 서버에서 실행
    ssh ${REMOTE_USER}@${REMOTE_HOST} << 'ENDSSH'
        cd /tmp
        echo "Docker 이미지 로드 중..."
        docker load < gas-management.tar.gz
        
        echo "기존 컨테이너 중지 및 제거..."
        docker-compose -f docker-compose.yml down || true
        
        echo "새 컨테이너 시작..."
        docker-compose -f docker-compose.yml up -d
        
        echo "배포 완료!"
        docker ps | grep gas-management-server
ENDSSH
    
    # 임시 파일 정리
    rm -f ${PROJECT_NAME}.tar.gz
    
    echo -e "${GREEN}✓ 원격 배포 완료${NC}"
    echo -e "${GREEN}서버 주소: http://${REMOTE_HOST}:8080/kkkkk_m_war/gasapp/home.jsp${NC}"
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}배포 완료!${NC}"
echo -e "${GREEN}========================================${NC}"
