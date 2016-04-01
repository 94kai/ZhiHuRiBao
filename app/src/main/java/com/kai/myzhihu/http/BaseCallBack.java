package com.kai.myzhihu.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/14 21:26
 * 提供多种callback  继承自basecallback  然后可以对它的 成功 失败 错误 请求之前三个方法进行重写
 */
public abstract class BaseCallBack implements Callback {
    private Handler handler;
    public BaseCallBack() {
        handler=new Handler(Looper.getMainLooper());
    }
    @Override
    public void onFailure(Call call, IOException e) {

    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
    }
    /**
     * 执行请求之前  通常用来做请求前的提示信息的显示
     * create at 2016/3/14 21:32
    */
    public void onBeforeRequest(){}
    /**
     * 请求码为200-300之间时回调这个
     * create at 2016/3/14 21:33
    */
    public  void onSuccess(final Call call, final Response response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccessOnUiThread(call,response);
            }
        });
    }
    /**
     * 没有Failure
     * 请求码3xx 4xx 5xx时回调
     * create at 2016/3/14 21:34
    */
    public  void onError(Call call, Response response,int errorCode){}
    /**
     * 请求成功之后 操作UI
     * create at 2016/3/14 21:34
     */
    public  void onSuccessOnUiThread(Call call, Response response){}
}
