#!/bin/bash
# webapp-runner 시작 스크립트

cd /home/gilza/gas_manangement_fullstack_2026

# 기존 프로세스가 있으면 종료
if [ -f app.pid ]; then
    OLD_PID=$(cat app.pid)
    if ps -p $OLD_PID > /dev/null 2>&1; then
        echo "Stopping existing process (PID: $OLD_PID)..."
        kill $OLD_PID
        sleep 2
    fi
    rm -f app.pid
fi

# webapp-runner 다운로드 (없으면)
if [ ! -f webapp-runner-9.0.27.1.jar ]; then
    echo "Downloading webapp-runner..."
    wget https://repo1.maven.org/maven2/com/github/jsimone/webapp-runner/9.0.27.1/webapp-runner-9.0.27.1.jar
fi

# 루트 컨텍스트(/)로 실행
echo "Starting webapp-runner on port 14007..."
nohup java -jar webapp-runner-9.0.27.1.jar --port 14007 target/kkkkk_m.war > app.log 2>&1 &

echo "Started with PID: $!"
echo $! > app.pid

echo ""
echo "✅ Server started successfully!"
echo "📍 URL: http://gas.joaoffice.com:14007/"
echo "📄 Logs: tail -f app.log"
