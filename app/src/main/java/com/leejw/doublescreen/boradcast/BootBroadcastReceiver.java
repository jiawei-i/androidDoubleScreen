package com.leejw.doublescreen.boradcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.leejw.doublescreen.util.SystemUtil;
import com.leejw.doublescreen.activity.UpdateActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by leejw on 2018/11/21.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    PackageManager pm;
    Context context;
    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {

            String path = intent.getDataString();
            String pathString = path.split("file://")[1];
            Log.d(TAG,"U盘插入" + pathString);
            Intent i = new Intent(context, UpdateActivity.class);
            boolean isUpdate = false;
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            List<File> files = new ArrayList<File>();
            SystemUtil.findFile(files, pathString + "/PhotoShow/", true);
            if (files.size() > 0) {
                isUpdate = true;
                i.putExtra("PhotoPath", pathString + "/PhotoShow/");
            }else {
                SystemUtil.findFile(files, pathString + "/udisk0/PhotoShow/", true);
                if (files.size() > 0) {
                    isUpdate = true;
                    i.putExtra("PhotoPath", pathString + "/udisk0/PhotoShow/");
                }
            }
            if (isUpdate) {
                context.startActivity(i);
            }
        }
    }
}
