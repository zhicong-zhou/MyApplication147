package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jzkl.Bean.Image;
import com.jzkl.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Image> mList;

    public ImageAdapter(Context mContext, List<Image> mList){
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
        Image list = (Image) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_imgae_url,null);
            shopHolder.feedback_img  = convertView.findViewById(R.id.item_feedback_img);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(list.getImageUrl().equals("")){
            shopHolder.feedback_img.setImageResource(R.mipmap.user_head);
        }else {
            Glide.with(mContext).load(list.getImageUrl()).into(shopHolder.feedback_img);
        }
        return convertView;
    }

    class ShopGridHolder{
        ImageView feedback_img;
    }
}
