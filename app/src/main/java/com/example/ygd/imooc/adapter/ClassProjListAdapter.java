package com.example.ygd.imooc.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.ygd.greendao.ClassProj;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by ygd on 2016/6/27.
 */
public class ClassProjListAdapter extends RecyclerArrayAdapter<ClassProj>{
    public ClassProjListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClassProjViewHolder(parent);
    }
}
