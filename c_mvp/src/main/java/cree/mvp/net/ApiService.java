package cree.mvp.net;


import cree.mvp.net.bean.LoginBean;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2017/3/16  22:05
 *
 * @author luyongjiang
 * @version 1.0
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("login/login")
    Observable<LoginBean> login(@Field("username") String username, @Field("password") String password);

    @Multipart
    @POST("login/register")
    Observable<LoginBean> register(@Part("username") RequestBody nameBody, @Part("password") RequestBody passwordBody, @Part("photo\"; filename=\"photo.jpg\"") RequestBody fileBody);


}
