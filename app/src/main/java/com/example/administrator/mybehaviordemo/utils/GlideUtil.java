package com.example.administrator.mybehaviordemo.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.mybehaviordemo.R;

/**
 * Created by 崔琦 on 2017/7/7 0007.
 * Describe : .....
 */
public class GlideUtil {
    ImageView imageView;
    private DiskCacheStrategy diskCache = DiskCacheStrategy.ALL;//磁盘缓存(缓存所有版本的图像)
    private boolean isSkipMemoryCache = true;//跳过内存缓存

    public GlideUtil attach(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }
    public GlideUtil injectImage(String url){
        Glide.with(imageView.getContext())//可以传入activity，fragment。
                .load(url)//加载完整的url
                .centerCrop()//图片会填充整个imageview
                .diskCacheStrategy(diskCache)//磁盘缓存
                .skipMemoryCache(isSkipMemoryCache)//内存缓存true为跳过，默认为false，全部缓存到内存。
                .placeholder(R.mipmap.ic_launcher)//加载图片时等待的图片(占位)
                .crossFade()//图像的平滑过渡
                .into(imageView);
        return this;
    }
    public GlideUtil injectImageWithNull(String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCache)
                .skipMemoryCache(false)//图片全部缓存到内存
                .placeholder(null)//加载图片时没有占位等待的图片
                .crossFade()
                .into(imageView);
        return this;
    }
}
