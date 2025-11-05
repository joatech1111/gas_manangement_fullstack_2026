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

};

config.server = {
    //url: "http://192.168.0.10:3000", //home localhost
    url: "http://192.168.3.38:3000", //office localhost
    cleartext: true
};

export default config;




