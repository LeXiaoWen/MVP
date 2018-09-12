package cree.mvp.base.activity;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import cree.mvp.util.develop.DevelopUtil;
import cree.mvp.util.develop.LogUtils;
import cree.mvp.util.leakcanary.LeakCanaryUtils;


/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/14  12:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BaseApplication extends Application {
    private RefWatcher refWatcher;

    public RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public void setRefWatcher(RefWatcher refWatcher) {
        this.refWatcher = refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isOpenLeakCanary()) {
            if (DevelopUtil.isDebug) {
                LeakCanaryUtils.init(this);
            }
        }
//        initLogger();
//        Api.init(this);
//        Utils.init(this);
//        ToastUtils.init(false);
//        DatabaseHelper.init(this);
//        SelectImg.init();
//        Fresco.initialize(this);
    }

    /**
     * 应用级别的关闭,可以强制让整个应用从内存种清除,一般情况不要调用
     */
    public void applicationShutDown() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //    /**
//     * 初级化日志工具
//     */
//    public void initLogger() {
//        LogUtils.init(null);
//    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//
//    }
//


    @Override
    public void onTerminate() {
        LogUtils.d("application在终止了");
        super.onTerminate();
    }

    public boolean isOpenLeakCanary() {
        return true;
    }


}
