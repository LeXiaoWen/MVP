package cree.mvp.ui.changepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Title:禁止滚动的viewpager
 * Description:可以设置是否需要滚动
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/4/25  17:22
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomScrollViewPager extends ViewPager {
    /**
     * 是否禁止左右滑动，true为禁止，false为不禁止
     */
    private boolean noScroll = true;

    public CustomScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll)
            //禁止左右滑动
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            //禁止左右滑动
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    /**
     * 打开滚动
     */
    public void openScroll() {
        noScroll = true;
    }

    /**
     * 关闭滚动
     */
    public void closeScroll() {
        noScroll = false;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //表示切换的时候，不需要切换时间。
        super.setCurrentItem(item, false);
    }
}
