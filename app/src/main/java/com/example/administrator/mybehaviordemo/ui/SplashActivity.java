package com.example.administrator.mybehaviordemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.administrator.mybehaviordemo.MainActivity;
import com.example.administrator.mybehaviordemo.R;
import com.example.administrator.mybehaviordemo.view.ISplashView;

/**
 * Created by 崔琦 on 2017/7/10 0010.
 * Describe : 启动页
 */
public class SplashActivity extends Activity implements ISplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        },2000);
    }

    private void toMainActivity(){
        MainActivity.startActivity(this);
        overridePendingTransition(R.anim.screen_zoom_in,R.anim.screen_zoom_out);
        finish();
    }
    @Override
    public void onSplashInitData() {

    }
}
