package com.example.administrator.mybehaviordemo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.mybehaviordemo.R;
import com.example.administrator.mybehaviordemo.utils.GifSizeFilter;
import com.example.administrator.mybehaviordemo.utils.GlideUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by 崔琦 on 2017/7/7 0007.
 * Describe : 图片选择器Matisse(知乎出品)
 */
public class MatisseActivityOne extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.grid_btn)
    Button gridBtn;
    @Bind(R.id.grid_view)
    GridView gridView;
    //照片返回请求码
    private static final int REQUEST_CODE_CHOOSE = 23;

    public GlideUtil glideUtil;
    public MyGridAdapter adapter;
    public List mData;//存放返回的图片
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_one);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("图片选择器Matisse");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        glideUtil = new GlideUtil();
        mData = new ArrayList();
        adapter = new MyGridAdapter();
        gridView.setAdapter(adapter);
    }

    @OnClick(R.id.grid_btn)
    public void gridBtn(){
        if (mData.size() >= 9){
            Toast.makeText(MatisseActivityOne.this, "上传图片不能超过9张", Toast.LENGTH_SHORT).show();
        }else {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean){
                                Matisse.from(MatisseActivityOne.this)
                                        .choose(MimeType.ofAll(),false)
                                        .countable(true)
                                        .capture(true)
                                        .captureStrategy(new CaptureStrategy(
                                                true,"com.example.administrator.mybehaviordemo.fileprovider"
                                        )).maxSelectable(9 - mData.size())
                                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine())
                                        .forResult(REQUEST_CODE_CHOOSE);
                                adapter.setData(null,null);
                            }else {
                                Toast.makeText(MatisseActivityOne.this, R.string.permission_request_denied, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK){
            adapter.setData(Matisse.obtainResult(data),Matisse.obtainPathResult(data));
            mData.addAll(Matisse.obtainResult(data));
        }
    }

    private class MyGridAdapter extends BaseAdapter{

        private List<Uri> mUris;
        private List<String> mPaths;
        void setData(List<Uri> uris,List<String> paths){
            mUris = uris;
            mPaths = paths;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,null);
                holder = new ViewHolder();
                holder.imgUrl = (ImageView)convertView.findViewById(R.id.img_url);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            glideUtil.attach(holder.imgUrl).injectImageWithNull(mData.get(position).toString());
            return convertView;
        }

        class ViewHolder{
            ImageView imgUrl;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
