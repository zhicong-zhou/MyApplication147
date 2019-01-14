package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzkl.Bean.ShopList;
import com.jzkl.R;

import java.util.List;

public class ShopTitleAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopList> mShopList;
    private int mSelection = 0;
    private Resources resources;
    ShopHolder shopHolder;

    public ShopTitleAdapter(Context mContext, List<ShopList> mShopList){
        this.mContext = mContext;
        this.mShopList = mShopList;
    }

    @Override
    public int getCount() {
        return mShopList.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopList shopList = (ShopList) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopHolder();
            convertView = inflater.inflate(R.layout.item_shop_list,null);
            shopHolder.textView  = convertView.findViewById(R.id.item_shop_title);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopHolder) convertView.getTag();
        }
        shopHolder.textView.setText(shopList.getShopTitle());
        secletBg(position);

        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = mContext.getResources();
            shopHolder.textView.setTextColor(resources.getColor(R.color.home_worded));
            /*选中下边有个蓝色线*/
            shopHolder.textView.setBackgroundResource(R.drawable.shape_orde1);
        }else {
            shopHolder.textView.setTextColor(resources.getColor(R.color.home_word));
            /*没选中下边白色线*/
            shopHolder.textView.setBackgroundResource(R.drawable.shape_orde_no);
        }
    }

    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }
    class ShopHolder{
        TextView textView;
    }
}
