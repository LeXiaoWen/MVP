package cree.mvp.base.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cree.mvp.base.activity.BaseApplication;
import cree.mvp.util.develop.DevelopUtil;
import cree.mvp.util.develop.LogUtils;
import cree.mvp.util.ui.SizeUtils;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/4/11  12:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseFragment<T extends BaseFragmentPresenter> extends Fragment {
    @Inject
    protected T mPresenter;
    private Unbinder mBinder;
    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 调用这个方法去获取activity
     *
     * @return
     */
    public Activity acquireActivity() {
        return super.getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingDagger2();
        mPresenter.setFragment(this);
        mPresenter.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null)
            return mRootView;
        if ((mRootView = onLayoutView(inflater, container, savedInstanceState)) != null)
            return mRootView;
        mRootView = inflater.inflate(onLayoutId(), container, false);
        //绑定framgent
        mBinder = ButterKnife.bind(this, mRootView);
        onLastViewCreate(inflater, savedInstanceState);
        mPresenter.onCreateView();
        return mRootView;
    }

    /**
     * 如果要代码里面创建view调用这个方法
     * 如果不是特殊需求只需要实现onLayoutId就可以了,这个两个方法只用实现一个
     * 一般都不会用这个方法,所以并没有把这个方法抽象化
     *
     * @param inflater           解析控件的
     * @param container          fragment的父控件
     * @param savedInstanceState 假如acivity被强制销毁或者复现的时候就会调用这个方法
     * @return
     */
    protected View onLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint())
            showFragment();
        else
            hideFragment();
    }

    /**
     * 实现方法返回需要加载的xml布局
     * 如果需要手动代码创建布局,则实现{@link #onLastViewCreate(LayoutInflater, Bundle)}
     *
     * @return LayoutRes
     */
    protected abstract int onLayoutId();

    /**
     * 当fragment创建并且butterknife解析控件完成后回调这个方法
     * 在这里面写一些数据初始化操作
     *
     * @param inflater
     * @param savedInstanceState
     */
    protected abstract void onLastViewCreate(LayoutInflater inflater, @Nullable Bundle savedInstanceState);


    protected abstract void bindingDagger2();


    protected void showFragment() {
    }

    protected void hideFragment() {
    }


    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this.getActivity(), clazz);
        startActivity(intent);
    }


    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }


    //---------------------------生命周期---------------------------------


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
        mRootView = null;
        mPresenter.onDestroyView();
        if (DevelopUtil.isDebug) {
            bindingLeakCanary();
        }
    }

    /**
     * 绑定内存泄露监听
     */
    private void bindingLeakCanary() {
        Application application = getActivity().getApplication();
        if (application instanceof BaseApplication) {
            if (((BaseApplication) application).isOpenLeakCanary()) {
                RefWatcher refWatcher = ((BaseApplication) application).getRefWatcher(getActivity());
                refWatcher.watch(this);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.onDetached();
        //---------------------------leakcanary---------------------------------
//        Application application = getActivity().getApplication();
//        if (application instanceof BaseApplication) {
//            RefWatcher refWatcher = ((BaseApplication) application).getRefWatcher(getActivity());
//            refWatcher.watch(this);
//        }
    }

    /**
     * 返回根布局的宽度
     *
     * @return
     */
    public int getRootViewWidth() {
        if (mRootView != null) {
            return SizeUtils.getMeasuredWidth(mRootView);
        }
        return 0;
    }

    /**
     * Default Subscribe
     */
    @Subscribe
    public void defaultSubscribe(Object o) {
        LogUtils.d("默认的订阅" + o.toString());
    }


}
