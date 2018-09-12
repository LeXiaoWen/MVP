package cree.mvp.ui.bar.v7;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import cree.mvp.ui.bar.BarView;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/8  17:27
 *
 * @author luyongjiang
 * @version 1.0
 */
public interface BarControl {
    /**
     * 设置标题栏
     *
     * @param name
     */
    void setTitle(String name);

    /**
     * 设置返回按钮
     */
    void setBackButton(Boolean show, BarView.OnBarClickListener onBarClickListener);

    /**
     * 添加右边的按钮
     *
     * @param drawable
     */
    void addRightButton(@DrawableRes int drawable, BarView.OnBarClickListener onBarClickListener);

    /**
     * 累加在右边的按钮上
     *
     * @param drawable
     */
    void addRightButton(Drawable drawable, BarView.OnBarClickListener onBarClickListener);

    /**
     * 释放bar
     */
    void releaseBar();

    /**
     * 显示右边的按钮
     */
    void showRight();

    /**
     * 隐藏右边的按钮
     */
    void hideRight();
}
