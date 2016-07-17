package com.example.ygd.imooc.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ygd.greendao.VedioDownload;
import com.example.ygd.greendao.VedioDownloadDao;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.adapter.DownloadAdapter;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.player.PlayerActivity;
import com.example.ygd.imooc.util.DataCleanManager;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.StorageUtil;
import com.example.ygd.imooc.util.StringPostRequest;
import com.example.ygd.imooc.util.UrlManager;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }
    private RecyclerView recyclerView;
    private List<VedioDownload> list;
    private DownloadAdapter downloadAdapter;
    private RecyclerTouchListener onTouchListener;
    private VedioDownloadDao vedioDownloadDao;
    private Toolbar toolbar;
    private ProgressBar pb;
    private TextView tv;

    public DownloadFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_download, container, false);

        toolbar= (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        pb= (ProgressBar) view.findViewById(R.id.pb);
        tv= (TextView) view.findViewById(R.id.tv2);
        String total=DataCleanManager.getFormatSize(StorageUtil.getTotalInternalMemorySize());
        String unused=DataCleanManager.getFormatSize(StorageUtil.getAvailableInternalMemorySize());
//        Log.d("===total===",""+StorageUtil.getTotalInternalMemorySize());
//        Log.d("===unused===",""+StorageUtil.getAvailableInternalMemorySize());
        String str= "总内存"+total+"，剩余"+unused+"可用";
        tv.setText(str);
        pb.setMax((int) (StorageUtil.getTotalInternalMemorySize()/100000));
        pb.setProgress((int) ((StorageUtil.getTotalInternalMemorySize()-StorageUtil.getAvailableInternalMemorySize())/100000));
        vedioDownloadDao= MyApplication.getInstance().getDaoSession(mContext).getVedioDownloadDao();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        downloadAdapter=new DownloadAdapter(mContext);
        recyclerView.setAdapter(downloadAdapter);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(mContext,0.5f), Util.dip2px(mContext,10),Util.dip2px(mContext,10));//color & height & paddingLeft & paddingRight
        itemDecoration.setDrawLastItem(true);//sometimes you don't want draw the divider for the last item,default is true.
        itemDecoration.setDrawHeaderFooter(false);//whether draw divider for header and footer,default is false.
        recyclerView.addItemDecoration(itemDecoration);
        list=new ArrayList<VedioDownload>();
        getList();
        return view;
    }

    private void getList() {
        list=vedioDownloadDao.loadAll();
        downloadAdapter.clear();
        downloadAdapter.addAll(list);
        setTouchListener();

    }

    private void setTouchListener() {
        onTouchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        onTouchListener
                .setIndependentViews(R.id.iv)
                .setViewsToFade(R.id.iv)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        startActivity(new Intent(mContext, PlayerActivity.class).putExtra("url", "file://"+list.get(position).getVUri()));
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
                            vedioDownloadDao.delete(list.get(position));
                            downloadAdapter.notifyDataSetChanged();
                            getList();
                            Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                        } else if (viewID == R.id.edit) {
                            message += "Edit";
                            addCollect(position);
                        } else if (viewID == R.id.change) {
                            message += "Change";
                        }
                        message += " clicked for row " + (position + 1);
//                        Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    public void addCollect(int position){
        MyApplication app= (MyApplication)getActivity().getApplication();
        if(app.getUser()==null){
            Toast.makeText(mContext, "请先登录！", Toast.LENGTH_SHORT).show();
        }else{
            UserInfo user=app.getUser();
            VedioDownload vedio=list.get(position);


            String url= UrlManager.SERVLET_URL+"UserCollectCtrl";
            StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    if(json.equals("null")){
                        Toast.makeText(mContext, "已经收藏过！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();

                }
            });
            spr.putValue("flag","1");
            spr.putValue("uname",user.getUname());
            spr.putValue("projid",vedio.getProjId());
            spr.putValue("vedioid",vedio.getVedioid());
            SingleVolleyRequestQueue.getInstance(mContext).addToRequestQueue(spr);
        }
    }

}
