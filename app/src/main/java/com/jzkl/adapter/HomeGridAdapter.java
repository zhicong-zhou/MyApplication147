package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.HomeGrid;
import com.jzkl.Bean.MyOrder;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class HomeGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeGrid> mList;

    public HomeGridAdapter(Context mContext, List<HomeGrid> mList){
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
        HomeGrid list = (HomeGrid) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_home_grid,null);
            shopHolder.my_img  = convertView.findViewById(R.id.item_home_img);
            shopHolder.my_name  = convertView.findViewById(R.id.item_home_name);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.my_name.setText(list.getMyOrderName());
        if(list.getMyOrderImg().equals("")){
            shopHolder.my_img.setImageResource(R.mipmap.home_yqtg);
        }else {
            OkHttpUtils.picassoImage(list.getMyOrderImg(),mContext,shopHolder.my_img);
        }
        return convertView;
    }

    class ShopGridHolder{
        ImageView my_img;
        TextView my_name;
    }
}
