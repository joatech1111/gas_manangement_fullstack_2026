#!/bin/bash

# 원격 서버에서 실행할 배포 스크립트
# 이 스크립트는 원격 서버에 업로드되어 실행됩니다

set -e

CONTAINER_NAME="gas-management-server"
IMAGE_NAME="gas-management:latest"

echo "=========================================="
echo "Gas Management Server 배포 시작"
echo "=========================================="

# 1. 기존 컨테이너 중지 및 제거
echo "[1/3] 기존 컨테이너 중지 및 제거..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm $CONTAINER_NAME 2>/dev/null || true

# 2. Docker 이미지 로드
echo "[2/3] Docker 이미지 로드 중..."
if [ -f "gas-management.tar.gz" ]; then
    docker load < gas-management.tar.gz
else
    echo "이미지 파일을 찾을 수 없습니다. docker-compose.yml을 사용합니다."
fi

# 3. Docker Compose로 컨테이너 시작
echo "[3/3] 컨테이너 시작 중..."
docker-compose -f docker-compose.yml up -d

# 4. 컨테이너 상태 확인
echo ""
echo "=========================================="
echo "컨테이너 상태:"
docker ps | grep $CONTAINER_NAME || echo "컨테이너가 실행되지 않았습니다."

echo ""
echo "=========================================="
echo "배포 완료!"
echo "=========================================="
echo "서버 로그 확인: docker logs -f $CONTAINER_NAME"
echo "컨테이너 중지: docker-compose -f docker-compose.yml down"
