package com.xuhongchuan.axenote.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.stylingandroid.prism.Prism;
import com.xuhongchuan.axenote.R;
import com.xuhongchuan.axenote.dao.ImgDao;
import com.xuhongchuan.axenote.utils.BitmapUtils;
import com.xuhongchuan.axenote.utils.GlobalConfig;
import com.xuhongchuan.axenote.utils.GlobalDataCache;
import com.xuhongchuan.axenote.utils.GlobalValue;

import java.util.Date;

/**
 * Created by xuhongchuan on 15/10/17.
 */
public class ContentActivity extends BaseActivity {

    public static String EXTRA_ID = "id";
    public static String EXTRA_CONTENT = "content";
    public static String EXTRA_CREATE_TIME = "createTime";
    public static String EXTRA_LAST_MODIFIED_TIME = "lastModifiedTime";
    public static final int READ_EXTERNAL_STORAGE_REQ_CODE = 2;
    private final int RQ_GET_IMAGE_FROM_SD_CARD = 1;

    private String mFilePath; // 图片路径，从SD卡获取图片时使用

    private Toolbar mToolbar;
    private EditText mEtContent;

    private int mId; // 便签ID
    private String mContent; // 便签内容
    private long mCreateTime; // 便签创建时间
    private long mLastModifiedTime; // 便签最后编辑时间

    private Prism mPrism; // 主题切换

    /**
     * 获取传递进来的便签信息
     */
    private void initContent() {
        Intent intent = getIntent();
        mId = intent.getIntExtra(EXTRA_ID, -1);
        mContent = intent.getStringExtra(EXTRA_CONTENT);
        mCreateTime = intent.getLongExtra(EXTRA_CREATE_TIME, 0);
        mLastModifiedTime = intent.getLongExtra(EXTRA_LAST_MODIFIED_TIME, 0);

        parseContent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initElement();
        initTheme();
        initContent();
    }

    /**
     * 初始化元素
     */
    private void initElement() {
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

        mEtContent = (EditText) findViewById(R.id.et_content);
    }

    /**
     * 解析便签的富文本内容
     */
    private void parseContent() {
        /**
         * 解析Html的<img>标签
         */
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                if (source != null && !source.equals("")) {
                    int imgId = Integer.parseInt(source);
                    Bitmap bitmap = ImgDao.getInstance().getImg(imgId);
                    BitmapDrawable d = new BitmapDrawable(Resources.getSystem(), bitmap);
                    d.setGravity(Gravity.CENTER);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    d.setBounds(0, 0, dm.widthPixels, d.getIntrinsicHeight());
                    return d;
                }
                return null;
            }
        };
        mEtContent.setText(Html.fromHtml(mContent, imageGetter, null));
    }

    /**
     * 初始化主题
     */
    public void initTheme() {
        mPrism = Prism.Builder.newInstance()
                .background(getWindow())
                .background(mToolbar)
                .build();
        changeTheme();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mContent = Html.toHtml(mEtContent.getText());

        GlobalDataCache.getInstance().updateNote(mId, mContent, new Date().getTime());
        // 发送更新便签列表的广播
        Intent intent = new Intent(GlobalValue.REFRESH_NOTE_LIST);
        sendBroadcast(intent);
    }

    /**
     * 切换主题
     */
    public void changeTheme() {
        Resources res = getResources();
        EditText etContext = (EditText) findViewById(R.id.et_content);
        if (GlobalConfig.getInstance().isNightMode(ContentActivity.this)) {
            mPrism.setColour(res.getColor(R.color.divider));
            etContext.setBackgroundColor(res.getColor(R.color.bg_night));
        } else {
            mPrism.setColour(res.getColor(R.color.primary));
            etContext.setBackgroundColor(res.getColor(R.color.bg_note));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insert_image) { // 插入图片
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
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType("image/*");
        startActivityForResult(getImage, RQ_GET_IMAGE_FROM_SD_CARD);
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQ_GET_IMAGE_FROM_SD_CARD:
                    // 获取图片的路径
                    Uri originalUri = intent.getData();
                    mFilePath = getRealPathFromURI(this, originalUri);
                    // 根据路径从SD卡获取图片并压缩
                    final Bitmap bitmap = BitmapUtils.compressBitmap(mFilePath, this, 0.75F);
                    // 插入图片到数据库
                    // TODO:在主线程插入图片到SQLite太耗费时间，插一张图片要几百毫秒
                    // TODO:但是又需要获得在插入SQLite后的imgId

                    // TODO:如果文本删除了图片，SQLite并没有删除
                    ImgDao.getInstance().insertImg(bitmap);
                    int lastImgId = ImgDao.getInstance().getLastId(); // 图片在数据库的id

                    BitmapDrawable d = new BitmapDrawable(Resources.getSystem(), bitmap);
                    d.setGravity(Gravity.CENTER);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    d.setBounds(0, 0, dm.widthPixels, d.getIntrinsicHeight());
                    if (bitmap != null) {
                        // 根据Bitmap对象创建ImageSpan对象
                        ImageSpan imageSpan = new ImageSpan(d, lastImgId + "", ImageSpan.ALIGN_BASELINE);

                        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                        String img = "i_m_g";
                        SpannableString spannableString = new SpannableString(img);
                        // 用ImageSpan对象替换face
                        spannableString.setSpan(imageSpan, 0, img.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        // 插入图片到EditText，上下换一行
                        mEtContent.append("\n");
                        mEtContent.append(spannableString);
                        mEtContent.append("\n");
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.get_img_failed), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}


