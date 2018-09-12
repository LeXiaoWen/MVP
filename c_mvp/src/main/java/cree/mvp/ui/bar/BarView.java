package cree.mvp.ui.bar;

import android.support.annotation.ColorRes;
import android.view.View;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/5  16:30
 *
 * @author luyongjiang
 * @version 1.0
 */
public interface BarView {

    void setBarBackGroundColor(int color);

    void setBarBackGroundColorResource(@ColorRes int color);


    void addLeftView(View view);

    void addLeftView(View view, int index);

    void addRightView(View view);

    void addRightView(View view, int index);

    void addCenterView(View view);

    void addCenterView(View view, int index);

    void addBarView(View view);

    void addBarView(View view, int index);


    void clearBarChildAllView();

    void replaceBarChildAllView(View view);

    void clearLeftView();

    void clearRightView();

    void clearCenterView();


    void removeLeftView(View view);

    void removeLeftView(int index);

    void removeCenterView(View view);

    void removeCenterView(int index);

    void removeRightView(View view);

    void removeRightView(int index);


    void addLeftClickListener(OnBarClickListener onBarClickListener);

    void addRightClickListener(OnBarClickListener onBarClickListener);

    void addCenterClickListener(OnBarClickListener onBarClickListener);

    interface OnBarClickListener {
        void onClick(View view);
    }


}
