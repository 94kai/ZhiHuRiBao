package com.kai.myzhihu.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 凯 on 2016/3/14 17:54
 */
public class OKHttpHelper {
    static {
        okHttpHelper = new OKHttpHelper();
    }

    private Gson gson;
    private static OKHttpHelper okHttpHelper;
    private OkHttpClient okHttpClient;
    private Handler handler;


    private OKHttpHelper() {
        okHttpClient = new OkHttpClient();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OKHttpHelper getInstance() {
        return okHttpHelper;
    }


    public void doGet(String uri, BaseCallBack callBack) {
        Request request = buildRequest(uri, Method.GET, null);
        request(request, callBack);

    }


    public void doPost(String uri, Map<String, String> params, BaseCallBack callBack) {
        Request request = buildRequest(uri, Method.POST, params);
        request(request, callBack);

    }


    /**
     * 构建请求对象
     *
     * @param uri    请求uri
     * @param method 请求方式
     * @param params 如果是post请求 传入一个map集合 否则传入空
     * @return 请求对象
     * create at 2016/3/14 21:00
     * @author 凯
     */
    private Request buildRequest(String uri, Method method, Map<String, String> params) {
        Request request = null;
        Request.Builder builder = new Request.Builder().url(uri);
        if (method == Method.GET) {
            //构建get请求的request
            request = builder.build();
        } else if (method == Method.POST) {
            //构建post请求的request
            FormBody.Builder formBody = new FormBody.Builder();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBody.add(entry.getKey(), entry.getValue());
            }
            request = builder.post(formBody.build()).build();
        }
        return request;
    }


    private void request(Request request, final BaseCallBack callBack) {
        //请求之前调用
        callBack.onBeforeRequest();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败调用
                callBack.onFailure(call, e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(call,response);
                if (response.isSuccessful()) {
                    //请求成功调用
                    callBack.onSuccess(call, response);
                } else {
                    //结果出错错误调用
                    callBack.onError(call, response, response.code());
                }
            }
        });
    }

    enum Method {
        GET, POST
    }

}
