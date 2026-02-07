package com.joainfo.gas_management;

import com.getcapacitor.BridgeActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowCompat;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Build;
import android.content.res.Resources;
import android.graphics.Color;

public class MainActivity extends BridgeActivity {

    private static final int REQUEST_PHONE_NUMBER_PERMISSION = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(AppLauncherPlugin.class);
        registerPlugin(PhoneNumberPlugin.class);
        super.onCreate(savedInstanceState);
        requestPhoneNumberPermission();

        // ✅ SafeAreaView 처리 설정
        setupSafeAreaView();
        hideSystemUI(); // ✅ 시스템 UI 숨김

        // 화면 포커스 변경 시에도 다시 숨기기
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                hideSystemUI();
            }
        });
    }

    // ✅ SafeAreaView 처리 설정
    private void setupSafeAreaView() {
        Window window = getWindow();

        // Edge-to-edge 디스플레이 활성화
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false);
        }

        // 상태바 투명 처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 상태바 텍스트를 어둡게 (밝은 배경용)
                View decorView = window.getDecorView();
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        // ✅ WindowInsets 리스너 설정으로 SafeArea 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (view, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            int navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

            // WebView에 SafeArea 적용
            applySafeAreaToWebView(statusBarHeight, navigationBarHeight);

            return insets;
        });
    }

    // ✅ WebView에 SafeArea 적용
    private void applySafeAreaToWebView(int statusBarHeight, int navigationBarHeight) {
        runOnUiThread(() -> {
            try {
                WebView webView = getBridge().getWebView();
                if (webView != null) {
                    // CSS 변수로 SafeArea 값 설정
                    String script = String.format(
                        "document.documentElement.style.setProperty('--safe-area-inset-top', '%dpx');" +
                        "document.documentElement.style.setProperty('--safe-area-inset-bottom', '%dpx');" +
                        "document.documentElement.style.setProperty('--safe-area-inset-left', '0px');" +
                        "document.documentElement.style.setProperty('--safe-area-inset-right', '0px');" +
                        "document.body.style.paddingTop = '%dpx';" +
                        "document.body.style.paddingBottom = '%dpx';" +
                        // 헤더에 SafeArea 적용
                        "var headers = document.querySelectorAll('[data-role=\"header\"]');" +
                        "headers.forEach(function(header) {" +
                        "    header.style.paddingTop = '%dpx';" +
                        "    header.style.minHeight = 'calc(44px + %dpx)';" +
                        "});",
                        statusBarHeight, navigationBarHeight, statusBarHeight, navigationBarHeight,
                        statusBarHeight, statusBarHeight
                    );
                    webView.evaluateJavascript(script, null);

                    // ✅ 추가적으로 meta 태그도 설정
                    String metaScript =
                        "var metaViewport = document.querySelector('meta[name=\"viewport\"]');" +
                        "if (metaViewport) {" +
                        "    metaViewport.setAttribute('content', 'width=device-width, initial-scale=1.0, viewport-fit=cover');" +
                        "} else {" +
                        "    var meta = document.createElement('meta');" +
                        "    meta.name = 'viewport';" +
                        "    meta.content = 'width=device-width, initial-scale=1.0, viewport-fit=cover';" +
                        "    document.getElementsByTagName('head')[0].appendChild(meta);" +
                        "}";
                    webView.evaluateJavascript(metaScript, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ✅ 상태바 높이 계산 헬퍼 메소드
    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    // ✅ 네비게이션 바 높이 계산 헬퍼 메소드
    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void requestPhoneNumberPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_NUMBERS}, REQUEST_PHONE_NUMBER_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_NUMBER_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "전화번호 접근 권한이 허용되었습니다. 앱을 재시작합니다.", Toast.LENGTH_SHORT).show();
                restartApp();
            } else {
                Toast.makeText(this, "전화번호 접근 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void restartApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // ✅ 시스템 네비게이션 바 숨기기 (SafeArea 고려)
    private void hideSystemUI() {
        Window window = getWindow();
        View decorView = window.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }

        // ✅ 시스템 UI 숨긴 후에도 SafeArea 적용
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        applySafeAreaToWebView(statusBarHeight, navigationBarHeight);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ✅ 앱이 다시 활성화될 때 SafeArea 재적용
        setupSafeAreaView();
    }
}
