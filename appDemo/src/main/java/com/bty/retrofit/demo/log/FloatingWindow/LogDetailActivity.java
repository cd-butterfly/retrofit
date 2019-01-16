package com.bty.retrofit.demo.log.FloatingWindow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.bty.retrofit.demo.R;

public class LogDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_detail_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> LogDetailActivity.this.finish());

        OkHttpRequestLog log = (OkHttpRequestLog) getIntent().getSerializableExtra("log");

        TextView url = findViewById(R.id.detail_url);
        TextView method = findViewById(R.id.detail_method);
        TextView statusCode = findViewById(R.id.detail_status_code);
        TextView timestamp = findViewById(R.id.detail_timestamp);
        TextView requestHeader = findViewById(R.id.detail_request_header);
        TextView requestBody = findViewById(R.id.detail_request_body);
        TextView responseBody = findViewById(R.id.detail_response_body);

        url.setText(log.url);
        method.setText(log.method);
        statusCode.setText(log.statusCode);
        timestamp.setText(log.timeStamp);
        requestHeader.setText(log.requestHeaders);
        findViewById(R.id.detail_request_body_layout)
                .setVisibility(TextUtils.isEmpty(log.requestBody) ? View.GONE : View.VISIBLE);
        requestBody.setText(log.requestBody);
        responseBody.setText(log.responseBody);
    }


}
