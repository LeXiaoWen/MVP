package cree.mvp.util.leakcanary;

import com.squareup.leakcanary.LeakCanary;

import cree.mvp.base.activity.BaseApplication;
import cree.mvp.util.develop.LogUtils;

/**
 * Title:内存泄露工具
 * Description:
 * CreateTime:2017/6/21  15:07
 *
 * @author luyongjiang
 * @version 1.0
 */
public class LeakCanaryUtils {
    public static void init(BaseApplication application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            LogUtils.e("//---------------------------leakcanary分析中---------------------------------");
            return;
        }
        application.setRefWatcher(LeakCanary.install(application));
    }
}
