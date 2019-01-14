package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jzkl.Bean.SolicitudeName;
import com.jzkl.R;
import com.jzkl.util.ImageViewPlus;

import java.util.List;

public class SolicitudeGridAdapter1 extends BaseAdapter {

    private Context mContext;
    private List<SolicitudeName> mList;
    ShopGridHolder shopHolder;

    public SolicitudeGridAdapter1(Context mContext, List<SolicitudeName> mList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SolicitudeName list = (SolicitudeName) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            /*item_solicitude_grid 不用了*/
            convertView = inflater.inflate(R.layout.item_birthday_grid1, null);
            shopHolder.silici_ll = convertView.findViewById(R.id.item_silici1_ll);
            shopHolder.silici_img = convertView.findViewById(R.id.item_silici1_img);
            shopHolder.silici_name = convertView.findViewById(R.id.item_silici1_name);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(list.getsImg().equals("")||list.getsImg().equals("null")){
            shopHolder.silici_img.setImageResource(R.mipmap.user_head);
        }else {
            Glide.with(mContext).load(list.getsImg()).into(shopHolder.silici_img);
        }
        shopHolder.silici_name.setText(list.getsName());
        return convertView;
    }


    class ShopGridHolder {
        LinearLayout silici_ll;
        ImageView silici_img;
        TextView silici_name;
    }
}
