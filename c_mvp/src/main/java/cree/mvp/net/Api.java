package cree.mvp.net;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cree.mvp.net.progressmanager.ProgressManager;
import cree.mvp.util.data.AppUtils;
import cree.mvp.util.data.ScreenUtils;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
public class Api {
    public Retrofit retrofit;
    public ApiService service;
    public static Context context;

    private static String url;
    private OkHttpClient mOkHttpClient;


    private static class SingletonHolderUrl {
        private static Api INSTANCE = new Api();

        public static void updateApi() {
            INSTANCE = new Api();
        }

        public static void release() {
            INSTANCE = null;
        }
    }

    public static Api getInstance() {
        if (context == null)
            throw new RuntimeException("需要init Api");
        return SingletonHolderUrl.INSTANCE;
    }

    public static void init(Context con) {
        context = con;
    }

    public static void init(Context con, String url) {
        Api.context = con;
        Api.url = url;
        if (SingletonHolderUrl.INSTANCE != null) {
            SingletonHolderUrl.updateApi();
        }
    }

    Interceptor headInterceptor = (chain) -> chain.proceed(chain.request().newBuilder()
            .addHeader("CLI_INFO", "phone&Android&" + AppUtils.getAppVersionName(context) + "&" + ScreenUtils.getScreenWidth() + "," + ScreenUtils.getScreenHeight())
//            .addHeader("Content-Type", "application/json")
            .build());



    //构造方法私有
    private Api() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File cacheFile = new File(context.getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .addInterceptor(headInterceptor)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache);

        mOkHttpClient = ProgressManager.getInstance().with(okhttpBuilder)
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Api.url == null ? Url.baseUrl : Api.url)//设置基础的请求地址
                .build();
        service = retrofit.create(ApiService.class);
    }


    public <T> T create(final Class<T> service) {
        T t = retrofit.create(service);
        return t;
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //判断是否有网络
//            if (!NetWorkUtil.isNetConnected(App.getAppContext())) {
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                LogUtils.d("no network");
//            }
//
//            Response originalResponse = chain.proceed(request);
//            if (NetWorkUtil.isNetConnected(App.getAppContext())) {
//                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//                String cacheControl = request.cacheControl().toString();
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", cacheControl)
//                        .removeHeader("Pragma")
//                        .build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                        .removeHeader("Pragma")
//                        .build();
//            }
            return chain.proceed(request);
        }
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}