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

// config.server = {
//     //url: "http://192.168.0.9:3000", //home localhost
//     url: "http://192.168.0.72:3000", //office localhost
//     cleartext: true
// };

export default config;




