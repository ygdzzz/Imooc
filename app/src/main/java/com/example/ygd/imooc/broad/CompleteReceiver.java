package com.example.ygd.imooc.broad;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.example.ygd.greendao.VedioDownload;
import com.example.ygd.greendao.VedioDownloadDao;
import com.example.ygd.imooc.application.MyApplication;

import de.greenrobot.dao.query.QueryBuilder;

public class CompleteReceiver extends BroadcastReceiver {
    public CompleteReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        VedioDownloadDao vedioDownloadDao= MyApplication.getInstance().getDaoSession(context).getVedioDownloadDao();
        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            Toast.makeText(context, "下载已完成..", Toast.LENGTH_LONG).show();
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if(c.moveToFirst()) {
                //获取文件下载路径
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if(filename != null){
                    QueryBuilder<VedioDownload> qb=vedioDownloadDao.queryBuilder();
                    qb.where(VedioDownloadDao.Properties.Id.eq(id));
                    if(qb.list().size()>0){
                        qb.list().get(0).setVUri(filename);
                        vedioDownloadDao.update(qb.list().get(0));
                        Toast.makeText(context, filename, Toast.LENGTH_LONG).show();
                        Log.d("===complete===",filename);
                    }

                }
            }
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            for(int i=0;i<ids.length;i++){
                vedioDownloadDao.deleteByKey(ids[i]);
            }
            Toast.makeText(context, "已经取消下载..", Toast.LENGTH_SHORT).show();
        }
    }
}