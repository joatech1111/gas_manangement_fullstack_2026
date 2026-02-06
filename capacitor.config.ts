import {CapacitorConfig} from '@capacitor/cli';

const config: CapacitorConfig = {
    appId: "com.joainfo.gas_management_2025",
    appName: "gas_management_2025",
    webDir: "www",
    bundledWebRuntime: false,
    plugins: {
        SplashScreen: {
            launchShowDuration: 0,
            autoHide: true
        }
    },
    android: {
        webContentsDebuggingEnabled: true,
        allowMixedContent: true
    },
    ios: {
        scheme: "gasmanagement"
    }
};

// 라이브리로드(개발용): 환경변수로만 활성화
// 예) CAPACITOR_LIVE_RELOAD_URL="http://192.168.0.9:3000" npx cap sync
const liveReloadUrl = process.env.CAPACITOR_LIVE_RELOAD_URL;
if (liveReloadUrl) {
    config.server = {
        url: liveReloadUrl,
        cleartext: true,
    };
}

export default config;




