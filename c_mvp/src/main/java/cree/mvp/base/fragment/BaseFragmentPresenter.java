package cree.mvp.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/4/11  12:03
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BaseFragmentPresenter<T extends BaseFragment, E extends BaseFragmentModel> {
    protected T mFragment;
    @Inject
    protected E mModel;

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject
    void setPresenter() {
        mModel.setPresenter(this);
    }

    public void setFragment(T fragment) {
        this.mFragment = fragment;
    }

    public void execute(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }


    public void onAttach(Context context) {

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    public void onCreateView() {

    }

    public void onResume() {
    }

    public void onPause() {
    }


    public void onDestroyView() {
    }


    public void onDetached() {
        mModel.onDetached();
        mFragment = null;
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }

}
