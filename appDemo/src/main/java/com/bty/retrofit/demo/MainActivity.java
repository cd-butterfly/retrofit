package com.bty.retrofit.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityPresenter = new MainActivityPresenter(this);

    }

    public void download(View view){
        mainActivityPresenter.download();
    }

    public void request(View view) {
        mainActivityPresenter.request();
    }
}
