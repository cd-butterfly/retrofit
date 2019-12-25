package com.bty.retrofit.demo.log.FloatingWindow;

import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bty.retrofit.demo.R;

public class LogDetailActivity extends AppCompatActivity {

    private ClipboardManager clipboardManager;
    private OkHttpRequestLog log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_detail_layout);

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(name);
        log = (OkHttpRequestLog) getIntent().getSerializableExtra("log");

        TextView url = findViewById(R.id.detail_url);
        TextView method = findViewById(R.id.detail_method);
        TextView statusCode = findViewById(R.id.detail_status_code);
        TextView timestamp = findViewById(R.id.detail_timestamp);
        TextView requestHeader = findViewById(R.id.detail_request_header);
        TextView requestBody = findViewById(R.id.detail_request_body);
        TextView responseBody = findViewById(R.id.detail_response_body);
        TextView tookMs = findViewById(R.id.detail_tookms);

        url.setText(log.url);
        method.setText(log.method);
        statusCode.setText(log.statusCode);
        timestamp.setText(log.timeStamp);
        tookMs.setText(String.format("%sms", log.tookMs));
        requestHeader.setText(log.requestHeaders);
        findViewById(R.id.detail_request_body_layout)
                .setVisibility(TextUtils.isEmpty(log.requestBody) ? View.GONE : View.VISIBLE);
        requestBody.setText(log.requestBody);
        responseBody.setText(log.responseBody);
    }


    public void copyResponse(View view) {
        if (log != null) {
            try {
                clipboardManager.setText(log.responseBody);
                Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    public void copyRequest(View view) {
        if (log != null) {
            try {
                clipboardManager.setText(log.requestBody);
                Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
            }
        }
    }
}
