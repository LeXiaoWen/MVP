package cree.mvp.net.bean;


import cree.mvp.base.bean.BaseBean;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/16  23:02
 *
 * @author luyongjiang
 * @version 1.0
 */
public class LoginBean extends BaseBean<LoginBean.DataBean> {


    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    ", create_date='" + create_date + '\'' +
                    ", permission_id='" + permission_id + '\'' +
                    ", head_img='" + head_img + '\'' +
                    '}';
        }

        /**
         * id : 1
         * user_name : Cree
         * password : 123
         * email : cree@qq.com
         * create_date : 2017-03-13 10:03:53
         * permission_id : 1
         * head_img : /img2017-03-13/58c607a5dd35f.jpg
         */


        private String id;
        private String user_name;
        private String password;
        private String email;
        private String create_date;
        private String permission_id;
        private String head_img;
        private boolean isAuto;

        public boolean isAuto() {
            return isAuto;
        }

        public void setAuto(boolean auto) {
            isAuto = auto;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getPermission_id() {
            return permission_id;
        }

        public void setPermission_id(String permission_id) {
            this.permission_id = permission_id;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }
    }
}
