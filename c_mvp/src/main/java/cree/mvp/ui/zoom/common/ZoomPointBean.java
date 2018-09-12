package cree.mvp.ui.zoom.common;

/**
 * Title:缩放平移所需要用到的类
 * Description:
 * CreateTime:2017/7/19  18:32
 *
 * @author luyongjiang
 * @version 1.0
 */
public class ZoomPointBean {
    //X方向缩放倍数(按所给值的百分比缩放2.0=200%)
    private float sx;
    //Y方向缩放倍数(按所给值的百分比缩放2.0=200%)
    private float px;
    //缩放时用的X轴中心坐标(以像素为单位)
    private float sy;
    //缩放时用的y轴中心坐标(以像素为单位)
    private float py;

    public float getSx() {
        return sx;
    }

    public ZoomPointBean setSx(float sx) {
        this.sx = sx;
        return this;
    }

    public float getPx() {
        return px;
    }

    public ZoomPointBean setPx(float px) {
        this.px = px;
        return this;

    }

    public float getSy() {
        return sy;
    }

    public ZoomPointBean setSy(float sy) {
        this.sy = sy;
        return this;

    }

    public float getPy() {
        return py;
    }

    public ZoomPointBean setPy(float py) {
        this.py = py;
        return this;
    }


    public static ZoomPointBean getPointBean() {
        return new ZoomPointBean();
    }

    @Override
    public String toString() {
        return "ZoomPointBean{" +
                "sx=" + sx +
                ", px=" + px +
                ", sy=" + sy +
                ", py=" + py +
                '}';
    }
}
