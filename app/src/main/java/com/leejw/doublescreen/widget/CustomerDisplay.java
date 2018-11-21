package com.leejw.doublescreen.widget;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;


import com.leejw.doublescreen.R;

import java.io.File;
import java.util.List;

/**
 * 描述：
 * 创建人：zxm
 * 创建时间： 2017/12/12 on 9:08.
 */

public class CustomerDisplay extends Presentation {
    private Context mContext;
    private ImgStringScrollLayout imgStringScrollLayout;

    public CustomerDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.view_display_customer);
        imgStringScrollLayout = findViewById(R.id.img_string_scroll_layout);
    }

    /**
     * 设置显示图片轮播
     */
    public void setProductList(List<File> str) {
        if (str != null && str.size() > 0) {
            imgStringScrollLayout.setData(str);
            imgStringScrollLayout.startTime();
        }
    }
}