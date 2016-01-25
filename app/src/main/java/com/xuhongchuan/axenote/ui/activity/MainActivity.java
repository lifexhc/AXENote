package com.xuhongchuan.axenote.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.adapter.NoteListAdapter;
import com.xuhongchuan.axenote.dao.NoteDao;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private CircleImageView headerImage;

    private RecyclerView mRecycleView;
    private NoteListAdapter mAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 悬浮按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Date date = new Date();
            long time = date.getTime();
            // 创建新便签
            GlobalDataCache cache = GlobalDataCache.getInstance();
            cache.createNewNote("", time, time);

            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            intent.putExtra("id", cache.getLastId());
            intent.putExtra("content", "");
            intent.putExtra("createTime", time);
            intent.putExtra("updateTime", time);
            startActivity(intent);
            }
        });

        // 和侧滑菜单绑定
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // 侧滑菜单内容
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerImage = (CircleImageView) navigationView.findViewById(R.id.header_image);


        mRecycleView = (RecyclerView) findViewById(R.id.rv_note_list);
        mAdapter = new NoteListAdapter(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);
        // 为NoteList绑定事件
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);
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
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView sv = (SearchView) searchItem.getActionView();
        sv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    /**
     * 菜单点击事件
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cached) { // 同步事件

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    /**
     * 侧滑菜单点击事件
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_last_sync) {
        } else if (id == R.id.nav_theme) {

        } else if (id == R.id.nav_user_info) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_version) {
            Intent intent = new Intent(this, VersionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_author) {
            Intent intent = new Intent(this, AboutAuthorActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * SearchView 提交查询事件
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * SearchView 关键字变化事件
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        return false;
    }
}
