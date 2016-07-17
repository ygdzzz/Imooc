package com.example.ygd.imooc.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.util.DataCleanManager;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout usermessage,clear,wifi,autoplay,suggest,update,about,recommend,logout,help;
    private ImageView headimage;
    private TextView nickname_tv,login_btn,clear_size;
    private UserInfo user;
    private CheckBox wifi_switch,autoplay_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user==null){
                    SettingActivity.this.setResult(RESULT_OK);
                }else{
                    SettingActivity.this.setResult(RESULT_CANCELED);
                }
                SettingActivity.this.finish();
            }
        });
        usermessage= (RelativeLayout) findViewById(R.id.usermessage_layout);
        help= (RelativeLayout) findViewById(R.id.help);
        clear= (RelativeLayout) findViewById(R.id.clear);
        wifi= (RelativeLayout) findViewById(R.id.wifi);
        autoplay= (RelativeLayout) findViewById(R.id.autoplay);
        suggest= (RelativeLayout) findViewById(R.id.suggest);
        update= (RelativeLayout) findViewById(R.id.update);
        about= (RelativeLayout) findViewById(R.id.about);
        recommend= (RelativeLayout) findViewById(R.id.best_recommend);
        logout= (RelativeLayout) findViewById(R.id.logout);
        headimage= (ImageView) findViewById(R.id.headimage);
        nickname_tv= (TextView) findViewById(R.id.nickname_tv);
        clear_size= (TextView) findViewById(R.id.clear_size);
        login_btn= (TextView) findViewById(R.id.login_btn);
        wifi_switch= (CheckBox) findViewById(R.id.wifi_switch);
        autoplay_switch= (CheckBox) findViewById(R.id.autoplay_switch);

        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
        wifi_switch.setChecked(sp.getBoolean("isWifi",true));
        autoplay_switch.setChecked(sp.getBoolean("isAuto",true));

        try {
            String size=DataCleanManager.getCacheSize(getBaseContext().getExternalCacheDir());
//            Log.d("===cache===",""+getExternalCacheDir().toString());
            clear_size.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setListener();

        MyApplication app= (MyApplication)getApplication();
        user=app.getUser();
        if(user!=null){
            setViewWhenLogin();
        }else{
            setViewWhenLogout();
        }
    }

    private void setListener() {

        wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sp.edit();
                edit.putBoolean("isWifi",isChecked);
                edit.commit();
            }
        });
        autoplay_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sp.edit();
                edit.putBoolean("isAuto",isChecked);
                edit.commit();
            }
        });

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,SuggestActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,OnlineHelpActivity.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "“视频课”是模仿慕课网所做出的一款看学习视频的应用", Toast.LENGTH_SHORT).show();
            }
        });

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "暂无应用推荐！", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "暂无更新！", Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.cleanInternalCache(getBaseContext());
                clear_size.setText("0.0Byte");
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(user==null){
                SettingActivity.this.setResult(RESULT_OK);
            }else{
                SettingActivity.this.setResult(RESULT_CANCELED);
            }
            SettingActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }
    private void setViewWhenLogout() {
        headimage.setImageResource(R.mipmap.personal_default_user_icon);
        nickname_tv.setText("昵称");
        login_btn.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
        usermessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setViewWhenLogin() {
        Glide.with(this)
                .load(UrlManager.BASE_URL+user.getPhotoUri())
                .placeholder(R.mipmap.personal_default_user_icon)
                .bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool()))
                .into(headimage);
        nickname_tv.setText(user.getNickName());
        login_btn.setVisibility(View.INVISIBLE);
        usermessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this,ModifyActivity.class);
                startActivityForResult(intent,1);
            }
        });
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication app= (MyApplication)getApplication();
                app.setUser(null);
                user=null;
                setViewWhenLogout();
            }
        });


    }

    public class BtClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(getBaseContext(), "正在开发中..", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK){
            String json=data.getStringExtra("result");
            Gson gson=new Gson();
            json=json.trim();
            user=gson.fromJson(json,new TypeToken<UserInfo>(){}.getType());
            MyApplication app= (MyApplication) getApplication();
            app.setUser(user);
            setViewWhenLogout();
        }
    }

}
