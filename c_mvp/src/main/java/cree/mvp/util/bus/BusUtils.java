package cree.mvp.util.bus;

import org.greenrobot.eventbus.EventBus;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/5  10:09
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BusUtils {
    /**
     * 请求一个事情个总线,总线负责分配
     *
     * @param event
     */
    public static void postMessage(Object event) {
        EventBus.getDefault().post(event);
    }
}
