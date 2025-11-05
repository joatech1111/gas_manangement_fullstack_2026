package com.joainfo.gas_management;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 제목 설정
        String title = getIntent().getStringExtra("title");
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }

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