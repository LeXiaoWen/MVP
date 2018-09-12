package cree.mvp.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cree.mvp.base.presenter.BasePresenter;


/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  18:24
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseNoTitleActivity<T extends BasePresenter> extends BaseActivity<T> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNoTitle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 5.0以后好像有一些问题,需要在xml的主题中指定
     * 不要用代码设置消除标题栏,在清单文件中通过主题指定的方式来设置标题栏
     * <item name="windowActionBar">false</item>
     * <item name="windowNoTitle">true</item>
     */
    private final void setNoTitle() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
    }
}
