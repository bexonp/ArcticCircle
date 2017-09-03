package io.github.bexonp.arcticcircle.service;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.allenliu.versionchecklib.core.AVersionService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.github.bexonp.arcticcircle.entity.Update;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bexon Pak on 2017/09/01.
 */

public class UpdateService extends AVersionService {

    private String json, downloadUrl, title, updateMsg;
    private double serverVersion, clientVersion;

    public UpdateService() {
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        init();
        if (serverVersion > clientVersion) {
            //传入下载地址，以及版本更新消息
            service.showVersionDialog(downloadUrl, title, updateMsg);
        }
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    //超时
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();
                    //服务器返回地址
                    Request request = new Request.Builder()
                            //缓存
                            .cacheControl(new CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).noCache().build())
                            .url("https://bexonp.github.io/apk/versionJson.json")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    //获取数据
                    json = response.body().string();
                    Gsonjx(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void Gsonjx(String json) {
        Gson gson = new Gson();
        Update update = gson.fromJson(json, Update.class);
        downloadUrl = update.getRequestUrl();
        title = update.getTitle();
        updateMsg = update.getUpdateMsg();
        serverVersion = Double.parseDouble(update.getServerVersion());
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            clientVersion = Double.parseDouble(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
