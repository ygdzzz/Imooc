package com.example.ygd.imooc.application;

import android.app.Application;
import android.content.Context;

import com.example.ygd.greendao.DaoMaster;
import com.example.ygd.greendao.DaoSession;
import com.example.ygd.imooc.entity.UserInfo;

public class MyApplication extends Application {
    private UserInfo user;
    //Application实例对象
    private static MyApplication instance;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
    public static MyApplication getInstance(){
        return instance;
    }

    public UserInfo getUser(){
        return user;
    }

    public void setUser(UserInfo user){
        this.user=user;
    }

    public DaoMaster getDaoMaster(Context context){
        if(daoMaster==null){
            DaoMaster.OpenHelper helper=new DaoMaster.DevOpenHelper(context,"vedio.db",null);
            daoMaster=new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(Context context){
        if(daoSession==null){
            if(daoSession==null){
                daoMaster=getDaoMaster(context);
            }
            daoSession=daoMaster.newSession();
        }
        return daoSession;
    }

}
