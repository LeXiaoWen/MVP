package cree.mvp.base.model;

import android.view.View;

import cree.mvp.R;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/3  15:18
 *
 * @author luyongjiang
 * @version 1.0
 */
public class StateModel implements View.OnClickListener {
    private View mNormalView;
    private View mLoadingView;
    private View mRetryView;
    private View mGroupView;
    private OnSateLinstener mSateLinstener;

    public StateModel(View loadingView) {
        mGroupView = loadingView;
        mNormalView = loadingView.findViewById(R.id.ll_normal);
        mLoadingView = loadingView.findViewById(R.id.ll_loading);
        mRetryView = loadingView.findViewById(R.id.ll_retry);
        init();
    }

    public View getNormalView() {
        return mNormalView;
    }

    public void setNormalView(View normalView) {
        mNormalView = normalView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
    }

    public View getRetryView() {
        return mRetryView;
    }

    public void setRetryView(View retryView) {
        mRetryView = retryView;
    }


    public void setSateLinstener(OnSateLinstener sateLinstener) {
        mSateLinstener = sateLinstener;
    }

    private void init() {
        mRetryView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isCallState()) {
            mSateLinstener.onRetry();
        }

    }

    private boolean isCallState() {
        return mSateLinstener != null;
    }

    public void setNormal() {
        mNormalView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.GONE);
    }


    public void setLoading() {
        mNormalView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mRetryView.setVisibility(View.GONE);
    }

    public void setRetry() {
        mNormalView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.VISIBLE);
    }

    public void setSuccess() {
        mNormalView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mRetryView.setVisibility(View.GONE);
        mGroupView.setVisibility(View.GONE);
    }

    public interface OnSateLinstener {
        void onRetry();
    }
}
