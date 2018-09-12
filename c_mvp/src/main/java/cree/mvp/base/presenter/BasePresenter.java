package cree.mvp.base.presenter;


import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import cree.mvp.base.activity.BaseActivity;
import cree.mvp.base.model.BaseModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Title:presenter基类,中间控制类
 * Description:不能在构造里面对activity做操作
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  21:17
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BasePresenter<T extends BaseActivity, E extends BaseModel> {


    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    protected T mActivity;

    @Inject
    protected E mModel;

    /**
     * 会在activity初始化view之后调用,一般在这里面对activity中的控件初始化一些数据才会用到
     */
    public void onCreateView() {

    }

    @Inject
    public void setPresenter() {
        mModel.setPresenter(this);
    }

    public void setActivity(T mActivity) {
        this.mActivity = mActivity;
    }

    public void onDetached() {
        mModel.onDetached();
        mActivity = null;
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }


    public void execute(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }
}
