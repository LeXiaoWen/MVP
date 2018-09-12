package cree.mvp.base.activity;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cree.mvp.R;
import cree.mvp.base.presenter.BasePresenter;
import cree.mvp.util.develop.LogUtils;
import cree.mvp.util.ui.BarUtils;
import cree.mvp.util.ui.ResourceUtil;
import cree.mvp.util.ui.SnackBarUtil;


/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  11:53
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    @Inject
    public T mPresenter;
    Unbinder mUnBinder;
    private final static Set<BaseActivity> activityList = new HashSet<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bindingDagger2(savedInstanceState);
        if (mPresenter == null)
            throw new RuntimeException("没有绑定dagger2或没有重写onCreate()方法");
        mPresenter.setActivity(this);
        mPresenter.onCreate(savedInstanceState);
        //---------------------------leakcanary---------------------------------
//        Application application = getApplication();
//        if (application instanceof BaseApplication) {
//            RefWatcher refWatcher = ((BaseApplication) application).getRefWatcher(this);
//            refWatcher.watch(this);
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        activityList.add(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    protected abstract void bindingDagger2(@Nullable Bundle savedInstanceState);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        BarUtils.setColor(this, ResourceUtil.getColor(this, onBarColorResource()));
        mUnBinder = ButterKnife.bind(this);
        mPresenter.onCreateView();
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 显示bar提示条
     *
     * @param message
     */
    public void showBar(String message) {
        SnackBarUtil.showBar(getRootView(), message);
    }

    protected ViewGroup getRootView() {
        return (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        activityList.remove(this);
        if (mUnBinder != null)
            mUnBinder.unbind();
        mPresenter.onDetached();
        mUnBinder = null;
        mPresenter = null;
    }

    /**
     * 用这个方法来关掉activity
     */
    public void shutDown() {
        finish();
    }

    /**
     * 只是清除acitivty列表,不要轻易调用
     */
    public void clearAll() {
        activityList.clear();
    }

    /**
     * 清理列表并关闭activity
     */
    public void clearAndShutdown() {
        Iterator<BaseActivity> iterator = activityList.iterator();
        while (iterator.hasNext()) {
            BaseActivity next = iterator.next();
            iterator.remove();
            next.shutDown();
        }
        //---------------------------关闭程序---------------------------------
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).applicationShutDown();
        }
    }


    /**
     * Default Subscribe
     */
    @Subscribe
    public void defaultSubscribe(Object o) {
        LogUtils.d("默认的订阅" + o.toString());
    }


    protected int onBarColorResource() {
        return R.color.colorPrimary;
    }


    /**
     * 添加fragment到栈的模式中去
     *
     * @param fragment
     */
    public void addFragmentToStack(Fragment fragment, @IdRes int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(id, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    /**
     * 普通的添加fragment
     *
     * @param fragment
     * @param id
     */
    public void addFragment(Fragment fragment, @IdRes int id) {
        Fragment findFragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName());
        if (findFragment == null) {
            getSupportFragmentManager().beginTransaction().add(id, fragment, fragment.getClass().getName()).commit();
        }
    }

    /**
     * 普通的移除Fragment
     *
     * @param fragment
     */
    public void removeFragment(Fragment fragment) {
        Fragment findFragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName());
        if (findFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    /**
     * 显示传入的fragment并且隐藏加入activity中的其他fragment
     *
     * @param fragment
     * @param id
     */
    public void showFragmentAndHideOther(Fragment fragment, @IdRes int id) {
        if (fragment != null) {
            FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
            tr.show(fragment);
            tr.commit();
        }
        @SuppressLint("RestrictedApi") List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment forFragment : fragments) {
            if (!(forFragment == fragment)) {
                FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
                tr.hide(forFragment);
                tr.commit();
            }
        }
    }

    /**
     * 移除栈顶的fragment{@link BaseActivity#backRemove}
     */
    public void popFragment() {
        backRemove();
    }

    private void backRemove() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        int size = backStackEntryCount;
        if (size == 0)
            return;
        onBackPressed();
    }

    /**
     * 得到栈顶的fragment
     *
     * @return
     */

    public Fragment peekFragment() {
        int index = getStackCount();
        if (getStackCount() < 0) {
            return null;
        }
        @SuppressLint("RestrictedApi") Fragment fragment = getSupportFragmentManager().getFragments().get(index);
        return fragment;
    }

    /**
     * 获取当前栈模式framgent中的总数
     *
     * @return
     */
    public int getStackCount() {
        return getSupportFragmentManager().getBackStackEntryCount() - 1;
    }

    /**
     * 清理加入栈中的fragment,只是针对调用{@link BaseActivity#addFragmentToStack(Fragment, int)}方法加入的Fragment
     */
    public void clear() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() + 3; i++) {
            backRemove();
        }
    }

}
