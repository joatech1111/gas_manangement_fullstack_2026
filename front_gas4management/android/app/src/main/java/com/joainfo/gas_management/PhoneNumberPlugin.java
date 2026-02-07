package com.joainfo.gas_management;

import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.JSObject;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginCall;

@CapacitorPlugin(name = "PhoneNumber")
public class PhoneNumberPlugin extends Plugin {

    @PluginMethod
    public void getPhoneNumber(PluginCall call) {
        Context context = getContext();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
            call.reject("Permission not granted");
            return;
        }

        String phoneNumber = telephonyManager.getLine1Number();
        if (phoneNumber == null) {
            call.reject("Phone number not available");
            return;
        }

        JSObject result = new JSObject();
        result.put("value", phoneNumber);
        call.resolve(result);
    }
}
