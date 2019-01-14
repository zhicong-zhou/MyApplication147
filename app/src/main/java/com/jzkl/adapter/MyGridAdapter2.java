package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.MyOrder;
import com.jzkl.Bean.MyService;
import com.jzkl.R;

import java.util.List;

public class MyGridAdapter2 extends BaseAdapter {

    private Context mContext;
    private List<MyService> mList;

    public MyGridAdapter2(Context mContext, List<MyService> mList){
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
        MyService list = (MyService) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_my_grid,null);
            shopHolder.my_img  = convertView.findViewById(R.id.item_my_img);
            shopHolder.my_name  = convertView.findViewById(R.id.item_my_name);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.my_name.setText(list.getMyServiceName());
        shopHolder.my_img.setImageResource(list.getMyServiceImg());

        return convertView;
    }

    class ShopGridHolder{
        ImageView my_img;
        TextView my_name;
    }
}
