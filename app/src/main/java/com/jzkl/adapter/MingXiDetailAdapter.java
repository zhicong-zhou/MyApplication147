package com.jzkl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.MingXiDetail;
import com.jzkl.Bean.MyOrder;
import com.jzkl.R;

import java.util.List;

public class MingXiDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<MingXiDetail> mList;

    public MingXiDetailAdapter(Context mContext, List<MingXiDetail> mList){
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
        MingXiDetail list = (MingXiDetail) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_mingxi_detail,null);
            shopHolder.ming_title  = convertView.findViewById(R.id.item_ming_title);
            shopHolder.ming_time  = convertView.findViewById(R.id.item_ming_time);
            shopHolder.ming_price  = convertView.findViewById(R.id.item_ming_price);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(Integer.parseInt(list.getDetailPrice())>0){
            shopHolder.ming_price.setTextColor(Color.parseColor("#D07D80"));
            shopHolder.ming_price.setText(list.getDetailPrice());
        }else {
            shopHolder.ming_price.setTextColor(Color.parseColor("#a3a3a3"));
            shopHolder.ming_price.setText(list.getDetailPrice());
        }
        shopHolder.ming_title.setText(list.getDetailTitle());
        shopHolder.ming_time.setText(list.getDetailTime());

        return convertView;
    }

    class ShopGridHolder{
        TextView ming_title;
        TextView ming_time;
        TextView ming_price;
    }
}
