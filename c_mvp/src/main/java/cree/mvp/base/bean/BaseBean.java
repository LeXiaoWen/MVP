package cree.mvp.base.bean;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/16  21:59
 *
 * @author luyongjiang
 * @version 1.0
 */
public class BaseBean<T> {
    /**
     * code : 0
     * message : 成功
     * data : [{"id":"1","user_name":"Cree","password":"123","email":"cree@qq.com","create_date":"2017-03-13 10:03:53","permission_id":"1","head_img":"/img2017-03-13/58c607a5dd35f.jpg"}]
     */
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
