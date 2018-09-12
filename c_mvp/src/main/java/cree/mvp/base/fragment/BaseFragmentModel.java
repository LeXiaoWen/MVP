package cree.mvp.base.fragment;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/4/11  12:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BaseFragmentModel<T extends BaseFragmentPresenter> {
    T presenter;

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    public void onDetached() {

    }
}
