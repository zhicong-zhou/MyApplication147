package com.jzkl.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.MyOrder;
import com.jzkl.Bean.ShopJiBuy;
import com.jzkl.R;

import java.util.List;

public class ShopJIBuyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopJiBuy> mList;

    public ShopJIBuyListAdapter(Context mContext, List<ShopJiBuy> mList){
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
        ShopJiBuy list = (ShopJiBuy) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_ji_buy,null);
            shopHolder.buy_img  = convertView.findViewById(R.id.item_ji_buy_img);
            shopHolder.buy_name  = convertView.findViewById(R.id.item_jibuy_name);
            shopHolder.buy_content  = convertView.findViewById(R.id.item_jibuy_content);
            shopHolder.buy_price  = convertView.findViewById(R.id.item_jibuy_price);
            shopHolder.buy_priced  = convertView.findViewById(R.id.item_jibuy_priced);
            shopHolder.buy_num  = convertView.findViewById(R.id.item_jibuy_num);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.buy_img.setImageResource(list.getJIBuyImg());
        shopHolder.buy_name.setText(list.getJIBuyName());
        shopHolder.buy_content.setText(list.getJIBuyContent());
        shopHolder.buy_price.setText("￥"+list.getJIBuyPrice());

        shopHolder.buy_priced.setText("原价￥"+list.getJIBuyPriced());
        shopHolder.buy_priced.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        shopHolder.buy_num.setText("仅剩"+list.getJIBuyNum()+"件");

        return convertView;
    }

    class ShopGridHolder{
        ImageView buy_img;
        TextView buy_name;
        TextView buy_content;
        TextView buy_price;
        TextView buy_priced;
        TextView buy_num;
    }
}
