package cree.mvp.base.activity;

import android.support.annotation.LayoutRes;

import cree.mvp.base.presenter.BasePresenter;
import cree.mvp.ui.bar.CustomToolbar;
import cree.mvp.ui.bar.VIEW_CONVER_PATTERNS;

/**
 * Title:自定义View的TitleBar
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
        mCustomToolbar = new CustomToolbar(this, showModel());
    }



    @Override
    public VIEW_CONVER_PATTERNS showModel() {
        return VIEW_CONVER_PATTERNS.FRAME_LAYOUT;
    }
}
