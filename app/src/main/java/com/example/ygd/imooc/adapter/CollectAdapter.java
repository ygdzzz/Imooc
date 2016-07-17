package com.example.ygd.imooc.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by ygd on 2016/7/4.
 */
public class CollectAdapter extends RecyclerArrayAdapter {
    public CollectAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return  new CollectViewHolder(parent);
    }
}
