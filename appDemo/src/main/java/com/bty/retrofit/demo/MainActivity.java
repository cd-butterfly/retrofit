package com.bty.retrofit.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bty.retrofit.demo.log.FloatingWindow.FloatingButtonService;


public class MainActivity extends AppCompatActivity {

    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityPresenter = new MainActivityPresenter(this);
    }

    public void testdownload(View view) {
        mainActivityPresenter.testdownload();
    }

    public void testGet(View view) {
        mainActivityPresenter.testGet();
    }

    public void testPost(View view) {
        mainActivityPresenter.testPost();
    }

    public void startFloatingwindow(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(new Intent(MainActivity.this, FloatingButtonService.class));
        }
    }
}
