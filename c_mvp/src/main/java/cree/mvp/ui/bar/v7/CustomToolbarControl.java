package cree.mvp.ui.bar.v7;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cree.mvp.R;
import cree.mvp.ui.bar.BarView;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/8  17:27
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomToolbarControl implements BarControl {

    private Context mContext;
    private ViewGroup mGroupBar;
    private ViewHolder mHolder;

    public CustomToolbarControl(ViewGroup groupBar) {
        mContext = groupBar.getContext();
        mGroupBar = groupBar;
        mHolder = new ViewHolder(mGroupBar);

    }

    @Override
    public void setTitle(String name) {
        mHolder.mTvTitle.setText(name);
        mHolder.mTvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBackButton(Boolean show, BarView.OnBarClickListener onBarClickListener) {
        if (show) {
            mHolder.mIvBack.setVisibility(View.VISIBLE);
            mHolder.mIvBack.setOnClickListener(v -> onBarClickListener.onClick(v));
        } else {
            mHolder.mIvBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void addRightButton(@DrawableRes int drawable, BarView.OnBarClickListener onBarClickListener) {
        ImageView tvAddButton = inflaterAddRightView(onBarClickListener);
        tvAddButton.setImageResource(drawable);
    }

    @Override
    public void addRightButton(Drawable drawable, BarView.OnBarClickListener onBarClickListener) {
        ImageView tvAddButton = inflaterAddRightView(onBarClickListener);
        tvAddButton.setImageDrawable(drawable);
    }

    @Override
    public void releaseBar() {
        mContext = null;
    }


    private ImageView inflaterAddRightView(BarView.OnBarClickListener onBarClickListener) {
        View viewGroup = LayoutInflater.from(mContext).inflate(R.layout.cmvp_v7_toolbar_right_button, mHolder.mLlRightGroup, false);
        ImageView ivAddView = (ImageView) viewGroup.findViewById(R.id.iv_addView);
        if (onBarClickListener != null) {
            ivAddView.setOnClickListener(v -> onBarClickListener.onClick(v));
        }
        mHolder.mLlRightGroup.addView(viewGroup);
        return ivAddView;
    }


    @Override
    public void showRight() {
        mHolder.mLlRightGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRight() {
        mHolder.mLlRightGroup.setVisibility(View.GONE);
    }

    /**
     * View缓存
     */
    class ViewHolder extends ToolBaseHolder<ViewGroup> {
        LinearLayout mLlLeftGroup;
        TextView mTvTitle;
        LinearLayout mLlCenterGroup;
        LinearLayout mLlRightGroup;
        RelativeLayout mRlBarGroup;
        ImageView mIvBack;

        ViewHolder(ViewGroup view) {
            super(view);
            mItemView = view;
            mLlLeftGroup = $(R.id.ll_leftGroup);
            mTvTitle = $(R.id.tv_title);
            mLlCenterGroup = $(R.id.ll_centerGroup);
            mLlRightGroup = $(R.id.ll_rightGroup);
            mRlBarGroup = $(R.id.rl_barGroup);
            mIvBack = $(R.id.iv_back);
        }

    }
}
