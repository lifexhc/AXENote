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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.data.Note;
import com.xuhongchuan.axenote.ui.view.RichEditor;
import com.xuhongchuan.axenote.utils.BitmapUtil;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;
import com.xuhongchuan.axenote.utils.L;

import java.util.Date;

/**
 * Created by xuhongchuan on 16/5/3.
 */
public class EditorActivity extends BaseActivity {
    private final int RQ_GET_IMAGE_FROM_SD_CARD = 1;
    public static final int READ_EXTERNAL_STORAGE_REQ_CODE = 2;

//    public static String EXTRA_ID = "id";
//    public static String EXTRA_CONTENT = "content";
//    public static String EXTRA_CREATE_TIME = "createTime";
//    public static String EXTRA_LAST_MODIFIED_TIME = "lastModifiedTime";

    private int mId; // 便签ID
    private String mContent; // 便签内容
    private long mCreateTime; // 便签创建时间
    private long mLastModifiedTime; // 便签最后编辑时间

    private RichEditor mWebView;
    private Toolbar mToolbar;
    MenuItem mInsertImg;
    private String mFilePath; // 图片路径，从SD卡获取图片时使用


    @Override
    @SuppressLint("JavascriptInterface")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.richeditor);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        /**
         * 返回箭头
         */
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mWebView = (RichEditor) findViewById(R.id.webview);
        mWebView.addJavascriptInterface(new JsInterface(), "AndroidEditor");
        mWebView.addJavascriptInterface(EditorActivity.this, "EditorActivity");

        initContent();
    }

    /**
     * 获取传递进来的便签信息
     */
    private void initContent() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        Note note = GlobalDataCache.getInstance().getNotes().get(position);
//        mId = intent.getIntExtra(EXTRA_ID, -1);
//        mContent = intent.getStringExtra(EXTRA_CONTENT);
//        mCreateTime = intent.getLongExtra(EXTRA_CREATE_TIME, 0);
//        mLastModifiedTime = intent.getLongExtra(EXTRA_LAST_MODIFIED_TIME, 0);
        mId = note.getId();
        mContent = note.getContent();
        mCreateTime = note.getCreateTime();
        mLastModifiedTime = note.getLastModifiedTime();

    }

    private Handler mHandler = new Handler();

    @JavascriptInterface
    public void initEditor() {
        //mWebView.loadUrl("javascript:initContent(" + mContent + ")");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:initContent('" + mContent + "')");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);

        // 初始化插入图片button
        mInsertImg = menu.findItem(R.id.insert_img);
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
                    L.d(this, "uri:" + originalUri.toString());
                    mFilePath = getFilePathFromURI(this, originalUri);
                    // 根据路径从SD卡获取图片并压缩
                    final Bitmap bitmap = BitmapUtil.compressBitmap(mFilePath, this, 0.75F);
                    String base64 = BitmapUtil.toBase64(bitmap);
                    L.d(this, bitmap.getByteCount() / 1024 + "kb");
                    long startTime=System.currentTimeMillis();   // 开始时间
                    L.d("base64", base64);
                    mWebView.loadUrl("javascript:insertImg('" + base64 + "')");
                    long endTime=System.currentTimeMillis(); // 结束时间
                    L.d("BitmapUtil", "loadUrl用时：" + (endTime-startTime) + "ms");
                    break;
            }
        }
    }

    public class JsInterface {
        @JavascriptInterface
        public void getEditorContent(String value) {
            mContent = value;
            GlobalDataCache.getInstance().updateNote(mId, mContent, false, new Date().getTime());
            // 发送更新便签列表的广播
            Intent intent = new Intent(GlobalValue.REFRESH_NOTE_LIST);
            sendBroadcast(intent);
        }
    }

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
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:getEditorContent()");
            }
        });
    }

    @Override
    public void initTheme() {

    }

    @Override
    public void changeTheme() {

    }

}




