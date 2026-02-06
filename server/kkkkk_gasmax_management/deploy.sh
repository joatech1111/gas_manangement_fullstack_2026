#!/bin/bash

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 설정 (수정 필요)
REMOTE_HOST="ubuntu@YOUR_SERVER_IP"
REMOTE_DIR="~/gasmax-deploy"
REMOTE_PORT="14009"
LOCAL_PROJECT="/Users/gilzako/IdeaProjects/kkkkk_gasmax_management"

# 사용법 출력
usage() {
    echo "Usage: $0 [OPTIONS]"
    echo "Options:"
    echo "  -h HOST       Remote server host (default: ubuntu@YOUR_SERVER_IP)"
    echo "  -p PORT       Remote server port (default: 14009)"
    echo "  -d DIR        Remote deployment directory (default: ~/gasmax-deploy)"
    echo "  --help        Show this help message"
    exit 1
}

# 옵션 파싱
while [[ $# -gt 0 ]]; do
    case $1 in
        -h)
            REMOTE_HOST="$2"
            shift 2
            ;;
        -p)
            REMOTE_PORT="$2"
            shift 2
            ;;
        -d)
            REMOTE_DIR="$2"
            shift 2
            ;;
        --help)
            usage
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            usage
            ;;
    esac
done

# 설정 확인
if [ "$REMOTE_HOST" == "ubuntu@YOUR_SERVER_IP" ]; then
    echo -e "${YELLOW}⚠️  Warning: Using default remote host. Please set with -h option.${NC}"
    read -p "Enter remote host (e.g., ubuntu@192.168.1.100): " REMOTE_HOST
    if [ -z "$REMOTE_HOST" ]; then
        echo -e "${RED}❌ Remote host is required!${NC}"
        exit 1
    fi
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  GasMax Backend Deployment Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "📍 Remote Host: $REMOTE_HOST"
echo "📍 Remote Dir:  $REMOTE_DIR"
echo "📍 Port:        $REMOTE_PORT"
echo ""

# 1. 로컬 빌드
echo -e "${YELLOW}🔨 Step 1: Building WAR file...${NC}"
cd "$LOCAL_PROJECT" || exit 1

if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ pom.xml not found in $LOCAL_PROJECT${NC}"
    exit 1
fi

mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Maven build failed!${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Build successful!${NC}"
echo ""

# 2. WAR 파일 준비
echo -e "${YELLOW}📦 Step 2: Preparing deployment files...${NC}"
if [ ! -f "target/kkkkk_m.war" ]; then
    echo -e "${RED}❌ WAR file not found!${NC}"
    exit 1
fi

cp target/kkkkk_m.war kkkkk.war
echo -e "${GREEN}✅ WAR file prepared!${NC}"
echo ""

# 3. 원격 서버에 디렉토리 생성
echo -e "${YELLOW}📁 Step 3: Creating remote directory...${NC}"
ssh "$REMOTE_HOST" "mkdir -p $REMOTE_DIR"
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to create remote directory!${NC}"
    echo -e "${YELLOW}💡 Tip: Check SSH connection with: ssh $REMOTE_HOST${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Remote directory ready!${NC}"
echo ""

# 4. 파일 업로드
echo -e "${YELLOW}📤 Step 4: Uploading files to remote server...${NC}"
scp kkkkk.war "$REMOTE_HOST:$REMOTE_DIR/"
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Failed to upload WAR file!${NC}"
    exit 1
fi

scp Dockerfile "$REMOTE_HOST:$REMOTE_DIR/"
scp -r gasmax_db_config "$REMOTE_HOST:$REMOTE_DIR/"
echo -e "${GREEN}✅ Files uploaded!${NC}"
echo ""

# 5. 원격 서버에서 Docker 배포
echo -e "${YELLOW}🚀 Step 5: Deploying on remote server...${NC}"
ssh "$REMOTE_HOST" << EOF
set -e

cd $REMOTE_DIR

echo "🛑 Stopping existing container..."
docker stop gasmax-server 2>/dev/null || true
docker rm gasmax-server 2>/dev/null || true

echo "🏗️  Building Docker image..."
docker build -t gasmax-backend .

echo "🚀 Starting new container..."
docker run -d \
  --name gasmax-server \
  -p $REMOTE_PORT:8080 \
  -v \$(pwd)/gasmax_db_config:/gasmax_db_config \
  --restart unless-stopped \
  gasmax-backend

echo ""
echo "✅ Container started successfully!"
echo ""

# 컨테이너 상태 확인
docker ps | grep gasmax-server

# 잠시 대기 후 로그 확인
echo ""
echo "📋 Container logs (last 20 lines):"
sleep 3
docker logs --tail 20 gasmax-server
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  🎉 Deployment Successful!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo -e "🌐 Server URL: ${GREEN}http://${REMOTE_HOST#*@}:${REMOTE_PORT}${NC}"
    echo ""
    echo "📋 Useful commands:"
    echo "  - View logs:    ssh $REMOTE_HOST 'docker logs -f gasmax-server'"
    echo "  - Stop server:  ssh $REMOTE_HOST 'docker stop gasmax-server'"
    echo "  - Start server: ssh $REMOTE_HOST 'docker start gasmax-server'"
    echo "  - Restart:      ssh $REMOTE_HOST 'docker restart gasmax-server'"
    echo ""
else
    echo -e "${RED}❌ Deployment failed!${NC}"
    exit 1
fi
