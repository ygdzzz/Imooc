package com.example.ygd.imooc.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.StringPostRequest;
import com.example.ygd.imooc.util.UrlManager;

public class SuggestActivity extends AppCompatActivity {

    private EditText etSuggest;
    private Button btSuggest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuggestActivity.this.finish();
            }
        });
        etSuggest= (EditText) findViewById(R.id.etSuggest);
        btSuggest= (Button) findViewById(R.id.btSuggest);

        btSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSuggest();
            }
        });

    }

    private void addSuggest() {
        MyApplication app= (MyApplication)getApplication();
        if(app.getUser()==null){
            Toast.makeText(SuggestActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();

        }if(etSuggest.length()<=0){
            Toast.makeText(SuggestActivity.this, "请输入建议！", Toast.LENGTH_SHORT).show();
        }else{
            String url= UrlManager.SERVLET_URL+"AddSuggest";
            StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    Toast.makeText(getBaseContext(), "非常感谢您的建议，我们一定会做出改进！", Toast.LENGTH_SHORT).show();
                    SuggestActivity.this.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getBaseContext(), "网络连接异常", Toast.LENGTH_SHORT).show();

                }
            });
            spr.putValue("uname",app.getUser().getUname());
            spr.putValue("suggestion",etSuggest.getText().toString());
            spr.setTag("A");
            SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(spr);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SingleVolleyRequestQueue.getInstance(this).getRequestQueue().cancelAll("A");
    }
}
