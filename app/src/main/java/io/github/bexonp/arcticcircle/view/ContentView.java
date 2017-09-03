package io.github.bexonp.arcticcircle.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.github.bexonp.arcticcircle.R;


/**
 * Created by Bexon Pak on 2017/08/30.
 */

public class ContentView extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Intent intent;
    private String title, url, author, date, content;
    private double apkVersion;
    private TextView contentTextView, titleTextView, authorTextView, dateTextView;
    private View blackLineView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        bintent();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.contentSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        getContent();
                                    }
                                }
        );
    }

    private void getContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection conn = Jsoup.connect(url);
                    //设置UA
                    conn.header("User-Agent", "Mozilla/5.0 (Linux; U; Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.91 Mobile Safari/537.36  ArcticCircle/" + apkVersion);
                    //Jsoup筛选器
                    Document html = conn.get();
                    //在网站的_layouts/post.heml里的{{content}}外面加<div class="post-content">...</div>
                    Elements article = html.select("div.post-content");
                    //获取文章
                    content = article.html();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onRefresh() {
        getContent();
    }

    private void bintent() {
        //接受Intent传递的数据
        intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        author = intent.getStringExtra("author");
        date = intent.getStringExtra("date");
        apkVersion = intent.getDoubleExtra("apkVersion", apkVersion);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 当收到消息时就会执行这个方法
            contentTextView = (TextView) findViewById(R.id.contentText);
            titleTextView = (TextView) findViewById(R.id.title);
            authorTextView = (TextView) findViewById(R.id.author);
            dateTextView = (TextView) findViewById(R.id.date);
            blackLineView = findViewById(R.id.blackLine);
            //从API level 24开始，fromHtml(String)被废弃，使用fromHtml(String source, int flags) 代替
            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                //Android N以上
                result = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
            } else {
                //旧版本设备
                result = Html.fromHtml(content);
            }
            contentTextView.setText(result);
            titleTextView.setText(title);
            authorTextView.setText("Written by " + author);
            dateTextView.setText("on " + date);
            blackLineView.setBackgroundColor(Color.parseColor("#353535"));
            //如果正在刷新，停止刷新
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }

    };
}
