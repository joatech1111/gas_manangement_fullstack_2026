# Ubuntu 원격 서버에서 14009 포트로 실행하기

## 🐳 방법 1: Docker 사용 (추천)

### 로컬 준비
```bash
cd /Users/gilzako/IdeaProjects/kkkkk_gasmax_management

# WAR 파일 빌드
mvn clean package

# WAR 파일 이름 변경
cp target/kkkkk_m.war kkkkk.war
```

### 원격 서버로 파일 전송
```bash
# 배포할 파일들을 원격 서버로 전송 (예: ubuntu@your-server-ip)
scp kkkkk.war ubuntu@YOUR_SERVER_IP:~/deploy/
scp Dockerfile ubuntu@YOUR_SERVER_IP:~/deploy/
scp -r gasmax_db_config ubuntu@YOUR_SERVER_IP:~/deploy/
```

### 원격 서버에서 실행
```bash
# 원격 서버 접속
ssh ubuntu@YOUR_SERVER_IP

# 배포 디렉토리로 이동
cd ~/deploy

# Docker 이미지 빌드
docker build -t gasmax-backend .

# 컨테이너 실행 (14009 포트)
docker run -d \
  --name gasmax-server \
  -p 14009:8080 \
  -v $(pwd)/gasmax_db_config:/gasmax_db_config \
  --restart unless-stopped \
  gasmax-backend

# 로그 확인
docker logs -f gasmax-server

# 상태 확인
docker ps

# 중지/시작/재시작
docker stop gasmax-server
docker start gasmax-server
docker restart gasmax-server
```

### 접속 테스트
```bash
# 서버에서 로컬 테스트
curl http://localhost:14009

# 외부에서 접속 테스트
curl http://YOUR_SERVER_IP:14009
```

---

## 🚀 방법 2: Tomcat 직접 설치

### 1. Java 및 Tomcat 설치
```bash
# Java 8 설치
sudo apt update
sudo apt install openjdk-8-jdk -y

# Tomcat 9 다운로드 및 설치
cd /opt
sudo wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.82/bin/apache-tomcat-9.0.82.tar.gz
sudo tar -xzf apache-tomcat-9.0.82.tar.gz
sudo mv apache-tomcat-9.0.82 tomcat9
sudo rm apache-tomcat-9.0.82.tar.gz
```

### 2. Tomcat 포트 변경 (8080 → 14009)
```bash
sudo nano /opt/tomcat9/conf/server.xml
```

다음 줄을 찾아서 수정:
```xml
<!-- 변경 전 -->
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />

<!-- 변경 후 -->
<Connector port="14009" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

### 3. WAR 파일 및 DB 설정 배포
```bash
# 기존 ROOT 앱 제거
sudo rm -rf /opt/tomcat9/webapps/ROOT

# WAR 파일을 ROOT.war로 복사
sudo cp ~/deploy/kkkkk_m.war /opt/tomcat9/webapps/ROOT.war

