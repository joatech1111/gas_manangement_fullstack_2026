#!/bin/bash
# webapp-runner 로그 보기 스크립트

cd /home/gilza/gas_manangement_fullstack_2026

if [ ! -f app.log ]; then
    echo "⚠️ No log file found (app.log)"
    exit 1
fi

echo "📄 Viewing logs (Ctrl+C to exit)..."
echo "============================================"
tail -f app.log
