package com.example.administrator.mybehaviordemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mybehaviordemo.ui.BBSFragment;
import com.example.administrator.mybehaviordemo.ui.MessageFragment;
import com.example.administrator.mybehaviordemo.ui.OurFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.img1_bottom_main)
    ImageView img1BottomMain;
    @Bind(R.id.text1_bottom_main)
    TextView text1BottomMain;
    @Bind(R.id.bottom_layout_one)
    LinearLayout bottomLayoutOne;
    @Bind(R.id.img2_bottom_main)
    ImageView img2BottomMain;
    @Bind(R.id.text2_bottom_main)
    TextView text2BottomMain;
    @Bind(R.id.bottom_layout_two)
    RelativeLayout bottomLayoutTwo;
    @Bind(R.id.img3_bottom_main)
    ImageView img3BottomMain;
    @Bind(R.id.text3_bottom_main)
    TextView text3BottomMain;
    @Bind(R.id.bottom_layout_three)
    LinearLayout bottomLayoutThree;
    //FragmentManager
    private FragmentManager mFragmentManager;
    //要切换的fragment
    private Fragment mFragment;
    private FragmentTransaction mTransaction;

    private BBSFragment bbsFragment;
    private MessageFragment messageFragment;
    private OurFragment ourFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mFragmentManager = getSupportFragmentManager();
        //默认首先显示第一个Fragment
        mFragment = new BBSFragment();
        mFragmentManager.beginTransaction().add(R.id.layFrame, mFragment, "bottom1").commit();
        bottomLayoutOne.setSelected(true);
        img1BottomMain.setSelected(true);
        text1BottomMain.setSelected(true);
    }
    @OnClick({R.id.bottom_layout_one,R.id.bottom_layout_two,R.id.bottom_layout_three})
    public void onClick(View v){
        //如果已经选中不再改变
        if (v.isSelected())
            return;
        bottomLayoutOne.setSelected(false);
        bottomLayoutTwo.setSelected(false);
        bottomLayoutThree.setSelected(false);
        img1BottomMain.setSelected(false);
        img2BottomMain.setSelected(false);
        img3BottomMain.setSelected(false);
        text1BottomMain.setSelected(false);
        text2BottomMain.setSelected(false);
        text3BottomMain.setSelected(false);
        mTransaction = mFragmentManager.beginTransaction();
        //要添加的新的fragment
        Fragment frag;
        //已添加的
        Fragment mFr;
        switch (v.getId()){
            case R.id.bottom_layout_one:
                //设置选中状态
                bottomLayoutOne.setSelected(true);
                img1BottomMain.setSelected(true);
                text1BottomMain.setSelected(true);
                //切换到第一页
                mFr = mFragmentManager.findFragmentByTag("bottom1");
                frag = new BBSFragment();
                switchFragment(mFr,frag,"bottom1");
                break;
            case R.id.bottom_layout_two:
                //设置选中状态
                bottomLayoutTwo.setSelected(true);
                img2BottomMain.setSelected(true);
                text2BottomMain.setSelected(true);
                //切换到第二页
                mFr = mFragmentManager.findFragmentByTag("bottom2");
                frag = new MessageFragment();
                switchFragment(mFr,frag,"bottom2");
                break;
            case R.id.bottom_layout_three:
                //设置选中状态
                bottomLayoutThree.setSelected(true);
                img3BottomMain.setSelected(true);
                text3BottomMain.setSelected(true);
                //切换到第三页
                mFr = mFragmentManager.findFragmentByTag("bottom3");
                frag = new OurFragment();
                switchFragment(mFr,frag,"bottom3");
                break;
        }
    }

    /**
     * 同一显示隐藏所有的Fragment
     * */
    private void switchFragment(Fragment mFr,Fragment frag,String tag) {
        //隐藏其他的Fragment
        Fragment b1 = mFragmentManager.findFragmentByTag("bottom1");
        if (b1 != null) {
            if (b1.isAdded())
                mTransaction.hide(b1);
        }
        Fragment b2 = mFragmentManager.findFragmentByTag("bottom2");
        if (b2 != null) {
            if (b2.isAdded())
                mTransaction.hide(b2);
        }
        Fragment b3 = mFragmentManager.findFragmentByTag("bottom3");
        if (b3 != null) {
            if (b3.isAdded())
                mTransaction.hide(b3);
        }
        if (mFr == null){
            mTransaction.add(R.id.layFrame, frag, tag);
        } else {
            mTransaction.show(mFr);
        }
        mTransaction.commit();
    }

    /**
     * 启动页跳转
     * */
    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
}
