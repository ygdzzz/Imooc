package com.example.ygd.imooc.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.application.MyApplication;
import com.example.ygd.imooc.entity.UserInfo;
import com.example.ygd.imooc.util.FileUitlity;
import com.example.ygd.imooc.util.SingleVolleyRequestQueue;
import com.example.ygd.imooc.util.StringPostRequest;
import com.example.ygd.imooc.util.UrlManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ModifyActivity extends AppCompatActivity {

    private UserInfo user;
    private EditText etPwd,etNickname;
    private TextView etName;
    private RadioButton rb1,rb2;
    private Button btModify;
    private ImageView ivHead;
    private PopupWindow popupWindow;
    private String capturePath;
    private final int TAKE_PHOTO=0;
    private final int RESULT_PHOTO=2;
    private  final int PHONE_PHOTO=1;
    private String imgUrl="/user/userhead_img/head.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyActivity.this.setResult(RESULT_CANCELED);
                ModifyActivity.this.finish();
            }
        });
        MyApplication app= (MyApplication)getApplication();
        user=app.getUser();
        etName= (TextView) findViewById(R.id.etName);
        etPwd= (EditText) findViewById(R.id.etPwd);
        etNickname= (EditText) findViewById(R.id.etNickname);
        rb1= (RadioButton) findViewById(R.id.rb1);
        rb2= (RadioButton) findViewById(R.id.rb2);
        btModify= (Button) findViewById(R.id.btModify);
        etName.setText(user.getUname());
        etPwd.setText(user.getUpwd());
        etNickname.setText(user.getNickName());
        ivHead= (ImageView) findViewById(R.id.ivHead);
        Glide.with(this)
                .load(UrlManager.BASE_URL+user.getPhotoUri())
                .placeholder(R.mipmap.personal_default_user_icon)
                .bitmapTransform(new CropCircleTransformation(Glide.get(this).getBitmapPool()))
                .into(ivHead);
        setHeadImg();
        if(user.getSexId().equals("男")){
            rb1.setChecked(true);
            rb2.setChecked(false);
        }else{
            rb1.setChecked(false);
            rb2.setChecked(true);
        }
        btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPwd.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }else if(etNickname.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
                }else{
                    stringRequestPost();
                }
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ModifyActivity.this.setResult(RESULT_CANCELED);
            ModifyActivity.this.finish();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void stringRequestPost(){
        String url= UrlManager.SERVLET_URL+"UserLogin";
        StringPostRequest spr=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                if(json.equals("null")){
                    Toast.makeText(getBaseContext(), "修改失败 ！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getBaseContext(), "修改成功 ！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("result",json);
                    ModifyActivity.this.setResult(RESULT_OK,intent);
                    ModifyActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("flag","3");
        spr.putValue("uname",etName.getText().toString());
        spr.putValue("upwd",etPwd.getText().toString());
        spr.putValue("unickname",etNickname.getText().toString());
        spr.putValue("photouri",imgUrl);
        spr.putValue("sexid",getSexid());
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(spr);
    }

    public String getSexid(){
        String sexId;
        if(rb1.isChecked()){
            sexId="男";
        }else{
            sexId="女";
        }
        return sexId;
    }


    private void setHeadImg() {
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int width=getResources().getDisplayMetrics().widthPixels;
                int height=getResources().getDisplayMetrics().heightPixels/3;
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(v, Gravity.BOTTOM,0,0);
                popupWindow.showAsDropDown(v,0,0);
                backgroundAlpha(0.7f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
            }
        });

        View view=getLayoutInflater().inflate(R.layout.pop_layout,null);
        popupWindow=new PopupWindow(view);
        popupWindow.setFocusable(true);
        ColorDrawable cd=new ColorDrawable();
        popupWindow.setBackgroundDrawable(cd);
        Button bt_Camera= (Button) view.findViewById(R.id.bt_camera);
        Button bt_Photo= (Button) view.findViewById(R.id.bt_photo);
        Button bt_qx= (Button) view.findViewById(R.id.bt_qx);
        bt_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相机
                Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File parent= FileUitlity.getInstance(getApplicationContext()).makeDir("gives_img");
                capturePath=parent.getPath()+File.separatorChar+System.currentTimeMillis()+".jpg";
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
                camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(camera,TAKE_PHOTO);
                popupWindow.dismiss();
            }
        });
        bt_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                phone_photo();
                popupWindow.dismiss();
            }
        });
        bt_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void phone_photo(){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PHONE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        //相机返回结果，调用系统裁剪
        if(requestCode==TAKE_PHOTO){
            startPhoneZoom(Uri.fromFile(new File(capturePath)));
        }
        else if (requestCode==PHONE_PHOTO){
            Cursor cursor=this.getContentResolver().query(data.getData(),new String[]{MediaStore.Images.Media.DATA},null,null,null);
            cursor.moveToFirst();
            String  capturePath=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            startPhoneZoom(Uri.fromFile(new File(capturePath)));
        }
        //裁剪返回位图
        else if (requestCode==RESULT_PHOTO){
            Bundle bundle=data.getExtras();
            if(bundle!=null){
                Bitmap bitmap=bundle.getParcelable("data");
                ivHead.setImageBitmap(bitmap);
                updatePhoto(convertBitmap(bitmap));
            }
        }
    }


    private void updatePhoto(String photoStr) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在上传，请稍后……");
        if (pd != null){
            pd.show();
        }
        // 加载网络数据
        String url = UrlManager.SERVLET_URL+"UserLogin?flag=4";
        StringPostRequest request = new StringPostRequest(url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                imgUrl = s;
                Log.d("===imgUrl===",s);
                Toast.makeText(getApplicationContext(), "头像上传成功！", Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(pd!=null && pd.isShowing())
                    pd.dismiss();
                Toast.makeText(getApplicationContext(), "头像上传失败！", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("uhead", photoStr);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }


    //Bitmap转换成字符串
    public String convertBitmap(Bitmap b){
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG,100,out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer=out.toByteArray();
        byte[] encode= Base64.encode(buffer,Base64.DEFAULT);
        return new String(encode);
    }



    //调用系统裁剪
    public void startPhoneZoom(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置是否可以裁剪
        intent.putExtra("crop","true");
        //设置宽度高度比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //设置图片的高度宽度
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_PHOTO);
    }

}
