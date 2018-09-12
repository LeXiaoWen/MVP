package cree.mvp.base.activity;

import javax.inject.Inject;

import cree.mvp.base.presenter.BasePresenter;
import cree.mvp.ui.dialog.WaterDialog;


/**
 * Title:可以用来做请求的activity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/17  10:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseRequestActivity<T extends BasePresenter> extends BaseNoTitleActivity<T> {


    @Inject
    public WaterDialog mDialog;

    public void showProgress() {
        mDialog.showProgress(this, "请稍等...");
    }

    public void dismissProgress() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    public void showMessage(String message) {
        mDialog.showMessage(this, message);
    }


}
