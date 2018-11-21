package com.leejw.doublescreen.widget;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.List;

/**
 * 描述：客户屏幕显示内容工具类
 * 创建人：zxm
 * 创建时间： 2017/12/20 on 15:04.
 */

public class CustomerEngine {
    // 获取设备上的屏幕
    private DisplayManager mDisplayManager;// 屏幕管理器
    private Display[] displays;// 屏幕数组
    private CustomerDisplay mCustomerDisplay;   //（继承Presentation）

    private static CustomerEngine instance;

    /**
     * 创建的时候把界面绑定到第二个屏幕中
     * @param context 这里需要传入getApplicationContext(),就能实现全局双屏异显
     * @return
     */
    public static CustomerEngine getInstance(Context context){
        if(instance == null){
            instance = new CustomerEngine(context);
        }
        return instance;
    }

    public static void colose(){
        instance = null;
    }

    private CustomerEngine(Context context){
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        displays = mDisplayManager.getDisplays();
        if (null == mCustomerDisplay && displays.length > 1) {
            mCustomerDisplay =  new CustomerDisplay(context, displays[1]);// displays[1]是副屏
            mCustomerDisplay.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mCustomerDisplay.show();
        }
    }

    /**
     * 显示图片轮播
     */
    public void setProductList(List<File> str){
        if(mCustomerDisplay != null){
            mCustomerDisplay.setProductList(str);
        }
    }

    /**
     * 取消异显
     */
    public void setSame(){
        if(mCustomerDisplay != null){
            mCustomerDisplay.dismiss();
        }
    }
}
