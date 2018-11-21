package com.leejw.doublescreen.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.leejw.doublescreen.util.FileUtils;
import com.leejw.doublescreen.R;
import com.leejw.doublescreen.util.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    /**
     * 更新文本
     */
    public static final int UPDATE_TXT = 743;
    /**
     * 结束
     */
    public static final int FINISH = 158;

    private TextView txtData;
    PackageManager pm;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == UPDATE_TXT) {
                txtData.append((String) msg.obj);
            } else if (msg.what == FINISH) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        pm = getPackageManager();
        txtData = (TextView) findViewById(R.id.Activity_Update_Txt_Data);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String photoPath = getIntent().getStringExtra("PhotoPath");
                if (photoPath != null) {
                    updatePhoto(photoPath);
                }
                int time = 10;
                while (time >= 0) {
                    myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("%s秒后退出\n", time--)));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                myHandler.sendEmptyMessage(FINISH);
            }
        }).start();
    }

    public void updatePhoto(String photoPath) {
        myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("正在更新图片-------%s\n", photoPath)));
        File file = new File(photoPath);
        if (!file.exists()) {
            return;
        }
        List<File> files = new ArrayList<File>();
        SystemUtil.findFile(files, photoPath, false);
        File savePath = new File(getFilesDir().getPath() + "/PhotoShow/");
        Log.d("Update","内部路径" + savePath);
        for (int i = files.size() - 1; i >= 0; i--) {
            if (!SystemUtil.isBitmap(files.get(i))) {
                files.remove(i);
            }
        }
        if (files.size() == 0) {
            myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, "没有找到图片%s\n"));
            return;
        }
        File[] oldFile = savePath.listFiles();
        if (oldFile != null && oldFile.length>0) {
            for (File f : oldFile) {
                myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("删除老图片[%s]张\n", f.getName())));
                f.delete();
            }
        }

        myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("找到新图片[%s]张\n", files.size())));

        if (!savePath.exists()) {
            savePath.mkdir();
        }
        long maxLength = 3 * 1024 * 1024;
        for (File f : files) {
            if (f.length() > maxLength) {

                myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("新图片[%s]-大于3M-添加失败\n", f.getName())));
            } else {
                FileUtils.copyFile(f, new File(savePath.getPath() + "/" + f.getName()));
                myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("新图片[%s]-添加成功\n", f.getName())));
            }

        }
        myHandler.sendMessage(myHandler.obtainMessage(UPDATE_TXT, String.format("更新图片完成-------%s\n", photoPath)));
    }
}
