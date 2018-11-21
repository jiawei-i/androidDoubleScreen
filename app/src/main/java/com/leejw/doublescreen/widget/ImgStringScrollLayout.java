/*
 * Copyright 2015. OpenCom (www.opencom.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leejw.doublescreen.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.leejw.doublescreen.R;
import com.leejw.doublescreen.util.SDHandlerUtil;


import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * on 2015/12/16.
 * 定时自动循环滚动图片控件
 * on 2014/10/17.
 */
public class ImgStringScrollLayout extends RelativeLayout {

    private Context mContext;

    private View view;
    private ViewPager viewPager;
    private LinearLayout ovalLayout;

    private List<File> imgsList;

    private ImgScrollPagerAdapter scrollPagerAdapter;

    private int mScrollTime = 10000;
    private Timer timer;
    private TimerTask timerTask;

    private int curIndex = 0;
    private int oldIndex = 0;

    public ImgStringScrollLayout(Context context) {
        super(context);
        init(context);
    }

    public ImgStringScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.img_scroll_layout, null);
        addView(view);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ovalLayout = (LinearLayout) view.findViewById(R.id.oval_layout);
    }

    public void setScrollTime(int scrollTime) {
        this.mScrollTime = scrollTime;
    }

    public void setData(List<File> imgsList) {
        /*if (this.imgsList != null) {
            if (this.imgsList.size() > 0) {
                this.imgsList.clear();
            }
        }*/
        this.imgsList = imgsList;
        setOvalLayout(this.imgsList);
        scrollPagerAdapter = new ImgScrollPagerAdapter(this.imgsList);
        viewPager.setAdapter(scrollPagerAdapter);

        if (mScrollTime != 0 && imgsList.size() > 1) {

//            new FixedSpeedScroller(mContext).setDuration(this, 700);

//            startTime();

            //触摸的时候停止滚动
            viewPager.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_UP) {
                        startTime();
                    } else {
                        stopTime();
                    }
                    return false;
                }
            });
        }
        if (imgsList.size() > 1) {
//            viewPager.setCurrentItem((Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % imgsList.size());
            viewPager.setCurrentItem(10 * imgsList.size());
        }
    }

    //动态生成圆点
    private void setOvalLayout(final List<File> imgsList) {
        if(imgsList == null || imgsList.size() <= 1){
            ovalLayout.setVisibility(View.GONE);
            return;
        }else {
            ovalLayout.setVisibility(View.VISIBLE);
        }
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ovalLayout.removeAllViews();
        for (int i = 0; i < imgsList.size(); i++) {//inflater.inflate 按需实例化...
            ovalLayout.addView(inflater.inflate(R.layout.oval_item_layout, null));
        }

        /*if(textList != null)
        tv.setText(textList.get(0)+"");*/
        //默认 第一个 为选中状态
        if(ovalLayout.getChildCount() <= 0) {
            return;
        }
        ovalLayout.getChildAt(0).findViewById(R.id.ad_item_v).setBackgroundResource(R.drawable.oval_focused);
//        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if (imgsList.size() != 0) {
                    curIndex = i % imgsList.size();
                    //取消 圆点选中
                    ovalLayout.getChildAt(oldIndex).findViewById(R.id.ad_item_v).setBackgroundResource(R.drawable.oval_normal);
                    //圆点选中
                    ovalLayout.getChildAt(curIndex).findViewById(R.id.ad_item_v).setBackgroundResource(R.drawable.oval_focused);
                    oldIndex = curIndex;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    private class ImgScrollPagerAdapter extends PagerAdapter {

        private List<File> imgsList;

        private ImgScrollPagerAdapter(List<File> imgsList) {
            this.imgsList = imgsList;
        }

        @Override
        public int getCount() {
            if (imgsList.size() == 1) {//只有一张时不滚动
                return imgsList.size();
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == (o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.img_view_layout, container, false);
            if (imgsList.size() == 0) {
                return layout;
            }
            ImageView iv = (ImageView) layout.findViewById(R.id.iv);
            File imgUrl = imgsList.get(position % imgsList.size());

            Glide.with(mContext).load(imgUrl).into(iv);

            container.addView(layout, 0);

            //设置图片点击事件
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imgsList.size() == 0) {
                        return;
                    }
                }
            });
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 开始滚动
     */
    public synchronized void startTime() {
        stopTime();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                SDHandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (scrollPagerAdapter != null) {
                            if (viewPager.getCurrentItem() + 1 == scrollPagerAdapter.getCount()) {
                                viewPager.setCurrentItem(0, false);
                            } else {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, mScrollTime, mScrollTime);//延时 mScrollTime ms后执行， ms执行一次
    }

    /**
     * 停止滚动
     */
    public void stopTime() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
        } catch (Exception e) {
            Log.e("ImgStringScroll",e.getMessage(), e);
        }

    }
}
