package cree.mvp.base.activity.v7.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import cree.mvp.R;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/6  10:46
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseMenuAdapter<T> {
    private T mData;
    private MenuType mMenuType;
    private ViewGroup mViewGroup;
    private AppCompatActivity mActivity;
    private String mFragmentTag;

    public abstract T bindData();


    public void init() {
        mData = bindData();
        mFragmentTag = mData.getClass().toString();
        if (mData instanceof Fragment || mData instanceof android.app.Fragment)
            mMenuType = MenuType.FRAGMENT;
        else if (mData instanceof View)
            mMenuType = MenuType.VIEW;
        else
            throw new RuntimeException("只能绑定View或者Fragment数据");
    }


    private void menuType(MenuType menuType) {
        if (mData == null)
            return;
        switch (menuType) {
            case FRAGMENT:
                initFragment();
                break;
            case VIEW:
                initView();
                break;
        }
    }


    public void refreshView(ViewGroup viewGroup, AppCompatActivity activity) {
        mActivity = activity;
        mViewGroup = viewGroup;
        menuType(mMenuType);
    }

    private void initFragment() {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
//        transaction.addToBackStack(null);
        transaction.replace(R.id.fl_start, (Fragment) mData, mFragmentTag);
        transaction.commit();
    }

    private void initView() {
        mViewGroup.removeAllViews();
        mViewGroup.addView((View) mData);
    }


    public T getData() {
        return mData;
    }
}
