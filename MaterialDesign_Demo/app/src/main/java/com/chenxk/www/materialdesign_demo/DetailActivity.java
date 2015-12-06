package com.chenxk.www.materialdesign_demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 详情界面
 *
 * @author chenxiangkong
 */
public class DetailActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinatorLayout;
    private NewsBean mNewsBean;
    private ImageView ivImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取列表项传递过来的实体数据
        Intent intent = getIntent();
        mNewsBean = (NewsBean) intent.getSerializableExtra("news");
        int pos = intent.getIntExtra("pos", 0);
        int layout = pos < 5 ? R.layout.activity_detail2 : R.layout.activity_detail;

        setContentView(layout);

        ivImage = (ImageView) findViewById(R.id.iv_news_pic);
        ivImage.setImageResource(mNewsBean.newsImage);
        // 初始化工具栏
        initToolBar();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.cl_root);
        initFAB();
    }


    private void initFAB() {
        //初始化浮动操作按钮控件
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_01);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar(mNewsBean.newsTitle);
            }
        });
    }

    /**
     * 初始化标题栏
     *
     * @return
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mNewsBean.newsTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.onBackPressed();
            }
        });
    }

    private void showSnackBar(String title) {
        // 修改样式提示
        final Snackbar sBar = Snackbar.make(mCoordinatorLayout,
                title, Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) sBar.getView();
        TextView tv = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        sBar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sBar.dismiss();
            }
        });
        sBar.show();
    }
}
