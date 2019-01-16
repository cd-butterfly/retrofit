package com.bty.retrofit.demo.log.FloatingWindow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import com.bty.retrofit.demo.R;

public class DebugLogActivity extends AppCompatActivity implements LogManager.LogAddListener {

    private LogListAdapter logListAdapter = new LogListAdapter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_log_list_activity);
        ListView list = findViewById(R.id.list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        list.setAdapter(logListAdapter);
        LogManager.INSTANCE.setLogAddListener(this);
    }


    @Override
    public void onAddLog(OkHttpRequestLog log) {
        runOnUiThread(() -> {
                    logListAdapter.data.add(0, log);
                    logListAdapter.notifyDataSetChanged();
                }
        );
    }

    public void clear(View view) {
        runOnUiThread(() -> {
            logListAdapter.data.clear();
            LogManager.INSTANCE.data.clear();
            logListAdapter.notifyDataSetChanged();
        });
    }
}
