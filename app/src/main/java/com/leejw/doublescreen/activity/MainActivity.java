package com.leejw.doublescreen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.leejw.doublescreen.R;
import com.leejw.doublescreen.util.SystemUtil;
import com.leejw.doublescreen.widget.CustomerEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 本地图片路径
     */
    public List<File> localPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView confirmTv = findViewById(R.id.confirm_tv);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerEngine.getInstance(MainActivity.this).setProductList(localPhotoPath);
            }
        });

        TextView sameTv = findViewById(R.id.same_tv);
        sameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerEngine.getInstance(MainActivity.this).setSame();
            }
        });
    }

    private void checkLocalPhoto() {
        localPhotoPath = new ArrayList<File>();
        File savePath = new File(getFilesDir().getPath() + "/PhotoShow/");
        if (!savePath.exists()) {
            savePath.mkdir();
        }
        SystemUtil.findFile(localPhotoPath, savePath.getPath(), false);
        for (int i = localPhotoPath.size() - 1; i >= 0; i--) {
            if (!SystemUtil.isBitmap(localPhotoPath.get(i))) {
                localPhotoPath.remove(i);
            }
        }
        if (localPhotoPath.size() == 0) {
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocalPhoto();
    }
}
