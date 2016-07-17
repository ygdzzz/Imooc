package com.example.ygd.imooc.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.StringPostRequest;
import com.example.ygd.imooc.util.UrlManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LoginActivity extends AppCompatActivity {
    private EditText etName,etPwd;
    private Button btLogin;
    private CheckBox cb1,cb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.setResult(RESULT_CANCELED);
                LoginActivity.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
                return false;
            }
        });

        etName= (EditText) findViewById(R.id.etName);
        etPwd= (EditText) findViewById(R.id.etPwd);
        btLogin= (Button) findViewById(R.id.btLogin);
        cb1= (CheckBox) findViewById(R.id.cb1);
        cb2= (CheckBox) findViewById(R.id.cb2);
        Login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String json=data.getStringExtra("result");
            Gson gson=new Gson();
            json=json.trim();
            UserInfo user=gson.fromJson(json,new TypeToken<UserInfo>(){}.getType());
            MyApplication app= (MyApplication) getApplication();
            app.setUser(user);
            Intent intent = new Intent();
            intent.putExtra("json",json);
            LoginActivity.this.setResult(RESULT_OK,intent);
            LoginActivity.this.finish();
        }
    }

    private void Login() {

        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
        if(sp!=null){
            String id=sp.getString("id","");
            String pwd=sp.getString("pwd","");
            Boolean flag=sp.getBoolean("flag",false);
            etName.setText(id);
            etPwd.setText(pwd);
            if(flag){
                stringRequestPost();
            }
        }
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etName.getText().toString().isEmpty()&&!etPwd.getText().toString().isEmpty()){
                    stringRequestPost();
                }
            }
        });
    }

    public void stringRequestPost(){
        String url= UrlManager.SERVLET_URL+"UserLogin";
        StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                if(json.equals("null")){
                    Toast.makeText(LoginActivity.this, "账号或密码有误！", Toast.LENGTH_SHORT).show();
                }else{
                    if(cb1.isChecked()){
                        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("id",etName.getText().toString());
                        edit.putString("pwd",etPwd.getText().toString());
                        edit.putBoolean("flag",cb2.isChecked());
                        edit.commit();
                    }else{
                        SharedPreferences sp=getSharedPreferences("spTest", Context.MODE_PRIVATE);
                        if(sp!=null){
                            SharedPreferences.Editor edit=sp.edit();
                            edit.clear();
                        }
                    }


                    Gson gson=new Gson();
                    json=json.trim();
                    UserInfo user=gson.fromJson(json,new TypeToken<UserInfo>(){}.getType());
                    MyApplication app= (MyApplication) getApplication();
                    app.setUser(user);

                    Intent intent = new Intent();
                    intent.putExtra("json",json);
                    LoginActivity.this.setResult(RESULT_OK,intent);
                    LoginActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("flag","1");
        spr.putValue("uname",etName.getText().toString());
        spr.putValue("upwd",etPwd.getText().toString());
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(spr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            LoginActivity.this.setResult(RESULT_CANCELED);
            LoginActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }
}
