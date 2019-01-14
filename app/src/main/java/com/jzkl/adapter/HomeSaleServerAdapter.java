package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.HomeSaleServer;
import com.jzkl.R;

import java.util.List;

public class HomeSaleServerAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeSaleServer> mList;

    public HomeSaleServerAdapter(Context mContext, List<HomeSaleServer> mList){
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
        HomeSaleServer list = (HomeSaleServer) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_home_sale,null);
            shopHolder.salebg  = convertView.findViewById(R.id.item_home_salebg);
            shopHolder.sale_reason  = convertView.findViewById(R.id.item_home_sale_reason);
            shopHolder.sale_content  = convertView.findViewById(R.id.item_home_sale_content);
            shopHolder.saleImg  = convertView.findViewById(R.id.item_home_saleImg);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.sale_reason.setText(list.getHomeSaleReason());
        shopHolder.sale_content.setText(list.getHomeSaleContent());
        shopHolder.saleImg.setImageResource(list.getHomeSaleImg());
        if(position == 0){
            shopHolder.salebg.setImageResource(R.mipmap.home_poble_agent);
        }else if(position == 1){
            shopHolder.salebg.setImageResource(R.mipmap.home_poble_jjcg);
        }else if(position == 2){
            shopHolder.salebg.setImageResource(R.mipmap.home_poble_lqhc);
        }
        return convertView;
    }

    class ShopGridHolder{
        ImageView salebg;
        TextView sale_reason;
        TextView sale_content;
        ImageView saleImg;
    }
}
