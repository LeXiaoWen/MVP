package cree.mvp.util.anim.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import cree.mvp.util.anim.AnimController;
import cree.mvp.util.anim.AnimFactory;


/**
 * Title:
 * Description:
 * CreateTime:2017/8/11  09:41
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomClickViewLayout extends FrameLayout {
    private String mAnimTag = "alpha";
    public static final String ALPHA_ANIM = "alpha";
    public static final String SCALE_ANIM = "scale";
    public static final String TRANSLATION_ANIM = "translation";
    public static final String ROTATE_ANIM = "rotate";
    public static final String COLOR_ANIM = "color";
    private AnimController mAnimController;

    public CustomClickViewLayout(@NonNull Context context) {
        super(context);
    }

    public CustomClickViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomClickViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDownAnim();
                break;

            case MotionEvent.ACTION_UP:
                startCancelAnim();

                break;
            case MotionEvent.ACTION_CANCEL:
                startCancelAnim();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setTag(String tag) {
        this.mAnimTag = tag;
        if (mAnimTag.equals(ALPHA_ANIM)) {//透明度
            mAnimController = AnimFactory.createAlphaAnim(this);
        } else if (mAnimTag.equals(SCALE_ANIM)) {//缩放
            mAnimController = AnimFactory.createScaleAnim(this);
        } else if (mAnimTag.equals(ROTATE_ANIM)) {//旋转
            mAnimController = AnimFactory.createRotateAnim(this);
        } else if (mAnimTag.equals(COLOR_ANIM)) {//颜色
            mAnimController = AnimFactory.createColorAnim(this);
        } else if (mAnimTag.equals(TRANSLATION_ANIM)) {//水平移动
//            mAnimController = AnimFactory.creattr(this);
        }
    }

    private void startDownAnim() {
        mAnimController.start();
    }

    private void startCancelAnim() {
        mAnimController.recover();
    }
}
