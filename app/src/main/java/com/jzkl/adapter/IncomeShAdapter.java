package com.jzkl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzkl.Bean.IncomeName;
import com.jzkl.R;

import java.util.List;
/*
* 推广标题下的  内容
* */
public class IncomeShAdapter extends BaseAdapter {

    private Context mContext;
    private List<IncomeName> mList;

    public IncomeShAdapter(Context mContext, List<IncomeName> mList){
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IncomeName list = (IncomeName) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_income_content,null);
            shopHolder.content_name  = convertView.findViewById(R.id.item_income_content_name);
            shopHolder.content_renz  = convertView.findViewById(R.id.item_income_content_renz);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.content_name.setText(list.getIncomeName());
        shopHolder.content_renz.setText(list.getIncomeRenz());

        if(list.getIncomeRenz().equals("未实名")){
            shopHolder.content_renz.setTextColor(Color.parseColor("#767676"));
        }else {
            shopHolder.content_renz.setTextColor(Color.parseColor("#ec7777"));
        }
        return convertView;
    }

    class ShopGridHolder{
        TextView content_name;
        TextView content_renz;
    }
}
