package cree.mvp.base.activity.v7.menu;

import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cree.mvp.R;
import cree.mvp.base.activity.v7.BaseCustomTitleActivity;
import cree.mvp.base.fragment.BaseFragment;
import cree.mvp.base.presenter.BasePresenter;
import cree.mvp.util.data.ConvertUtils;
import cree.mvp.util.ui.ResourceUtil;

/**
 * Title:支持侧滑菜单的Activity
 * Description:在需要侧滑菜单或者简介的时候可以用到这个activity
 * CreateTime:2017/6/5  17:39
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseMenuCustomTitleActivity<T extends BasePresenter> extends BaseCustomTitleActivity<T> implements DrawerLayout.DrawerListener {


    private DrawerLayout mDrawerLayout;
    private BaseMenuAdapter<Object> mMenuAdapter;
    private FrameLayout mFrameLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initDrawableLayout();
    }

    private void initDrawableLayout() {
        initDrawer();
        bindingActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        menuMeausre();

    }

    /**
     * 替换barView
     */
    private void bindingActionBar() {
        //初始化menu按钮
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mCustomToolbar.getToolbar(), R.string.cmvp_open, R.string.cmvp_close);
        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(this);
        arrowDrawable.setColor(ResourceUtil.getColor(this, R.color.cmvp_white));
        drawerToggle.setDrawerArrowDrawable(arrowDrawable);
        //为drawerlayout添加监听事件
        mDrawerLayout.addDrawerListener(drawerToggle);
        //menu按钮同步状态
        drawerToggle.syncState();
        ViewGroup replaceGroup = (ViewGroup) mDrawerLayout.findViewById(R.id.fl_start);
        //获取适配器
        mMenuAdapter = bindAdapter();
        if (mMenuAdapter != null) {
            //初始化适配器
            mMenuAdapter.init();
            //根据适配器刷新View到视图上面
            mMenuAdapter.refreshView(replaceGroup, this);
            //---------------------------对菜单栏修改宽度设置 通过获取子View的宽度设置,dp转px做适配---------------------------------
            
        }
    }

    /**
     * 测量各个部件的高度
     */
    private void menuMeausre() {
        Object data = mMenuAdapter.getData();
        if (data instanceof View) {
            measureView((View) data);
        } else if (data instanceof BaseFragment) {
            measureFragment((BaseFragment) data);
        }
    }

    /**
     * 测量Fragment的高度,只有继承{@link BaseFragment}的才会有效果
     *
     * @param data 需要测量的Fragment
     */
    private void measureFragment(BaseFragment data) {
        int width = data.getRootViewWidth();
        ViewGroup.LayoutParams layoutParams = mFrameLayout.getLayoutParams();
        layoutParams.width = width;
        mFrameLayout.setLayoutParams(layoutParams);
    }

    /**
     * 测量View的高度
     *
     * @param data 需要测量的控件
     */
    private void measureView(View data) {
        View view = data;
        view.measure(0, 0);
        int width = view.getMeasuredWidth();
        ViewGroup.LayoutParams layoutParams = mFrameLayout.getLayoutParams();
        layoutParams.width = ConvertUtils.dp2px(width);
        mFrameLayout.setLayoutParams(layoutParams);
    }

    /**
     * 初始化布局
     */
    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) LayoutInflater.from(this).inflate(R.layout.cmvp_v7_menu_layout, null, false);
        mDrawerLayout.addDrawerListener(this);
        int childCount = mCustomToolbar.mContentView.getChildCount();
        if (childCount > 0) {
            View contentView = mCustomToolbar.mContentView.getChildAt(0);
            mCustomToolbar.mContentView.removeAllViews();
            mCustomToolbar.mContentView.addView(mDrawerLayout);
            mDrawerLayout.addView(contentView, 0);
        }
        //放置菜单的View
        mFrameLayout = (FrameLayout) mDrawerLayout.findViewById(R.id.fl_start);
    }

    public abstract <E> BaseMenuAdapter<E> bindAdapter();

    //---------------------------listener---------------------------------
    public void openMenu() {
        mDrawerLayout.openDrawer(GravityCompat.START, true);
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawer(GravityCompat.END, true);
    }

    public boolean isOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    //---------------------------end listener---------------------------------
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
