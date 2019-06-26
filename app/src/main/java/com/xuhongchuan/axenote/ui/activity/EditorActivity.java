package com.xuhongchuan.axenote.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.ui.view.RichEditor;
import com.xuhongchuan.axenote.utils.BitmapUtil;
import com.xuhongchuan.axenote.utils.GlobalConfig;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.util.Date;

import butterknife.BindView;

/**
 * 编辑界面
 * Created by xuhongchuan on 16/5/3.
 */
public class EditorActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    RichEditor richEditor;

    public static final int READ_EXTERNAL_STORAGE_REQ_CODE = 2;
    private final int RQ_GET_IMAGE_FROM_SD_CARD = 1;
    private int mId; // 便签ID
    private String mContent; // 便签内容
    private long mCreateTime; // 便签创建时间
    private long mLastModifiedTime; // 便签最后编辑时间

    private MenuItem mInsertImg;
    private String mFilePath; // 图片路径，从SD卡获取图片时使用
    private Handler mHandler = new Handler();

    @SuppressLint("NewApi")
    public static String getFilePathFromURI(Context context, Uri uri) {
        // 有两种uri格式
        // uri:content://media/external/images/media/88
        // uri:content://com.android.providers.media.documents/document/image%3A13004
        // 判断uri格式是否第二种
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(context, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
    }

    @Override
    @SuppressLint("JavascriptInterface")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        /**
         * 返回箭头
         */
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        richEditor.addJavascriptInterface(new JsInterface(), "AndroidEditor");
        richEditor.addJavascriptInterface(EditorActivity.this, "EditorActivity");

        initContent();
    }

    /**
     * 获取传递进来的便签信息
     */
    private void initContent() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        Note note = GlobalDataCache.getInstance().getNotes().get(position);
        mId = note.getId();
        mContent = note.getContent();
        mCreateTime = note.getCreateTime();
        mLastModifiedTime = note.getLastModifiedTime();

    }

    @JavascriptInterface
    public void initEditor() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                boolean isNightMode = GlobalConfig.getInstance().isNightMode(EditorActivity.this);
                if (isNightMode) {
                    richEditor.loadUrl("javascript:initTheme('" + 1 + "')");
                } else {
                    richEditor.loadUrl("javascript:initTheme('" + 0 + "')");
                }
                richEditor.loadUrl("javascript:initContent('" + mContent + "')");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);

        // 初始化插入图片button
        mInsertImg = menu.findItem(R.id.insert_img);

        // 初始化icon主题
        changeToolbarIconTheme();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insert_img) { // 插入图片
            requestPermission();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 请求权限
     */
    public void requestPermission() {
        // 判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限
            // 判断这个权限是否曾经被用户拒绝过
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // 进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        } else {
            // 已经获得相应权限
            getImgFromSDCard();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQ_CODE: {
                // 判断权限请求是否通过
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 请求通过
                    getImgFromSDCard();
                } else {
                    // 请求被拒绝
                }
                return;
            }
        }
    }

    /**
     * 从SD卡获取图片
     */
    public void getImgFromSDCard() {
        Intent getImgIntent = new Intent();
        getImgIntent.setAction(Intent.ACTION_GET_CONTENT);
        getImgIntent.addCategory(Intent.CATEGORY_OPENABLE);
        getImgIntent.setType("image/*");
        startActivityForResult(getImgIntent, RQ_GET_IMAGE_FROM_SD_CARD);
    }

    @Override
    @SuppressLint("addJavascriptInterface")
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQ_GET_IMAGE_FROM_SD_CARD:
                    // 获取图片的路径
                    Uri originalUri = intent.getData();
                    mFilePath = getFilePathFromURI(this, originalUri);
                    // 根据路径从SD卡获取图片并压缩
                    final Bitmap bitmap = BitmapUtil.compressBitmap(mFilePath, this, 0.75F);
                    String base64 = BitmapUtil.toBase64(bitmap);
                    richEditor.loadUrl("javascript:insertImg('" + base64 + "')");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.post(() -> {
            richEditor.loadUrl("javascript:getEditorContent()");
            richEditor.clearCache(true);
        });
    }

    /**
     * 修改toolbar上图标的颜色
     */
    private void changeToolbarIconTheme() {
        if (mInsertImg != null) {
            if (GlobalConfig.getInstance().isNightMode(this)) {
                mInsertImg.setIcon(R.drawable.ic_insert_img);
                toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            } else {
                mInsertImg.setIcon(R.drawable.ic_insert_img);
                toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            }
        }
    }

    public class JsInterface {
        @JavascriptInterface
        public void getEditorContent(String title, String content, int count) {
            mContent = content;
            boolean hasImage = false;
            if (count > 0) {
                hasImage = true;
            }
            GlobalDataCache.getInstance().updateNote(mId, title, mContent, hasImage, new Date().getTime());
            // 发送更新便签列表的广播
            Intent intent = new Intent(GlobalValue.REFRESH_NOTE_LIST);
            sendBroadcast(intent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editor;
    }
}




