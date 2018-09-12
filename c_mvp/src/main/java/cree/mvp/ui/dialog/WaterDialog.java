package cree.mvp.ui.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;

import javax.inject.Inject;

import static android.content.DialogInterface.OnClickListener;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  22:33
 *
 * @author luyongjiang
 * @version 1.0
 */
public class WaterDialog {
    CrDialog mCrDialog;

    @Inject
    public WaterDialog() {

    }


    public WaterDialog showProgress(Context context, String message) {
        mCrDialog = new CrDialog.Builder(context).setMessage(message).isProgress().create();
        mCrDialog.show();
        return this;
    }

    public WaterDialog showClearProgress(Context context, String message) {
        mCrDialog = new CrDialog.Builder(context).setTitle("更新").isHorizontalProgress().setMessage(message).create();
        mCrDialog.show();
        return this;
    }


    public WaterDialog showCustom(Context context, @LayoutRes int layoutId) {
        mCrDialog = new CrDialog.Builder(context).setLayoutId(layoutId).create();
        mCrDialog.show();
        return this;
    }

    public WaterDialog showTrans(Context context, @LayoutRes int layoutId) {
        mCrDialog = new CrDialog.Builder(context).isTransparent().setLayoutId(layoutId).create();
        mCrDialog.show();
        return this;
    }

    public WaterDialog showMessage(Context context, String message) {
        mCrDialog = new CrDialog.Builder(context).setMessage(message).create();
        mCrDialog.show();
        return this;
    }

    public WaterDialog showTitleAndMessage(Context context, String title, String message) {
        mCrDialog = new CrDialog.Builder(context).setTitle(title).setMessage(message).create();
        mCrDialog.show();
        return this;
    }

    public WaterDialog showConfirmButtonMessage(Context context, String title, String message, OnClickListener confirmListener, OnClickListener cancelListener) {
        mCrDialog = new CrDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .isNoAutoCancel()
                .setConfirmButton("确定", confirmListener)
                .setCancelButton("取消", cancelListener)
                .create();
        mCrDialog.show();
        return this;
    }


    public void dismiss() {
        if (mCrDialog != null)
            mCrDialog.dismiss();
    }
}
