package cree.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import cree.mvp.ui.dialog.WaterDialog;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/4/13  14:11
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseRequestFragment<T extends BaseRequestFragmentPresenter> extends BaseStateFragment<T> {

    @Inject
    protected WaterDialog mWaterDialog;


    public void showProgress() {
        mWaterDialog.showProgress(getActivity(),"请稍等");
    }

    public void showMessage(String message) {
        mWaterDialog.showMessage(getActivity(), message);
    }


    public void dissmissProgress() {
        mWaterDialog.dismiss();
    }

    @Override
    @Deprecated
    protected void onLastViewCreate(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        onLastViewCreate(savedInstanceState);
        return view;
    }

    public abstract void onLastViewCreate(@Nullable Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetached();
    }
}
