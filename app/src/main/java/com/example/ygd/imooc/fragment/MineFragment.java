package com.example.ygd.imooc.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ygd.imooc.Activity.LoginActivity;
import com.example.ygd.imooc.Activity.MyCollectActivity;
import com.example.ygd.imooc.Activity.SettingActivity;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    public MineFragment() {
        // Required empty public constructor
    }

    private Button btLogin;
    private TextView tvCollect,tv2,tv3,tv4,tv5,tv6,tvName;
    private ImageView ivHead;
    private FloatingActionButton fab;
    private UserInfo user;
    private CollapsingToolbarLayout ctl;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mine, container, false);
        toolbar= (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(mContext, SettingActivity.class);
                startActivityForResult(intent,2);
                return false;
            }
        });

        appBar= (AppBarLayout) view.findViewById(R.id.appBar);
        btLogin= (Button) view.findViewById(R.id.loginBtn);
        tvName= (TextView) view.findViewById(R.id.nameTv);
        tvCollect= (TextView) view.findViewById(R.id.tv1);
        tv2= (TextView) view.findViewById(R.id.tv2);
        tv3= (TextView) view.findViewById(R.id.tv3);
        tv4= (TextView) view.findViewById(R.id.tv4);
        tv5= (TextView) view.findViewById(R.id.tv5);
        tv6= (TextView) view.findViewById(R.id.tv6);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        ivHead= (ImageView) view.findViewById(R.id.headImg);
        ctl= (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        MyApplication app= (MyApplication)getActivity().getApplication();
        user=app.getUser();
        setListener();

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                onPrepareOptionsMenu(toolbar.getMenu());
                if(verticalOffset==0){
                    setHasOptionsMenu(false);
                }else{
                    setHasOptionsMenu(true);
                }
            }
        });
        if(user==null){
            SharedPreferences sp=getActivity().getSharedPreferences("spTest", Context.MODE_PRIVATE);
            if(sp!=null){
                Boolean fg=sp.getBoolean("flag",false);
                if(fg){
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent,1);
                }
            }
        }else{
            setTop();
        }


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        Log.d("===","走过");
        inflater.inflate(R.menu.mine_toolbar_menu,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.set).setVisible(false);
    }

    public class BtClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "正在开发中..", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListener() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });

        tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    Intent intent=new Intent(getActivity(), MyCollectActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "请先登录！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, SettingActivity.class);
                startActivityForResult(intent,2);
            }
        });

        tv2.setOnClickListener(new BtClick());
        tv3.setOnClickListener(new BtClick());
        tv4.setOnClickListener(new BtClick());
        tv5.setOnClickListener(new BtClick());
        tv6.setOnClickListener(new BtClick());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==getActivity().RESULT_OK){
                Gson gson=new Gson();
                String json=data.getStringExtra("json");
                user=gson.fromJson(json,new TypeToken<UserInfo>(){}.getType());
                Toast.makeText(mContext, "欢迎回来，"+user.getNickName(), Toast.LENGTH_SHORT).show();
                setTop();
            }
        }else if(requestCode==2){
            if(resultCode==getActivity().RESULT_OK){
                user=null;
                setTopBack();
            }
        }

    }

    public void setTop(){
        tvName.setVisibility(View.VISIBLE);
        btLogin.setVisibility(View.INVISIBLE);
        btLogin.setEnabled(false);
        if(user.getSexId().equals("男")){
            tvName.setText(user.getNickName()+"♂");
            ctl.setTitle(user.getNickName()+"♂");
        }else{
            tvName.setText(user.getNickName()+"♀");
            ctl.setTitle(user.getNickName()+"♀");
        }

        Glide.with(mContext)
                .load(UrlManager.BASE_URL+user.getPhotoUri())
                .placeholder(R.mipmap.personal_default_user_icon)
                .bitmapTransform(new CropCircleTransformation(Glide.get(mContext).getBitmapPool()))
                .into(ivHead);
    }

    public void setTopBack(){
        tvName.setVisibility(View.INVISIBLE);
        btLogin.setVisibility(View.VISIBLE);
        btLogin.setEnabled(true);
        tvName.setText("昵称");
        ctl.setTitle("我的");
        ivHead.setImageResource(R.mipmap.personal_default_user_icon);
    }


}
