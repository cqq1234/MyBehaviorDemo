package com.example.administrator.mybehaviordemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mybehaviordemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 崔琦 on 2017/7/4 0004.
 * Describe : .....
 */
public class BBSFragment extends Fragment {
    @Bind(R.id.recycle_bbs)
    RecyclerView recycleBbs;
    //数据源
    private List mData;
    //适配器
    private MyRecycleAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        mData = new ArrayList();
        for (int i = 0; i < 20;i++){
            String str = "我是第" + i + "个帅哥";
            mData.add(i,str);
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleBbs.setLayoutManager(manager);
        adapter = new MyRecycleAdapter();
        recycleBbs.setAdapter(adapter);
    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.item_recycle,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mText.setText(mData.get(position).toString());
            holder.mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),RectProgressActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mText;
            public ViewHolder(View itemView) {
                super(itemView);
                mText = (TextView)itemView.findViewById(R.id.text1);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
