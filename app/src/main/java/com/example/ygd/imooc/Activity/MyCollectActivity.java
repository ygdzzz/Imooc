package com.example.ygd.imooc.Activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.greendao.Vedio;
import com.example.ygd.greendao.VedioDownload;
import com.example.ygd.greendao.VedioDownloadDao;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.adapter.CollectAdapter;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.player.PlayerActivity;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.StringPostRequest;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class MyCollectActivity extends AppCompatActivity {

    private UserInfo user;
    private RecyclerView recyclerView;
    private List<Vedio> list;
    private CollectAdapter collectAdapter;
    private RecyclerTouchListener onTouchListener;
    private VedioDownloadDao vedioDownloadDao;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            collectAdapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vedioDownloadDao= MyApplication.getInstance().getDaoSession(this).getVedioDownloadDao();
        MyApplication app= (MyApplication) getApplication();
        user=app.getUser();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectAdapter=new CollectAdapter(this);
        recyclerView.setAdapter(collectAdapter);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,10),Util.dip2px(this,10));//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        recyclerView.addItemDecoration(itemDecoration);
        list=new ArrayList<Vedio>();
        getList();

    }

    private void setTouchListener() {
        onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setIndependentViews(R.id.iv)
                .setViewsToFade(R.id.iv)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            String type = networkInfo.getTypeName();
                            if (type.equalsIgnoreCase("WIFI")) {
                                startActivity(new Intent(getBaseContext(), PlayerActivity.class).putExtra("url", UrlManager.BASE_URL+list.get(position).getVUri()));
                            } else {
                                SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                                if(sp.getBoolean("isWifi",true)){
                                    startActivity(new Intent(getBaseContext(), PlayerActivity.class).putExtra("url", UrlManager.BASE_URL + list.get(position).getVUri()));
                                }else{
                                    Toast.makeText(MyCollectActivity.this, "请切换到Wifi网络进行播放！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                    }
                })
                .setSwipeOptionViews(R.id.add, R.id.edit, R.id.change)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        String message = "";
                        if (viewID == R.id.add) {
                            message += "Add";
                            deleteCollect(position);
                        } else if (viewID == R.id.edit) {
                            message += "Edit";
                            download(position);
                        } else if (viewID == R.id.change) {
                            message += "Change";
                        }
                        message += " clicked for row " + (position + 1);
//                        Toast.makeText(MyCollectActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    private void download(int position) {
        final Vedio download=list.get(position);
        QueryBuilder<VedioDownload> qb=vedioDownloadDao.queryBuilder();
        qb.where(VedioDownloadDao.Properties.ProjId.eq(download.getProjId()),VedioDownloadDao.Properties.Vedioid.eq(download.getVedioid()));
        if(qb.list().size()>0){
            Snackbar.make(getCurrentFocus(), "视频已下载，是否仍然下载？", Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorMine))
                    .setAction("仍然下载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadVedio(download);
                        }
                    }).show();
        }else{
            downloadVedio(download);
        }

    }

    public void downloadVedio(Vedio download){
        String vname=download.getVUri();
        int index=vname.lastIndexOf("/");
        vname=vname.substring(index);
        String url=UrlManager.BASE_URL+download.getVUri();
        //取得系统的下载服务
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //创建下载请求对象
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir("sdcard/Download/", vname);
        request.setNotificationVisibility(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long id=downloadManager.enqueue(request);
        VedioDownload vedioDownload=new VedioDownload(id,download.getVedioid(), download.getVedioName(), download.getVUri(), download.getProjId(), download.getInstruction(), download.getAuthor(), download.getPubDate(), download.getVPickUri(), download.getFlag());
        vedioDownloadDao.insert(vedioDownload);
    }



    public void deleteCollect(int position){
        Vedio collect=list.get(position);
        String url= UrlManager.SERVLET_URL+"UserCollectCtrl";
        StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                if(json.equals("0")){
                    Toast.makeText(MyCollectActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                    getList();
                }else{
                    Toast.makeText(MyCollectActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();

            }
        });
        spr.putValue("flag","2");
        spr.putValue("uname",user.getUname());
        spr.putValue("vedioid",collect.getVedioid());
        spr.putValue("projid",collect.getProjId());
        spr.setTag("B");
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(spr);
    }


    private void getList() {
        String url= UrlManager.SERVLET_URL+"UserCollectCtrl?flag=3&uname="+user.getUname();
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                Log.d("====json====",json);

                list=gson.fromJson(json,new TypeToken<ArrayList<Vedio>>(){}.getType());
                if(list!=null&&list.size()>0) {
                    collectAdapter.clear();
                    collectAdapter.addAll(list);
                }
                setTouchListener();
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        sr.setTag("A");
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(sr);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        SingleVolleyRequestQueue.getInstance(this).getRequestQueue().cancelAll("A");
        SingleVolleyRequestQueue.getInstance(this).getRequestQueue().cancelAll("B");
    }
}
