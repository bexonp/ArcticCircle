package io.github.bexonp.arcticcircle.view;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.Toast;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.github.bexonp.arcticcircle.R;
import io.github.bexonp.arcticcircle.adapter.DataAdapter;
import io.github.bexonp.arcticcircle.entity.Data;

import io.github.bexonp.arcticcircle.service.UpdateService;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {

    private String json;
    private ListView listView;
    private ArrayList<Data> listuser;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String RootURL = "https://bexonp.github.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.mainList);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        checkVersion();
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContentView.class);
                intent.putExtra("title", listuser.get(position).getTitle());
                intent.putExtra("url", RootURL + listuser.get(position).getUrl());
                intent.putExtra("author", listuser.get(position).getAuthor());
                intent.putExtra("date", listuser.get(position).getDate());
                startActivity(intent);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        init();
                                    }
                                }
        );
        menu();
    }

    private void menu() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.about_item:
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void checkVersion() {
        VersionParams.Builder builder = new VersionParams.Builder()
                .setRequestUrl("https://bexonp.github.io/apk/versionJson.json")
                .setService(UpdateService.class)
                .setDownloadAPKPath(getDiskCacheDir(this));
        AllenChecker.startVersionCheck(this, builder.build());
    }

    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath() + "/update";
        } else {
            cachePath = context.getCacheDir().getPath() + "/update";
        }
        return cachePath;
    }

    @Override
    public void onRefresh() {
        init();
    }

    //获取Json数据
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
                            .url("https://bexonp.github.io/getjson.json")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    //获取数据
                    json = response.body().string();
                    Message message = new Message();
                    message.what = 1;
                    han.sendMessage(message);
                    Gsonjx(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public android.os.Handler han = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //Toast.makeText(MainActivity.this,""+json,Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    //调用listview适配器
                    listView.setAdapter(new DataAdapter(MainActivity.this, listuser));
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    break;
            }
        }
    };

    private void Gsonjx(String json) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        Gson gson = new Gson();
        listuser = new ArrayList<>();
        for (JsonElement data : jsonArray) {
            Data userBean = gson.fromJson(data, Data.class);
            listuser.add(userBean);
        }
        Message messsage = new Message();
        messsage.what = 2;
        han.sendMessage(messsage);
    }
}

