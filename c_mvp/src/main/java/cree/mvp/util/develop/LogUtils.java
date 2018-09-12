package cree.mvp.util.develop;

import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * <pre>
 *     author: luyongjiang
 *     time  : 2016/9/21
 *     desc  : 日志相关工具类
 * </pre>
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String TAG = "cree";
    private static int methodCount = 1;
    private static int methodOffset = 1;

    public static void init(@Nullable String tag) {
        if (tag == null)
            Logger.init(TAG).methodCount(methodCount).methodOffset(methodOffset);
        else
            Logger.init(tag).methodCount(methodCount).methodOffset(methodCount);
    }


    public static void d(Object obj) {
        if (DevelopUtil.isDebug)
            Logger.d(obj);
    }


    public static void d(String message, Object... args) {
        if (DevelopUtil.isDebug)
            Logger.d(message, args);
    }

    public static void e(String message, Object... args) {
        if (DevelopUtil.isDebug)
            Logger.e(message, args);
    }

    public static void w(String message, Object... args) {

        if (DevelopUtil.isDebug)
            Logger.w(message, args);
    }

    public static void v(String message, Object... args) {

        if (DevelopUtil.isDebug)
            Logger.v(message, args);
    }

    public static void wtf(String message, Object... args) {

        if (DevelopUtil.isDebug)
            Logger.wtf(message, args);
    }

    public static void json(String message) {

        if (DevelopUtil.isDebug)
            Logger.json(message);
    }

    public static void xml(String message) {

        if (DevelopUtil.isDebug)
            Logger.xml(message);
    }

    public static void log(int priority, String tag, String message, Throwable throwable) {
        if (DevelopUtil.isDebug)
            Logger.log(priority, tag, message, throwable);
    }


}
