package cree.mvp.base.model;

import cree.mvp.base.presenter.BasePresenter;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/15  19:19
 *
 * @author luyongjiang
 * @version 1.0
 */
public abstract class BaseModel<T extends BasePresenter> {
    protected T mPresenter;


    public void setPresenter(T mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void onDetached() {
    }


}
