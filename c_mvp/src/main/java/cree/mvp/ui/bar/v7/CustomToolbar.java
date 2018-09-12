package cree.mvp.ui.bar.v7;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cree.mvp.R;
import cree.mvp.ui.bar.BarView;
import cree.mvp.ui.bar.CustomToolbarView;
import cree.mvp.ui.bar.VIEW_CONVER_PATTERNS;

/**
 * Title:支持___V7____的自定义Toolbar控件
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/9  09:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomToolbar implements BarView, BarControl {
    private final AppCompatActivity mActivity;
    //bar左边的位置的容器
    private LinearLayout mLlLeft;
    //bar右边位置的容器
    private LinearLayout mLlRight;
    //bar中间位置的容器
    private LinearLayout mLlCenter;
    //自定义Barview容器
    private RelativeLayout mRlBar;
    //最外层容器
    private ViewGroup mTBar;
    //V7包的Toolbar
    private Toolbar mToolbar;
    //BarView的控制器
    private BarControl mBarControl;
    //包括setContentView的父布局
    public ViewGroup mContentView;

    public CustomToolbar(AppCompatActivity activity) {
        this.mActivity = activity;
        initToolBarAsHome("");
    }

    public CustomToolbar(AppCompatActivity activity, VIEW_CONVER_PATTERNS view_conver_patterns) {
        this.mActivity = activity;
        if (view_conver_patterns == VIEW_CONVER_PATTERNS.LINEAR_LAYOUT)
            initLinearBarAsHome("");
        if (view_conver_patterns == VIEW_CONVER_PATTERNS.FRAME_LAYOUT)
            initFrameBarAsHome("");
    }


    public CustomToolbar(AppCompatActivity activity, String title) {
        this.mActivity = activity;
        initToolBarAsHome(title);
    }

    private void initToolBarAsHome(String title) {
        initToolBarAsHome(title, mActivity, VIEW_CONVER_PATTERNS.LINEAR_LAYOUT);
    }


    private void initFrameBarAsHome(String title) {
        initToolBarAsHome(title, mActivity, VIEW_CONVER_PATTERNS.FRAME_LAYOUT);
    }

    private void initLinearBarAsHome(String title) {
        initToolBarAsHome(title, mActivity, VIEW_CONVER_PATTERNS.LINEAR_LAYOUT);
    }


    private void initToolBarAsHome(String title, AppCompatActivity activity, VIEW_CONVER_PATTERNS patterns) {
        //---------------------------初始化控件---------------------------------
        View barView = initView(activity, patterns);
        if (barView == null) return;
        //---------------------------初始化toolbar---------------------------------
        iniToolbar(barView);
        mBarControl = new CustomToolbarControl(mTBar);
    }

    private void iniToolbar(View barView) {
        mToolbar = (CustomToolbarView) barView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
    }

    @Nullable
    private View initView(AppCompatActivity activity, VIEW_CONVER_PATTERNS patterns) {
        View barView = LayoutInflater.from(activity).inflate(R.layout.cmvp_v7_toolbar, null);
        //放置内容的布局
        //---------------------------线性布局---------------------------------
        ViewGroup groupLinear = (FrameLayout) barView.findViewById(R.id.f_linearLayout);
        //--------------------------帧布局---------------------------------
        ViewGroup groupFrame = (FrameLayout) barView.findViewById(R.id.f_frameLayout);
        //视图根布局
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        int childCount = viewGroup.getChildCount();
        //setContent的布局
        ViewGroup rootChildView = null;
        if (childCount > 0) {
            rootChildView = (ViewGroup) viewGroup.getChildAt(0);
        }
        if (rootChildView != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(barView);
            if (patterns == VIEW_CONVER_PATTERNS.FRAME_LAYOUT) {
                mContentView = groupFrame;
                groupFrame.addView(rootChildView);
            } else {
                mContentView = groupLinear;
                groupLinear.addView(rootChildView);
            }
        } else {
            return null;
        }
        mTBar = (ViewGroup) activity.findViewById(R.id.t_bar);
        if (mTBar != null) {
            mLlLeft = (LinearLayout) mTBar.findViewById(R.id.ll_leftGroup);
            mLlRight = (LinearLayout) mTBar.findViewById(R.id.ll_rightGroup);
            mLlCenter = (LinearLayout) mTBar.findViewById(R.id.ll_centerGroup);
            mRlBar = (RelativeLayout) mTBar.findViewById(R.id.rl_barGroup);
        }
        return barView;
    }

    public void setBarBackGroundColor(int color) {
        mRlBar.setBackgroundColor(color);
    }

    public void setBarBackGroundColorResource(@ColorRes int color) {
        mRlBar.setBackgroundResource(color);
    }


    public void addLeftView(View view) {
        mLlLeft.addView(view);
    }

    public void addLeftView(View view, int index) {
        mLlLeft.addView(view, index);
    }

    public void addRightView(View view) {
        mLlRight.addView(view);
    }

    public void addRightView(View view, int index) {
        mLlRight.addView(view, index);
    }

    public void addCenterView(View view) {
        mLlCenter.addView(view);
    }

    public void addCenterView(View view, int index) {
        mLlCenter.addView(view, index);
    }

    public void addBarView(View view) {
        mTBar.addView(view);
    }

    public void addBarView(View view, int index) {
        mTBar.addView(view, index);
    }

    public void clearBarChildAllView() {
        mTBar.removeAllViews();
    }

    public void replaceBarChildAllView(View view) {
        clearBarChildAllView();
        mTBar.addView(view);
    }

    public void clearLeftView() {
        mLlLeft.removeAllViews();
    }

    public void clearRightView() {
        mLlRight.removeAllViews();
    }

    public void clearCenterView() {
        mLlCenter.removeAllViews();
    }

    public void removeLeftView(View view) {
        mLlLeft.removeView(view);
    }

    public void removeLeftView(int index) {
        mLlLeft.removeViewAt(index);
    }

    public void removeCenterView(View view) {
        mLlCenter.removeView(view);
    }

    public void removeCenterView(int index) {
        mLlCenter.removeViewAt(index);
    }

    public void removeRightView(View view) {
        mLlRight.removeView(view);
    }

    public void removeRightView(int index) {
        mLlRight.removeViewAt(index);
    }


    public void addLeftClickListener(OnBarClickListener onBarClickListener) {
        mLlLeft.setOnClickListener(v -> onBarClickListener.onClick(v));
    }

    public void addRightClickListener(OnBarClickListener onBarClickListener) {
        mLlRight.setOnClickListener(v -> onBarClickListener.onClick(v));
    }

    public void addCenterClickListener(OnBarClickListener onBarClickListener) {
        mLlCenter.setOnClickListener(v -> onBarClickListener.onClick(v));
    }


    //---------------------------Toolbar相关---------------------------------
    public Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    public void setTitle(String name) {
        mBarControl.setTitle(name);
    }

    @Override
    public void setBackButton(Boolean show, BarView.OnBarClickListener onBarClickListener) {
        mBarControl.setBackButton(show, onBarClickListener);
    }

    @Override
    public void addRightButton(@DrawableRes int drawable, OnBarClickListener onBarClickListener) {
        mBarControl.addRightButton(drawable, onBarClickListener);
    }

    @Override
    public void addRightButton(Drawable drawable, OnBarClickListener onBarClickListener) {
        mBarControl.addRightButton(drawable, onBarClickListener);
    }

    @Override
    public void releaseBar() {
        mBarControl.releaseBar();
    }

    @Override
    public void showRight() {
        mBarControl.showRight();
    }

    @Override
    public void hideRight() {
        mBarControl.hideRight();
    }
}
