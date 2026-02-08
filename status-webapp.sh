#!/bin/bash
# webapp-runner 상태 확인 스크립트

cd /home/gilza/gas_manangement_fullstack_2026

echo "🔍 webapp-runner Status"
echo "========================"

if [ -f app.pid ]; then
    PID=$(cat app.pid)
    if ps -p $PID > /dev/null 2>&1; then
        echo "✅ Running (PID: $PID)"
        echo ""
        echo "📊 Process Info:"
        ps -p $PID -o pid,user,%cpu,%mem,etime,command --no-headers
    else
        echo "❌ Not running (stale PID file)"
    fi
else
    PIDS=$(ps aux | grep webapp-runner | grep -v grep | awk '{print $2}')
    if [ -n "$PIDS" ]; then
        echo "✅ Running (PID: $PIDS) - no PID file"
    else
        echo "❌ Not running"
    fi
fi

echo ""
echo "📄 Last 5 log lines:"
echo "--------------------"
if [ -f app.log ]; then
    tail -5 app.log
else
    echo "(no log file)"
fi
