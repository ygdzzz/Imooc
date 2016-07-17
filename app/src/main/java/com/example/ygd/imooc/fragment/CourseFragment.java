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
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.ygd.greendao.ClassProj;
import com.example.ygd.greendao.ClassProjDao;
import com.example.ygd.greendao.Vedio;
import com.example.ygd.greendao.VedioDao;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.adapter.BannerAdapter;
import com.example.ygd.imooc.adapter.ClassProjListAdapter;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.Util;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    public CourseFragment() {
        // Required empty public constructor
    }


    private BannerAdapter bannerAdapter;
    private RollPagerView rollPagerView;
    private RecyclerView recyclerView;
    private ClassProjListAdapter classProjListAdapter;
    private ClassProjDao classProjDao;
    private List<ClassProj> classProjList;
    private MaterialRefreshLayout swipe;
    private List<Vedio> vedioList;
    private VedioDao vedioDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_course, container, false);

//        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        classProjDao= MyApplication.getInstance().getDaoSession(mContext).getClassProjDao();
        vedioDao= MyApplication.getInstance().getDaoSession(mContext).getVedioDao();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipe= (MaterialRefreshLayout) view.findViewById(R.id.swipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        classProjListAdapter=new ClassProjListAdapter(getActivity());
        swipe.setSunStyle(true);
        swipe.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getListRequest();
                        getTopRequest();
                        materialRefreshLayout.finishRefresh();
                    }
                }, 2000);

            }

            @Override
            public void onfinish() {
                Toast.makeText(mContext, "加载完成", Toast.LENGTH_SHORT).show();
            }


        });

        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getActivity(),0.5f), Util.dip2px(getActivity(),10),Util.dip2px(getActivity(),10));//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        recyclerView.addItemDecoration(itemDecoration);

        classProjList=new ArrayList<ClassProj>();
        getList();
        recyclerView.setAdapter(classProjListAdapter);
        rollPagerView = (RollPagerView) view.findViewById(R.id.rollPagerView);
        rollPagerView.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW,Color.GRAY));
        vedioList=new ArrayList<Vedio>();
        getTop();
        return view;
    }




    public void getTop(){

        if(vedioDao.queryBuilder().count()==0){
            getTopRequest();
        }else{
            List temp=vedioDao.loadAll();
            vedioList.clear();
            vedioList.addAll(temp);
            bannerAdapter=new BannerAdapter(getActivity(),vedioList);
            rollPagerView.setAdapter(bannerAdapter);
        }



    }

    public void getTopRequest(){
        String url= UrlManager.SERVLET_URL+"VedioCtrl?top=1";
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                List<Vedio> list=gson.fromJson(json,new TypeToken<ArrayList<Vedio>>(){}.getType());
                if(list!=null&list.size()>0){
                    vedioDao.deleteAll();
                    vedioDao.insertInTx(list);
                    getTop();
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

    public void getList(){
        if(classProjDao.queryBuilder().count()==0){
            getListRequest();
        }else{
            List temp=classProjDao.loadAll();
            classProjList.clear();
            classProjList.addAll(temp);
            classProjListAdapter.addAll(classProjList);
        }
    }

    public void getListRequest(){
        String url= UrlManager.SERVLET_URL+"ClassProjCtrl?top=1";
        StringRequest sr=new StringRequest(url, new Response.Listener<String>() {   //响应成功监听接口
            @Override
            public void onResponse(String json) {
                Gson gson= new Gson();
                json=json.trim();
                final List<ClassProj> list=gson.fromJson(json,new TypeToken<ArrayList<ClassProj>>(){}.getType());
                if(list!=null&list.size()>0){
                    classProjDao.deleteAll();
                    classProjDao.insertInTx(list,false);
                    getList();
                }
            }
        }, new Response.ErrorListener() {   //响应错误监听接口
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //提示网络异
                Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
            }
        });
        sr.setTag("B");
        SingleVolleyRequestQueue.getInstance(getActivity()).addToRequestQueue(sr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SingleVolleyRequestQueue.getInstance(mContext).getRequestQueue().cancelAll("A");
        SingleVolleyRequestQueue.getInstance(mContext).getRequestQueue().cancelAll("B");
    }
}