# DB 설정 디렉토리 복사
sudo mkdir -p /opt/gasmax_db_config
sudo cp -r ~/deploy/gasmax_db_config/* /opt/gasmax_db_config/

# db_config.properties 수정 (필요시)
sudo nano /opt/tomcat9/webapps/ROOT/WEB-INF/classes/db_config.properties
```

### 4. Tomcat 실행
```bash
# Tomcat 시작
sudo /opt/tomcat9/bin/startup.sh

# 로그 실시간 확인
tail -f /opt/tomcat9/logs/catalina.out

# Tomcat 중지
sudo /opt/tomcat9/bin/shutdown.sh
```

### 5. systemd 서비스 등록 (자동 시작)
```bash
sudo nano /etc/systemd/system/tomcat.service
```

다음 내용 입력:
```ini
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
Environment=CATALINA_PID=/opt/tomcat9/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat9
Environment=CATALINA_BASE=/opt/tomcat9

ExecStart=/opt/tomcat9/bin/startup.sh
ExecStop=/opt/tomcat9/bin/shutdown.sh

User=root
Group=root
UMask=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target
```

서비스 활성화 및 시작:
```bash
sudo systemctl daemon-reload
sudo systemctl enable tomcat
sudo systemctl start tomcat
sudo systemctl status tomcat

# 로그 확인
sudo journalctl -u tomcat -f
```

---

## 🔥 방법 3: 빠른 배포 스크립트

전체 과정을 자동화하는 스크립트입니다.

### deploy.sh 스크립트 생성
```bash
#!/bin/bash

# 설정
REMOTE_HOST="ubuntu@YOUR_SERVER_IP"
REMOTE_DIR="~/gasmax-deploy"
LOCAL_PROJECT="/Users/gilzako/IdeaProjects/kkkkk_gasmax_management"

echo "🔨 Building WAR file..."
cd $LOCAL_PROJECT
mvn clean package

echo "📦 Preparing deployment files..."
cp target/kkkkk_m.war kkkkk.war

echo "📤 Uploading to remote server..."
ssh $REMOTE_HOST "mkdir -p $REMOTE_DIR"
scp kkkkk.war $REMOTE_HOST:$REMOTE_DIR/
scp Dockerfile $REMOTE_HOST:$REMOTE_DIR/
scp -r gasmax_db_config $REMOTE_HOST:$REMOTE_DIR/

echo "🚀 Deploying on remote server..."
ssh $REMOTE_HOST << 'EOF'
cd ~/gasmax-deploy

# 기존 컨테이너 중지 및 제거
docker stop gasmax-server 2>/dev/null
docker rm gasmax-server 2>/dev/null

# 새 이미지 빌드
docker build -t gasmax-backend .

# 컨테이너 실행
docker run -d \
  --name gasmax-server \
  -p 14009:8080 \
  -v $(pwd)/gasmax_db_config:/gasmax_db_config \
  --restart unless-stopped \
  gasmax-backend

echo "✅ Deployment complete!"
docker ps | grep gasmax-server
EOF

echo "🎉 Done! Server running on http://YOUR_SERVER_IP:14009"
```

스크립트 실행:
```bash
chmod +x deploy.sh
./deploy.sh
```

---

## 🔍 문제 해결

### 포트가 이미 사용 중인 경우
```bash
# 14009 포트 사용 중인 프로세스 확인
sudo lsof -i :14009
sudo netstat -tulpn | grep 14009

# 프로세스 종료
sudo kill -9 <PID>
```

### 방화벽 설정 (UFW)
```bash
# 14009 포트 열기
sudo ufw allow 14009/tcp
sudo ufw reload
sudo ufw status
```

### 로그 확인
```bash
# Docker 로그
docker logs -f gasmax-server

# Tomcat 로그
tail -f /opt/tomcat9/logs/catalina.out
tail -f /opt/tomcat9/logs/localhost.*.log
```

### 애플리케이션 로그 확인
```bash
# Docker 컨테이너 내부 접속
docker exec -it gasmax-server bash

# Tomcat 로그 위치
cd /usr/local/tomcat/logs
ls -lah
```

---

## ✅ 접속 테스트

```bash
# 로컬에서 테스트
curl http://localhost:14009

# 원격에서 테스트
curl http://YOUR_SERVER_IP:14009

# 헬스체크 (앱에 따라 다름)
curl http://YOUR_SERVER_IP:14009/gasapp/
```

---

## 📝 참고사항

1. **DB 연결 설정 확인**
   - `/opt/gasmax_db_config/` 또는 Docker 볼륨 마운트 경로 확인
   - `db_config.properties`에서 Ubuntu 환경에 맞는 경로 설정 필요

2. **CORS 설정**
   - 프론트엔드 앱에서 접속하려면 CORS 설정이 필요할 수 있습니다
   - `web.xml`에 CORS 필터가 이미 설정되어 있는지 확인

3. **메모리 설정**
   - Docker 또는 Tomcat의 JVM 메모리 설정 조정 필요시:
   ```bash
   # Docker
   docker run -d \
     --name gasmax-server \
     -p 14009:8080 \
     -e JAVA_OPTS="-Xms512m -Xmx2048m" \
     gasmax-backend
   
   # Tomcat (catalina.sh 수정)
   export JAVA_OPTS="-Xms512m -Xmx2048m"
   ```

4. **SSL/HTTPS 설정**
   - 프로덕션 환경에서는 Nginx 리버스 프록시 사용 권장
   - Let's Encrypt로 무료 SSL 인증서 발급 가능
