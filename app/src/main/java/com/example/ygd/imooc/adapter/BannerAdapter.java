package com.example.ygd.imooc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ygd.greendao.Vedio;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.player.PlayerActivity;
import com.example.ygd.imooc.util.UrlManager;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

public class BannerAdapter extends StaticPagerAdapter {

    private Context ctx;
    private List<Vedio> list;
       public BannerAdapter(Context ctx,List<Vedio> list){
           this.ctx = ctx;
           this.list=list;
        }

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(ctx);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //加载图片
            Glide.with(ctx)
                    .load(UrlManager.BASE_URL+list.get(position).getVPickUri())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
            //点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getUrl())));
                    Intent intent=new Intent(ctx, PlayerActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", UrlManager.BASE_URL+list.get(position).getVUri());
                    ctx.startActivity(intent);
                }
            });
            return imageView;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }