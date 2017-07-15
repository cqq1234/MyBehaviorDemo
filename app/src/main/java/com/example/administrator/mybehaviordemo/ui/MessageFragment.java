package com.example.administrator.mybehaviordemo.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mybehaviordemo.R;
import com.example.administrator.mybehaviordemo.view.SearchBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 崔琦 on 2017/7/4 0004.
 * Describe : .....
 */
public class MessageFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycle_message)
    RecyclerView recycleMessage;
    @Bind(R.id.searchBarView)
    SearchBarView searchBarView;
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    //添加一些假数据
    private List mData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mData = new ArrayList();
        ActionBar superActionBar = getActivity().getActionBar();
        if (superActionBar != null) {
            superActionBar.hide();
        }
        appBarLayout.addOnOffsetChangedListener(this);
        recycleMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleMessage.setVerticalScrollBarEnabled(true);
        recycleMessage.setNestedScrollingEnabled(false);
        recycleMessage.setAdapter(new MyAdapter());
        for (int i = 0;i < 25;i++){
            String str = "我是美女" + i + "号";
            mData.add(i,str);
        }
    }

    @OnClick(R.id.searchBarView)
    public void searchRecycle() {
        Toast.makeText(getActivity(), "enter search activity!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float alpha = Math.abs((float) verticalOffset / appBarLayout.getTotalScrollRange());
        toolbar.setAlpha(alpha);
        if (alpha >= 1) {
            searchBarView.startOpen();
        } else {
            searchBarView.startClose();
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycle,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mText.setText(mData.get(position).toString());
            holder.mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),MatisseActivityOne.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView mText;
            public ViewHolder(View itemView) {
                super(itemView);
                mText = (TextView)itemView.findViewById(R.id.text1);
            }
        }
    }
}
