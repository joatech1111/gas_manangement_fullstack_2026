#!/bin/bash
# webapp-runner 종료 스크립트

cd /home/gilza/gas_manangement_fullstack_2026

if [ -f app.pid ]; then
    PID=$(cat app.pid)
    if ps -p $PID > /dev/null 2>&1; then
        echo "Stopping process (PID: $PID)..."
        kill $PID
        sleep 2
        
        # 강제 종료 (아직 살아있으면)
        if ps -p $PID > /dev/null 2>&1; then
            echo "Force killing..."
            kill -9 $PID
        fi
        
        echo "✅ Stopped successfully!"
    else
        echo "⚠️ Process not running"
    fi
    rm -f app.pid
else
    echo "⚠️ No PID file found. Checking for running process..."
    PIDS=$(ps aux | grep webapp-runner | grep -v grep | awk '{print $2}')
    if [ -n "$PIDS" ]; then
        echo "Found running processes: $PIDS"
        kill $PIDS
        echo "✅ Stopped"
    else
        echo "No webapp-runner process found"
    fi
fi
