package com.kai.myzhihu.http;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 凯 on 2016/3/14 22:14
 */
public abstract class SpotsCallBack extends BaseCallBack{

    private final SpotsDialog dialog;

    public SpotsCallBack(Context context,String spotsText){
        dialog = new SpotsDialog(context);
        dialog.setMessage(spotsText);
    }
    @Override
    public void onBeforeRequest() {
        dialog.show();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        super.onFailure(call, e);
        dialog.dismiss();
    }

    @Override
    public void onSuccess(Call call, Response response) {
        super.onSuccess(call,response);
        dialog.dismiss();
    }

    @Override
    public void onError(Call call, Response response, int errorCode) {
        super.onError(call,response,errorCode);
        dialog.dismiss();
    }


}


