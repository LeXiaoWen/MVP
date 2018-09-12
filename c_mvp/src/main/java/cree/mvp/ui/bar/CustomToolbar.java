package cree.mvp.ui.bar;

import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cree.mvp.R;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/9  09:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomToolbar implements BarView{
    private final AppCompatActivity mActivity;
    private LinearLayout mLlLeft;
    private LinearLayout mLlRight;
    private LinearLayout mLlCenter;
    private RelativeLayout mRlBar;
    private ViewGroup mTBar;

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
        View barView = LayoutInflater.from(activity).inflate(R.layout.cmvp_toolbar, null);
        //视图根布局
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        int childCount = viewGroup.getChildCount();
        //setContent的布局
        ViewGroup rootChildView = null;
        if (childCount > 0) {
            rootChildView = (ViewGroup) viewGroup.getChildAt(0);
        }

        if (rootChildView != null) {
            //创建用来包容setContent的布局
            ViewGroup rootGroup = null;
            if (patterns == VIEW_CONVER_PATTERNS.LINEAR_LAYOUT) {
                rootGroup = new LinearLayout(activity);
                rootGroup.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ((LinearLayout) rootGroup).setOrientation(LinearLayout.VERTICAL);

            } else if (patterns == VIEW_CONVER_PATTERNS.FRAME_LAYOUT) {
                rootGroup = new FrameLayout(activity);
                rootGroup.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            viewGroup.removeView(rootChildView);
            rootGroup.addView(rootChildView);
            rootGroup.addView(barView);
            viewGroup.addView(rootGroup);
        } else {
            return;
        }
        mTBar = (ViewGroup) activity.findViewById(R.id.t_bar);
        if (mTBar != null) {
            mLlLeft = (LinearLayout) mTBar.findViewById(R.id.ll_leftGroup);
            mLlRight = (LinearLayout) mTBar.findViewById(R.id.ll_rightGroup);
            mLlCenter = (LinearLayout) mTBar.findViewById(R.id.ll_centerGroup);
            mRlBar = (RelativeLayout) mTBar.findViewById(R.id.rl_barGroup);
        }

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


}
