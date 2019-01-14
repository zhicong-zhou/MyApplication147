package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jzkl.Bean.HomeYHList;
import com.jzkl.R;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class HomeYhListAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeYHList> mList;

    public HomeYhListAdapter(Context mContext, List<HomeYHList> mList){
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
        HomeYHList list = (HomeYHList) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_home_yh_list,null);
            shopHolder.yhimg  = convertView.findViewById(R.id.item_home_yhimg);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }

        if(list.getHomeYhImg().equals("")){
            shopHolder.yhimg.setImageResource(R.mipmap.user_head);
        }else {
            OkHttpUtils.picassoImage(list.getHomeYhImg(),mContext,shopHolder.yhimg);
        }
        return convertView;
    }

    class ShopGridHolder{
        ImageViewPlus yhimg;
    }
}
