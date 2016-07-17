package com.example.ygd.imooc.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ygd.greendao.Blog;
import com.example.ygd.greendao.BlogDao;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.adapter.BlogAdapter;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }
    public CommunityFragment() {
    }

    private RecyclerView recyclerView;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList;
    private BlogDao blogDao;
    private PullToRefreshView pullToRefreshView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_community, container, false);
        blogDao= MyApplication.getInstance().getDaoSession(mContext).getBlogDao();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        pullToRefreshView= (PullToRefreshView) view.findViewById(R.id.pulltorefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        blogAdapter=new BlogAdapter(mContext);
        recyclerView.setAdapter(blogAdapter);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(mContext,0.5f), Util.dip2px(mContext,10),Util.dip2px(mContext,10));//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        recyclerView.addItemDecoration(itemDecoration);
        blogList=new ArrayList<Blog>();
        getList();

        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getListRequest();
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void getList() {

        if(blogDao.queryBuilder().count()==0){
            getListRequest();
        }else{
            List temp=blogDao.loadAll();
            blogList.clear();
            blogList.addAll(temp);
            blogAdapter.addAll(blogList);
        }



    }


    public void getListRequest() {
        String url= UrlManager.SERVLET_URL+"GetBlog";
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                final List<Blog> list=gson.fromJson(json,new TypeToken<ArrayList<Blog>>(){}.getType());
                if(list!=null&list.size()>0){
                    blogDao.deleteAll();
                    blogDao.insertInTx(list,false);
                    getList();
                }
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异常
                Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        sr.setTag("A");
        SingleVolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(sr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SingleVolleyRequestQueue.getInstance(mContext).getRequestQueue().cancelAll("A");
    }

}
