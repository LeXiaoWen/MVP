package cree.mvp.ui.bar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ViewGroup;

import cree.mvp.R;
import cree.mvp.util.data.ScreenUtils;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/7  10:23
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomToolbarView extends Toolbar {
    public CustomToolbarView(Context context) {
        super(context);
    }

    public CustomToolbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomToolbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        synFrameLayout();
    }

    public void synFrameLayout() {
        ViewGroup group = (ViewGroup) findViewById(R.id.t_bar);
        ViewGroup.LayoutParams layoutParams = group.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth();
        group.setX(0);
        group.setLayoutParams(layoutParams);
    }

}
