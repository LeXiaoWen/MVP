package cree.mvp.util.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import cree.mvp.util.bus.BusUtils;
import cree.mvp.util.data.NetworkUtils;
import cree.mvp.util.develop.LogUtils;

/**
 * Title:
 * Description:
 * CreateTime:2017/6/22  11:14
 *
 * @author luyongjiang
 * @version 1.0
 */
public class UDPListener {

    public static void init() {
        new UDPListener();
    }

    private UDPListener() {
        onCreate();
    }

    private boolean IsThreadDisable = false;
    private DatagramSocket mDatagramSocket;
    private boolean isReady = false;

    protected void onCreate() {
        new Thread(() -> StartListen()).start();
    }

    private void StartListen() {
        // UDP服务器监听的端口
        Integer port = 50017;
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] message = new byte[1024];
        try {
            DatagramPacket datagramPacket = null;
            try {
                // 建立Socket连接
                mDatagramSocket = new DatagramSocket(port);
                mDatagramSocket.setBroadcast(true);
                datagramPacket = new DatagramPacket(message,
                        message.length);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while (!IsThreadDisable) {
                // 准备接收数据
                if (!NetworkUtils.isWifiConnected()) {
                    return;
                }
                mDatagramSocket.receive(datagramPacket);
                if (datagramPacket.getAddress().toString().contains("127.0.0.1") || datagramPacket.getAddress().toString().contains(NetworkUtils.getIPAddress(true))) {
                    onWifiConnect();
                    return;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mDatagramSocket != null) {
                mDatagramSocket.close();
                mDatagramSocket = null;
            }
            onFwEroor();
        }
    }

    /**
     * wifi状态收到连接状态
     */
    private void onWifiConnect() {
        isReady = true;
        postMessage(2000);
    }

    /**
     * 蜂窝网络状态--
     */
    private void onFwEroor() {
        if (!isReady) {
            postMessage(12000);
        }
    }

    private void postMessage(int time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UDPConnectBean udpConnectBean = new UDPConnectBean();
            udpConnectBean.setUdpStart(true);
            BusUtils.postMessage(udpConnectBean);
            LogUtils.e("//---------------------------发送unity加载成功---------------------------------");
        }).start();

    }
}
