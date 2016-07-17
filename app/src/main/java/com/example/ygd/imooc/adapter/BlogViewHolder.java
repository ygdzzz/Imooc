package com.example.ygd.imooc.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ygd.greendao.Blog;
import com.example.ygd.imooc.Activity.MyCSDNActivity;
import com.example.ygd.imooc.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by ygd on 2016/7/6.
 */
public class BlogViewHolder extends BaseViewHolder {
    private TextView tvTitle,tvDate,tvRead,tvComment;
    public BlogViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_blog);
        tvTitle= (TextView) $(R.id.tvTitle);
        tvDate= (TextView) $(R.id.tvDate);
        tvRead= (TextView) $(R.id.tvRead);
        tvComment= (TextView) $(R.id.tvComment);
    }

    @Override
    public void setData(Object data) {
        final Blog blog= (Blog) data;
        tvTitle.setText(blog.getTitle());
        tvDate.setText(blog.getDate());
        tvRead.setText("阅读量："+blog.getRead());
        tvComment.setText("评论数："+blog.getComment());

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=blog.getUrl();
                Intent intent=new Intent(getContext(),MyCSDNActivity.class);
                intent.putExtra("url",url);
                getContext().startActivity(intent);
            }
        });
    }
}
