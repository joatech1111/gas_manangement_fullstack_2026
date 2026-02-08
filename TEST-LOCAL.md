# 로컬 Docker 테스트 가이드

로컬에서 Docker를 사용하여 서버를 테스트하는 방법입니다.

## 빠른 시작

### 방법 1: 자동 스크립트 사용 (권장)

```bash
./test-local.sh
```

이 스크립트가 자동으로:
1. Maven 빌드
2. Docker 이미지 생성
3. 컨테이너 실행

### 방법 2: 수동 실행

```bash
# 1. Maven 빌드
mvn clean package -DskipTests

# 2. Docker Compose로 빌드 및 실행
docker-compose up -d --build

# 3. 로그 확인
docker-compose logs -f
```

## 접속 URL

서버가 시작되면 다음 URL로 접속할 수 있습니다:

```
http://localhost:9093/kkkkk_m_war/gasapp/home.jsp
```

## 포트 변경하기

다른 포트로 테스트하려면 `docker-compose.yml` 파일을 수정하세요:

```yaml
ports:
  - "원하는포트:8080"  # 예: "9093:8080"
```

예시:
- `"9093:8080"` → 외부에서 9093 포트로 접속
- `"8080:8080"` → 외부에서 8080 포트로 접속

## 유용한 명령어

### 컨테이너 상태 확인
```bash
docker ps | grep gas-management
```

### 로그 확인
```bash
# 실시간 로그
docker-compose logs -f

# 최근 100줄 로그
docker-compose logs --tail=100
```

### 컨테이너 재시작
```bash
docker-compose restart
```

### 컨테이너 중지 및 제거
```bash
docker-compose down
```

### 컨테이너 내부 접속
```bash
docker exec -it gas-management-server bash
```

### 컨테이너 내부에서 Tomcat 로그 확인
```bash
docker exec gas-management-server tail -f /usr/local/tomcat/logs/catalina.out
```

## 문제 해결

### 포트가 이미 사용 중인 경우

```bash
# 포트 사용 확인
lsof -i :9093
# 또는
netstat -an | grep 9093

# docker-compose.yml에서 포트 변경
```

### 컨테이너가 시작되지 않는 경우

```bash
# 로그 확인
docker-compose logs

# 컨테이너 상태 확인
docker ps -a

# 이미지 확인
docker images | grep gas-management
```

### WAR 파일이 없는 경우

```bash
# Maven 빌드 확인
ls -la target/kkkkk_m.war

# 없으면 다시 빌드
mvn clean package -DskipTests
```

### 캐시 없이 다시 빌드

```bash
docker-compose build --no-cache
docker-compose up -d
```

## 테스트 체크리스트

- [ ] Maven 빌드 성공 (`target/kkkkk_m.war` 파일 생성)
- [ ] Docker 이미지 빌드 성공
- [ ] 컨테이너 실행 성공 (`docker ps`에서 확인)
- [ ] 브라우저에서 접속 가능
- [ ] 서버 상태 페이지 표시 확인

## 다음 단계

로컬 테스트가 성공하면 원격 서버에 배포할 수 있습니다:

```bash
./deploy.sh [원격서버IP] [사용자명]
```
