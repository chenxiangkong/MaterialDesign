package com.chenxk.www.materialdesign_demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 *
 * @author chenxiangkong
 */
public class MainActivity extends AppCompatActivity {
    // 抽屉控件
    private DrawerLayout mDrawerLayout;
    // 抽屉切换按钮
    private ActionBarDrawerToggle mToggle;
    // 下拉刷新控件
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressBar mProgressBar;
    // 列表项数据
    private List<NewsBean> mDatas = new ArrayList<NewsBean>();
    // 列表： 可回收的视图
    private RecyclerView mRecyclerView;
    // 列表适配器
    private NewsAdapter mAdapter;

    private FloatingActionButton fabButton;

    // 是否是瀑布流控件
    private boolean isStaggeredLayout = false;

    private TextView tvToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadData();
    }

    // 初始化视图
    private void initViews() {
        initActionBar();
        tvToggle = (TextView) findViewById(R.id.tv_change);
        tvToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStaggeredLayout = !isStaggeredLayout;
                setTextViewState();
                mDrawerLayout.closeDrawers();
                changeShowStyle(isStaggeredLayout);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        fabButton = (FloatingActionButton) findViewById(R.id.fab_01);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 点击浮动操作按钮时显示列表的顶部
                mRecyclerView.scrollToPosition(0);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        changeShowStyle(isStaggeredLayout);

        // 下拉刷新控件
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        // 设置下拉刷新控件显示的样式
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright);

        mSwipeRefreshLayout.setRefreshing(true); // 显示下拉刷新控件
        // 下拉刷新事件监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void setTextViewState() {
        if (isStaggeredLayout) {// 瀑布流
            tvToggle.setText("切换为列表");
        } else { //
            tvToggle.setText("切换为瀑布流");
        }
    }

    private void changeShowStyle(boolean isStaggeredLayout) {
        RecyclerView.LayoutManager manager =
                isStaggeredLayout ? new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL) : new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new NewsAdapter(this, isStaggeredLayout);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmDatas(mDatas); // 设置列表数据，刷新列表数据
    }

    // 加载数据
    private void loadData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1500);
                // 加载本地安装的应用程序
                mDatas = getNewsDatas();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.setmDatas(mDatas); // 设置列表数据，刷新列表数据
                mSwipeRefreshLayout.setRefreshing(false); // 隐藏刷新控件
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 跳转到详情界面，并且实现转场动画的效
     */
    public void animateActivity(NewsBean bean, View item, int pos) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("news", bean);
        intent.putExtra("pos", pos);

        View view = item.findViewById(R.id.iv_image);
        // 创建转场动画，实现转场动画效果。
        ActivityOptionsCompat option = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        Pair.create(view, "tran_icon"),
                        Pair.create((View) fabButton, "tran_fab")
                );

        startActivity(intent, option.toBundle());
    }

    private void initActionBar() {
        // 工具栏，使用它替换ActionBar时，要用一个没有actionbar的主题
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Material Design");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置显示左侧按钮

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 初始化开关，并和drawer关联
        mToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mToggle.syncState();//该方法会自动和actionBar关联
        mDrawerLayout.setDrawerListener(mToggle); // 设置监听器让两个控件的状态关联起来
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {// 左上角logo处被点击
            mToggle.onOptionsItemSelected(item);//侧边栏收起或者关闭
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建假数据
     */
    public static List<NewsBean> getNewsDatas() {
        ArrayList<NewsBean> news = new ArrayList<NewsBean>();
        for (int i = 0; i < 40; i++) {
            //packInfo  相当于一个应用程序apk包的清单文件
            NewsBean newsBean = new NewsBean();
            newsBean.id = i;
            newsBean.newsTitle = "新闻 " + i;
            newsBean.newsImage = R.drawable.p1 + i;
            news.add(newsBean);
        }
        return news;
    }
}
