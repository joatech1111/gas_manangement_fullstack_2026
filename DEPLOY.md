# Docker 배포 가이드

이 프로젝트는 Docker를 사용하여 원격 Ubuntu 서버에 배포할 수 있습니다.

## 사전 요구사항

### 로컬 환경
- Docker 설치
- Maven 설치
- SSH 접근 권한 (원격 배포 시)

### 원격 서버 (Ubuntu)
- Docker 설치
- Docker Compose 설치
- 포트 8080 개방

## 배포 방법

### 방법 1: 자동 배포 스크립트 사용 (권장)

```bash
# 로컬에서 실행
./deploy.sh [원격서버IP] [사용자명]

# 예시
./deploy.sh 192.168.1.100 ubuntu
```

스크립트가 자동으로:
1. Maven으로 WAR 파일 빌드
2. Docker 이미지 생성
3. 원격 서버로 전송 및 배포

### 방법 2: 수동 배포

#### 1단계: 로컬에서 빌드

```bash
# Maven 빌드
mvn clean package -DskipTests

# Docker 이미지 빌드
docker build -t gas-management:latest .
```

#### 2단계: 원격 서버로 전송

```bash
# 이미지를 tar로 저장
docker save gas-management:latest | gzip > gas-management.tar.gz

# 원격 서버로 전송
scp gas-management.tar.gz docker-compose.yml deploy-remote.sh ubuntu@서버IP:/tmp/
```

#### 3단계: 원격 서버에서 실행

```bash
# 원격 서버에 SSH 접속
ssh ubuntu@서버IP

# 이미지 로드
cd /tmp
docker load < gas-management.tar.gz

# 컨테이너 실행
docker-compose -f docker-compose.yml up -d
```

### 방법 3: Docker Compose만 사용 (로컬)

```bash
# 빌드 및 실행
docker-compose up -d --build

# 로그 확인
docker-compose logs -f

# 중지
docker-compose down
```

## 컨테이너 관리

### 로그 확인
```bash
docker logs -f gas-management-server
```

### 컨테이너 상태 확인
```bash
docker ps | grep gas-management
```

### 컨테이너 재시작
```bash
docker restart gas-management-server
```

### 컨테이너 중지 및 제거
```bash
docker-compose down
# 또는
docker stop gas-management-server
docker rm gas-management-server
```

## 접속 확인

배포 후 다음 URL로 접속하여 서버 상태를 확인할 수 있습니다:

```
http://서버IP:8080/kkkkk_m_war/gasapp/home.jsp
```

## 포트 변경

포트를 변경하려면 `docker-compose.yml` 파일을 수정하세요:

```yaml
ports:
  - "9093:8080"  # 호스트포트:컨테이너포트
```

## 환경 변수 설정

`docker-compose.yml`에서 환경 변수를 설정할 수 있습니다:

```yaml
environment:
  - JAVA_OPTS=-Xms512m -Xmx2048m
  - TZ=Asia/Seoul
```

## 문제 해결

### 컨테이너가 시작되지 않는 경우
```bash
# 로그 확인
docker logs gas-management-server

# 컨테이너 상태 확인
docker ps -a
```

### 포트가 이미 사용 중인 경우
```bash
# 포트 사용 확인
sudo netstat -tulpn | grep 8080

# docker-compose.yml에서 포트 변경
```

### 이미지 빌드 실패 시
```bash
# 캐시 없이 빌드
docker build --no-cache -t gas-management:latest .
```

## 원격 서버 Docker 설치 (Ubuntu)

원격 서버에 Docker가 설치되어 있지 않은 경우:

```bash
# Docker 설치
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 현재 사용자를 docker 그룹에 추가
sudo usermod -aG docker $USER

# Docker Compose 설치
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 재로그인 필요
```
