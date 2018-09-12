package cree.mvp.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cree.mvp.R;


/**
 * Title:
 * Description:
 * CreateTime:2017/6/12  11:22
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CrDialog extends AlertDialog {
    public static final int BUTTON_CANCEL = DialogInterface.BUTTON_NEGATIVE;
    public static final int BUTTON_OTHER = DialogInterface.BUTTON_NEUTRAL;
    public static final int BUTTON_CONFIRM = DialogInterface.BUTTON_POSITIVE;

    private TextView mTvContent;
    private ProgressBar mPbLoading;

    public CrDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CrDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void setTvContent(TextView tvContent) {
        this.mTvContent = tvContent;
    }


    public void setContentMessage(String message) {
        if (mTvContent != null && !TextUtils.isEmpty(message))
            mTvContent.setText(message);
    }

    public void setPbLoading(ProgressBar pbLoading) {
        mPbLoading = pbLoading;
    }

    public void setContentLoading(int progress) {
        if (mPbLoading != null)
            mPbLoading.setProgress(progress);
    }

    public void init(@NonNull Context context) {
//        setContentView(R.layout.cmvp_dialog_layout);
    }


    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {
        private final int NONE_THEME = 0;
        private Context mContext;
        private int mTheme;
        private View mProgressView;
        private View mLayoutView;
        private LayoutInflater mFrom;
        private CrDialog mCrDialog;

        private String mOtherMessage;
        private OnClickListener mOtherClickListener;
        private String mConfirmMessage;
        private OnClickListener mConfirmClickListener;
        private String mCancelMessage;
        private OnClickListener mCancelClickListener;
        private Boolean mNoAutoCancel;
        private ProgressBar mPbLoading;
        private String mTitle;
        private String mMessage;

        public Builder(Context context) {
            mContext = context;
            mFrom = LayoutInflater.from(mContext);
            mTheme = NONE_THEME;
            mNoAutoCancel = false;

        }


        public Builder isTransparent() {
            mTheme = R.style.CMVP_Dialog_Transparency;
            return this;
        }

        public Builder isProgress() {
            mProgressView = mFrom.inflate(R.layout.cmvp_dialog_layout, null, false);
            return this;
        }

        public Builder isHorizontalProgress() {
            mProgressView = mFrom.inflate(R.layout.cmvp_dialog_horizontal_layout, null, false);
            mPbLoading = (ProgressBar) mProgressView.findViewById(R.id.pb_loading);
            return this;
        }


        public Builder setLayoutId(@LayoutRes int layoutId) {
            mLayoutView = mFrom.inflate(layoutId, null, false);
            return this;
        }

        public Builder setLayoutView(View view) {
            mLayoutView = view;
            return this;
        }

        public Builder setOtherButton(String message, OnClickListener onClickListener) {
            mOtherMessage = message;
            mOtherClickListener = onClickListener;
            return this;

        }

        public Builder setConfirmButton(String message, OnClickListener onClickListener) {
            mConfirmMessage = message;
            mConfirmClickListener = onClickListener;
            return this;

        }

        public Builder setCancelButton(String message, OnClickListener onClickListener) {
            mCancelMessage = message;
            mCancelClickListener = onClickListener;
            return this;
        }


        public Builder isNoAutoCancel() {
            mNoAutoCancel = true;
            return this;
        }


        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }


        public CrDialog create() {
            //初始化对象
            mCrDialog = new CrDialog(mContext, mTheme);
            //设置自定义View
            if (mLayoutView != null)
                mCrDialog.setView(mLayoutView);
            //设置Other按钮的点击事件
            if (mOtherClickListener != null || mOtherMessage != null)
                mCrDialog.setButton(CrDialog.BUTTON_OTHER, mOtherMessage + "", mOtherClickListener);
            //设置Confirm按钮的点击事件
            if (mConfirmClickListener != null || mConfirmMessage != null)
                mCrDialog.setButton(CrDialog.BUTTON_CONFIRM, mConfirmMessage + "", mConfirmClickListener);
            //设置Cancel按钮的点击事件
            if (mCancelClickListener != null || mCancelMessage != null)
                mCrDialog.setButton(CrDialog.BUTTON_CANCEL, mCancelMessage + "", mCancelClickListener);
            //设置是否自动取消
            if (mNoAutoCancel) {
                mCrDialog.setCanceledOnTouchOutside(false);
                mCrDialog.setCancelable(false);
            }
            //设置信息

            //设置进度条
            if (mProgressView != null) {//如果需要进度条不为空,则完全使用自定义View的标题以及内容信息
                mCrDialog.setView(mProgressView);
                //设置默认显示信息
                if (mMessage != null) {
                    TextView tvContent = (TextView) mProgressView.findViewById(R.id.tv_content);
                    mCrDialog.setTvContent(tvContent);
                    tvContent.setText(mMessage);
                }
                //设置标题
                if (mTitle != null) {
                    TextView tvTitle = (TextView) mProgressView.findViewById(R.id.tv_title);
                    tvTitle.setText(mTitle);
                    tvTitle.setVisibility(View.VISIBLE);
                    View vCut = mProgressView.findViewById(R.id.v_cut);
                    if (vCut != null) {
                        vCut.setVisibility(View.VISIBLE);
                    }
                }
                //设置水平进度的控制条
                if (mPbLoading != null) {
                    mCrDialog.setPbLoading(mPbLoading);
                }

                return mCrDialog;
            }

            //设置默认显示信息
            if (mMessage != null)
                mCrDialog.setMessage(mMessage);
            //设置标题
            if (mTitle != null)
                mCrDialog.setTitle(mTitle);
            return mCrDialog;

        }

        public CrDialog show() {
            create().show();
            return mCrDialog;
        }

    }
}
