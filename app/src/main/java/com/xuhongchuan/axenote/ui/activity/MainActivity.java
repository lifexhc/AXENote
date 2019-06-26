package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.NoteListAdapter;
import com.xuhongchuan.axenote.adapter.SimpleItemTouchHelperCallback;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;
import com.xuhongchuan.axenote.utils.GlobalConfig;

import java.util.Date;

import butterknife.BindView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.rv_note_list)
    RecyclerView recyclerView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private SearchView mSearchView;
    private ImageView mSearchViewIcon; // SearchView的图标，用于切换主题时修改图标

    private NoteListAdapter mAdapter;
    /**
     * 广播
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalValue.REFRESH_NOTE_LIST)) {
                GlobalDataCache.getInstance().syncNotes();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalValue.REFRESH_NOTE_LIST);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    /**
     * 初始化元素
     */
    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 悬浮按钮
        fab.setOnClickListener(view -> {
            Date date = new Date();
            long currentTime = date.getTime();
            // 创建新便签
            GlobalDataCache cache = GlobalDataCache.getInstance();
            cache.createNewNote("", false, currentTime, currentTime);

            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            intent.putExtra("position", 0);
            startActivity(intent);
        });

        // 便签列表
        mAdapter = new NoteListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        // 为NoteList绑定事件
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        // 侧滑菜单内容
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // 和侧滑菜单绑定
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 修改toolbar上图标的颜色
     */
    private void changeToolbarIconTheme() {
        if (mSearchView != null) {
            if (GlobalConfig.getInstance().isNightMode(this)) {
                mSearchViewIcon.setImageResource(R.drawable.ic_search);
                toolbar.setNavigationIcon(R.drawable.ic_menu);
            } else {
                mSearchViewIcon.setImageResource(R.drawable.ic_search);
                toolbar.setNavigationIcon(R.drawable.ic_menu);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 如果呼出了侧滑菜单则关闭菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    /**
     * 初始化菜单
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // 初始化SearchView
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = R.id.search_button;
        mSearchViewIcon = (ImageView) mSearchView.findViewById(searchImgId);
        mSearchViewIcon.setImageResource(R.drawable.ic_search);
        mSearchView.setOnQueryTextListener(this);

        // 初始化icon主题
        changeToolbarIconTheme();

        return true;
    }

    @Override
    /**
     * toolbar菜单点击事件
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /**
     * 侧滑菜单点击事件
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_theme) { // 主题
            Resources res = getResources();
            final String[] themes = {res.getString(R.string.day), res.getString(R.string.night)};
            final GlobalConfig config = GlobalConfig.getInstance();
            final AlertDialog.Builder builder;
            // 根据当前主题设置对话框主题
            if (config.isNightMode(MainActivity.this)) {
                builder = new AlertDialog.Builder(this, R.style.Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this, R.style.Dialog_Alert);
            }
            builder.setTitle(res.getString(R.string.select_theme));
            int checkedItem = config.isNightMode(MainActivity.this) ? 1 : 0; // 默认选中项
            builder.setSingleChoiceItems(themes, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        config.setNightMode(MainActivity.this, false);
                    } else {
                        config.setNightMode(MainActivity.this, true);
                    }
                    // 发送切换主题广播
                    Intent intent = new Intent(GlobalValue.CHANGE_THEME);
                    sendBroadcast(intent);
                    dialog.dismiss();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (id == R.id.nav_version) { // 版本号
            Intent intent = new Intent(this, VersionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_author) { // 关于作者
            Intent intent = new Intent(this, AboutAuthorActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * SearchView 提交查询事件
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * SearchView 关键字变化事件
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
