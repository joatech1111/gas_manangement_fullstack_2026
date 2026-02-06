package com.joainfo.gas_management;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.view.Window;
import android.graphics.Color;
import android.os.Build;
import androidx.core.view.WindowCompat;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // SafeArea 처리 설정
        setupSafeAreaView();
        
        setContentView(R.layout.activity_webview);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 제목 설정
        String title = getIntent().getStringExtra("title");
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }

        // Toolbar에 SafeArea 적용
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (view, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            view.setPadding(
                view.getPaddingLeft(),
                statusBarHeight,
                view.getPaddingRight(),
                view.getPaddingBottom()
            );
            return insets;
        });

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // 🚀 WebView가 Chrome이 아닌 앱 내부에서 열리도록 설정
        webView.setWebViewClient(new WebViewClient());  // ✅ 추가
        webView.setWebChromeClient(new WebChromeClient());

        // URL 로드
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            webView.loadUrl(url);
        }
    }
    
    // SafeArea 처리 설정
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
    }

    // ✅ 🔙 상단의 툴바(백버튼) 동작 수정
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // ✅ 🔙 하드웨어 백버튼 처리
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();  // 🔙 웹뷰 내에서 뒤로 가기 수행
        } else {
            super.onBackPressed();  // ❌ 더 이상 뒤로 갈 페이지가 없으면 앱의 이전 화면으로 이동
        }
    }
}