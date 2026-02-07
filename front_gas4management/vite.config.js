import { defineConfig } from 'vite';

export default defineConfig({
    root: 'www',
    build: {
        sourcemap: false
    },
    logLevel: 'error', // 'info' 대신 'error'로 변경
    server: {
        host: '0.0.0.0', // 모든 IP에서 접근 가능
        open: true,
        port: 3000,      // 포트 설정
    },
});
