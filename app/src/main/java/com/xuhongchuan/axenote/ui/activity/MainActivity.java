package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.NoteListAdapter;
import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;
import com.xuhongchuan.axenote.utils.GlobalConfig;

import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFAB;
    private RecyclerView mRecycleView;
    private SearchView mSearchView;
    private NoteListAdapter mAdapter;
    private NavigationView mNavigationView;
    private ImageView mSearchViewIcon; // SearchView的图标，用于切换主题时修改图标

    private Prism mPrism; // 主题切换

    /**
     * 广播
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalValue.REFRESH_NOTE_LIST)) {
                GlobalDataCache.getInstance().initNotes();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

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
     * NoteList相关事件
     */
    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

        @Override
        /**
         * 长按移动便签
         */
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition(); // 得到拖动ViewHolder的position
            int toPosition = target.getAdapterPosition(); // 得到目标ViewHolder的position

            // 交换数据库两条便签的排序值
            List<Note> notes;
            NoteDao dao = NoteDao.getInstance();
            notes = dao.getAllNotes();
            int id1 = notes.get(fromPosition).getId();
            int id2 = notes.get(toPosition).getId();
            dao.swapOrdinal(id1, id2);

            mAdapter.notifyItemMoved(fromPosition, toPosition);
            GlobalDataCache.getInstance().initNotes(); // 更新Note列表
            // 返回true表示执行拖动
            return true;
        }

        @Override
        /**
         * 滑动删除便签
         */
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.notifyItemRemoved(position);
            GlobalDataCache cache = GlobalDataCache.getInstance();
            cache.deleteNote(cache.getNotes().get(position).getId());
            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initTheme();
    }

    /**
     * 初始化元素
     */
    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        // 悬浮按钮
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                long currentTime = date.getTime();
                // 创建新便签
                GlobalDataCache cache = GlobalDataCache.getInstance();
                cache.createNewNote("", currentTime, currentTime);

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("position", 0);
//                intent.putExtra(EditorActivity.EXTRA_ID, cache.getLastId());
//                intent.putExtra(EditorActivity.EXTRA_CONTENT, "");
//                intent.putExtra(EditorActivity.EXTRA_CREATE_TIME, currentTime);
//                intent.putExtra(EditorActivity.EXTRA_LAST_MODIFIED_TIME, currentTime);
                startActivity(intent);

//                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
//                startActivity(intent);
            }
        });

        // 便签列表
        mRecycleView = (RecyclerView) findViewById(R.id.rv_note_list);
        mAdapter = new NoteListAdapter(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);

        // 为NoteList绑定事件
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);

        // 侧滑菜单内容
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        // 和侧滑菜单绑定
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 初始化主题
     */
    public void initTheme() {
        LinearLayout llHeader = (LinearLayout) mNavigationView.findViewById(R.id.ll_header);

        mPrism = Prism.Builder.newInstance()
                .background(getWindow())
                .background(mToolbar)
                .background(mFAB)
                .background(llHeader)
                .build();
        changeTheme();
    }

    /**
     * 修改主题
     */
    @Override
    public void changeTheme() {
        Resources res = getResources();
        boolean isNightMode = GlobalConfig.getInstance().isNightMode(MainActivity.this);
        if (isNightMode) {
            mPrism.setColour(res.getColor(R.color.divider));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
        }
        CoordinatorLayout dl = (CoordinatorLayout) findViewById(R.id.app_bar_main);
        if (isNightMode) {
            dl.setBackgroundColor(res.getColor(R.color.divider));
            mNavigationView.setBackgroundColor(res.getColor(R.color.bg_night));
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.activity_main_drawer_night);

            mFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus_night));
        } else {
            dl.setBackgroundColor(res.getColor(R.color.white));
            mNavigationView.setBackgroundColor(res.getColor(R.color.white));
            mNavigationView.getMenu().clear();
            mNavigationView.inflateMenu(R.menu.activity_main_drawer);

            mFAB.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus));
        }
        mAdapter.notifyDataSetChanged();

        changeToolbarIconTheme();
    }

    /**
     * 修改toolbar上图标的颜色
     */
    private void changeToolbarIconTheme() {
        if (mSearchView != null) {
            if (GlobalConfig.getInstance().isNightMode(this)) {
                mSearchViewIcon.setImageResource(R.drawable.ic_search_night);
                mToolbar.setNavigationIcon(R.drawable.ic_menu_night);
            } else {
                mSearchViewIcon.setImageResource(R.drawable.ic_search);
                mToolbar.setNavigationIcon(R.drawable.ic_menu);
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
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
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
                builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            } else {
                builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
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

}
