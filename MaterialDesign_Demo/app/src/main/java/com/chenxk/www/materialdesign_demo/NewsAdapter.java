package com.chenxk.www.materialdesign_demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
public class NewsAdapter extends RecyclerView.Adapter {

    private List<NewsBean> mDatas = new ArrayList<NewsBean>();
    private MainActivity mAct;
    private boolean isStaggeredLayout = false;

    public NewsAdapter(MainActivity act, boolean isStaggeredLayout) {
        this.isStaggeredLayout = isStaggeredLayout;
        this.mAct = act;
    }

    public void setmDatas(List<NewsBean> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged(); // 刷新界面数据
    }

    // 当RecyclerView需要一个ViewHolder时会回调该方法，如果有可复用的View则该方法不会得倒回调
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        int layout = isStaggeredLayout? R.layout.item_news_staggered : R.layout.item_news;
        View itemView = LayoutInflater.from(mAct).inflate(layout, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    // 当一个View需要出现在屏幕上时，该方法会被回调，你需要在该方法中根据数据来更改视图
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ((MyViewHolder) viewHolder).refreshViews(mDatas.get(i), i);
    }

    // 用于告诉RecyclerView有多个视图需要显示
    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            name = (TextView) itemView.findViewById(R.id.tv_title);
            image = (ImageView) itemView.findViewById(R.id.iv_image);
        }

        public void refreshViews(final NewsBean bean, final int pos) {
            name.setText(bean.newsTitle);
            image.setBackgroundResource(bean.newsImage);
            // 使用RecyclerView时没有列表项setOnItemClickListener()点击事件，
            // 可以通过这种方式处理
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAct.animateActivity(bean, itemView, pos);
                }
            });
        }
    }
}
