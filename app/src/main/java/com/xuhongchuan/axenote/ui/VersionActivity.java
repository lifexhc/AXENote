package com.xuhongchuan.axenote.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuhongchuan.axenote.R;

/**
 * Created by xuhongchuan on 15/10/21.
 */
public class VersionActivity extends AppCompatActivity {

    private Button btnCheckUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        btnCheckUpdate = (Button) findViewById(R.id.btn_check_update);

        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
