package cree.mvp.util.udp;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/22  14:21
 *
 * @author luyongjiang
 * @version 1.0
 */
public class UDPConnectBean {
    private boolean mIsUdpStart = false;
    private String message = "UDP接收数据成功";

    public boolean isUdpStart() {
        return mIsUdpStart;
    }

    public void setUdpStart(boolean udpStart) {
        mIsUdpStart = udpStart;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
