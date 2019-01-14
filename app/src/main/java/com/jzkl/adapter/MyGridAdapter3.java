package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.MyJuan;
import com.jzkl.Bean.MyService;
import com.jzkl.R;

import java.util.List;

public class MyGridAdapter3 extends BaseAdapter {

    private Context mContext;
    private List<MyJuan> mList;

    public MyGridAdapter3(Context mContext, List<MyJuan> mList){
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
        MyJuan list = (MyJuan) getItem(position);

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
        shopHolder.my_name.setText(list.getMyJuanName());
        shopHolder.my_img.setImageResource(list.getMyJuanImg());

        return convertView;
    }

    class ShopGridHolder{
        ImageView my_img;
        TextView my_name;
    }
}
