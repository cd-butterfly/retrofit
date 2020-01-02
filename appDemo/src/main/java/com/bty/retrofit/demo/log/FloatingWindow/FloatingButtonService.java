package com.bty.retrofit.demo.log.FloatingWindow;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.bty.retrofit.demo.R;

/**
 * Created by duo.chen
 */

public class FloatingButtonService extends Service {

    public AppCompatImageButton button;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private int statusBarHeight;

    @Override
    public void onCreate() {
        super.onCreate();

        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = this.getResources().getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = this.getResources().getDimensionPixelOffset(R.dimen._20);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        layoutParams.width = getResources().getDimensionPixelOffset(R.dimen._48);
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen._48);
        layoutParams.x = 900;
        layoutParams.y = 120;

        showFloatingWindow();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (button != null) {
            windowManager.removeViewImmediate(button);
        }
        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (button == null) {
                    button = new AppCompatImageButton(getApplicationContext());
                    button.setElevation(30);
                    button.setScaleType(ImageView.ScaleType.CENTER);
                    button.setImageResource(R.drawable.ic_adb_black_24dp);
                    button.setBackgroundResource(R.drawable.btn_corner_dev);
                    button.setOnClickListener(v -> {
                        Intent intent = new Intent(FloatingButtonService.this, DebugLogActivity.class);
                        startActivity(intent);
                    });
                    button.setOnTouchListener(new FloatingOnTouchListener());
                    windowManager.addView(button, layoutParams);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    if (nowY <= statusBarHeight) {
                        nowY = y;
                    }
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
