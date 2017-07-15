package com.example.administrator.mybehaviordemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.administrator.mybehaviordemo.R;
import com.example.administrator.mybehaviordemo.view.RectProgress;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 崔琦 on 2017/7/7 0007.
 * Describe : .....
 */
public class RectProgressActivity extends AppCompatActivity {
    @Bind(R.id.rectProgress_main)
    RectProgress rectProgressMain;
    @Bind(R.id.rectProgress_main1)
    RectProgress rectProgressMain1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectprogress);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        rectProgressMain.setChangedListener(new RectProgress.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int currentValue, int percent) {
                Log.d("Main", "==onProgressChanged: " + currentValue);
                Log.d("Main", "==percent: " + percent);
            }
        });
    }
    public void setProgress(View view){
        rectProgressMain.setProgress((int) (Math.random() * 100));
        rectProgressMain1.setProgress((int) (Math.random() * 100));
    }
}
