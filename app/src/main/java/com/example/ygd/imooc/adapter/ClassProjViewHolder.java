package com.example.ygd.imooc.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ygd.greendao.ClassProj;
import com.example.ygd.imooc.Activity.ClassProjActivity;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.util.UrlManager;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by ygd on 2016/6/27.
 */
public class ClassProjViewHolder extends BaseViewHolder{
    private ImageView img;
    private TextView remark;
    public ClassProjViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_class_proj);
        img = (ImageView) $(R.id.iv);
        remark = (TextView) $(R.id.tv);
    }

    @Override
    public void setData(Object data) {
        final ClassProj classProj= (ClassProj) data;
        Glide.with(getContext())
                .load(UrlManager.BASE_URL+classProj.getPhotoUri())
                .placeholder(R.mipmap.ic_launcher)
//                .bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool()))
                .into(img);
        remark.setText(classProj.getRemark());

        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ClassProjActivity.class);
                intent.putExtra("projid",classProj.getProjId());
                intent.putExtra("remark",classProj.getRemark());
                getContext().startActivity(intent);
            }
        });
    }
}
