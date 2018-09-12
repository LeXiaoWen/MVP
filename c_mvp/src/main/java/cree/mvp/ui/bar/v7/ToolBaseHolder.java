package cree.mvp.ui.bar.v7;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/8  17:56
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ToolBaseHolder<T extends View> {
    public T mItemView;

    public ToolBaseHolder(T itemView) {
        this.mItemView = itemView;
    }


    <E> E $(@IdRes int id) {
        return (E) mItemView.findViewById(id);
    }
}
