package com.example.ygd.imooc.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ygd.greendao.VedioDownload;
import com.example.ygd.imooc.R;
import com.example.ygd.imooc.util.UrlManager;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by ygd on 2016/7/6.
 */
public class DownloadViewHolder extends BaseViewHolder {
    private ImageView img;
    private TextView name;
    private TextView instruction;
    private TextView author;
    public DownloadViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_download);
        img = (ImageView) $(R.id.iv);
        name= (TextView) $(R.id.tv1);
        instruction= (TextView) $(R.id.tv2);
        author= (TextView) $(R.id.tv3);
    }

    @Override
    public void setData(Object data) {
        final VedioDownload vedioDownload= (VedioDownload) data;
        Glide.with(getContext())
                .load(UrlManager.BASE_URL+vedioDownload.getVPickUri())
                .placeholder(R.mipmap.ic_launcher)
//                .bitmapTransform(new CropCircleTransformation(Glide.get(getContext()).getBitmapPool()))
                .into(img);
        name.setText(vedioDownload.getVedioName());
        instruction.setText(vedioDownload.getInstruction());
        author.setText(vedioDownload.getAuthor());
    }
}
