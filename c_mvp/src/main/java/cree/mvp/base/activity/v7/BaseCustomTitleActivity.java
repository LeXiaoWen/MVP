package cree.mvp.base.activity.v7;

import android.support.annotation.LayoutRes;
import android.view.MenuItem;
import android.view.View;

import cree.mvp.base.activity.ActivityShowModel;
import cree.mvp.base.activity.BaseRequestActivity;
import cree.mvp.base.presenter.BasePresenter;
import cree.mvp.ui.bar.VIEW_CONVER_PATTERNS;
import cree.mvp.ui.bar.v7.CustomToolbar;

/**
 * Title:支持___V7___TitleBar的Activity
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/8  17:34
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseCustomTitleActivity<T extends BasePresenter> extends BaseRequestActivity<T> implements ActivityShowModel {
    protected CustomToolbar mCustomToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    private void init() {
        if (mCustomToolbar == null) {
            mCustomToolbar = new CustomToolbar(this, showModel());
        }
        if (isShowLeftIcon()) {
            initIcon();
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
    }

    public void setTitle(String title) {
        mCustomToolbar.setTitle(title);
    }

    /**
     * 设置视图显示模式
     * {@link VIEW_CONVER_PATTERNS#LINEAR_LAYOUT} 线性显示模式
     * {@link VIEW_CONVER_PATTERNS#FRAME_LAYOUT} 叠加显示模式
     *
     * @return 默认返回
     */
    @Override
    public VIEW_CONVER_PATTERNS showModel() {
        return VIEW_CONVER_PATTERNS.LINEAR_LAYOUT;
    }


    private void initIcon() {
        //V7Toolbar自带的返回
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //自定义的返回
        mCustomToolbar.setBackButton(isShowLeftIcon(), v -> shutDown());
    }

    /**
     * 根据返回值判断是否显示返回按钮
     * 返回按钮的响应时间在{@onOptionsItemSelected()}
     *
     * @return 默认false 不显示  true显示
     */
    public boolean isShowLeftIcon() {
        return false;
    }

    /**
     * 标题栏的返回点击事件
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                shutDown();
                break;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCustomToolbar != null) {
            mCustomToolbar.releaseBar();
            mCustomToolbar = null;
        }
    }
}
