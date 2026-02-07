package com.joainfo.gas_management;

import android.content.Intent;
import android.os.Bundle;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.content.ComponentName;

@CapacitorPlugin(name = "AppLauncher")
public class AppLauncherPlugin extends Plugin {

    @PluginMethod
    public void launchActivity(PluginCall call) {
        String url = call.getString("url");
        String title = call.getString("title");

        try {
            Intent intent = new Intent(getContext(), WebViewActivity.class); // ✅ WebViewActivity 직접 지정
            intent.putExtra("url", url);
            intent.putExtra("title", title);
            getContext().startActivity(intent);
            call.resolve();
        } catch (Exception e) {
            call.reject("Failed to launch activity: " + e.getMessage());
        }
    }
}
