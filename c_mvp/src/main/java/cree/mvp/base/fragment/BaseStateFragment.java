package cree.mvp.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cree.mvp.R;
import cree.mvp.base.model.StateModel;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/3  14:45
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseStateFragment<T extends BaseFragmentPresenter> extends BaseFragment<T> implements StateModel.OnSateLinstener {


    private FrameLayout mFrameLayout;
    private View mStateView;
    private StateModel mStateModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        addStateView(inflater, container);
        mStateModel = new StateModel(mStateView);
        mStateModel.setSateLinstener(this);
        return mFrameLayout;
    }

    private void addStateView(LayoutInflater inflater, @Nullable ViewGroup container) {
        mFrameLayout = new FrameLayout(getActivity());
        mStateView = inflater.inflate(R.layout.fr_base_state_layout, container, false);
        mFrameLayout.addView(mRootView);
        mFrameLayout.addView(mStateView);
    }

    /**
     * 设置状态为没有值
     */
    public void setNormal() {
        mStateModel.setNormal();
    }

    /**
     * 设置正在加载中
     */
    public void setLoading() {
        mStateModel.setLoading();
    }

    /**
     * 设置为重试
     */
    public void setRetry() {
        mStateModel.setRetry();
    }

    @Override
    public void onRetry() {

    }

    /**
     * 设置数据请求成功
     */
    public void setSuccess() {
        if (mStateModel != null && mFrameLayout != null) {
//            mFrameLayout.removeView(mStateView);

            mStateModel.setSuccess();
        }
//        mStateView = null;
//        mStateModel = null;
    }
}
