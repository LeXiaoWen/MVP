package cree.mvp.util.permissions;

import android.app.Activity;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.lang.ref.SoftReference;

import cree.mvp.util.permissions.rx.PerAction1;
import cree.mvp.util.permissions.rx.PerActionError;
import cree.mvp.util.permissions.rx.PerSubscriber;
import rx.Observable;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/11  16:43
 *
 * @author luyongjiang
 * @version 1.0
 */
public class PermissionsUtils<T> {

    private RxPermissions rxPermissions;
    private Observable<Boolean> mRequest;

    private PermissionsUtils(Activity activity) {
        rxPermissions = new RxPermissions(activity);
    }

    public static PermissionsUtils<Boolean> getInstance(Activity activity) {
        SoftReference<Activity> activitySoftReference = new SoftReference<>(activity);
        return new PermissionsUtils<>(activitySoftReference.get());
    }

    public PermissionsUtils<T> request(final String... permissions) {
        mRequest = rxPermissions.request(permissions);
        return this;
    }

    public void subscrbe(PerAction1 onNext, PerActionError onError) {
        mRequest.subscribe(onNext, onError);
    }

    public void subscrbe(PerSubscriber subscriber) {
        mRequest.subscribe(subscriber);
    }

}
