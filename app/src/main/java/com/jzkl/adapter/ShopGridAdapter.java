package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Bean.ShopList;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class ShopGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopListShop> mList;
    private int number;
    ShopGridHolder shopHolder;

    public ShopGridAdapter(Context mContext, List<ShopListShop> mList,int number){
        this.mContext = mContext;
        this.mList = mList;
        this.number = number;
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
        ShopListShop list = (ShopListShop) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_shop_grid,null);
            shopHolder.shop_ll  = convertView.findViewById(R.id.itme_shop_ll);
            shopHolder.shop_img  = convertView.findViewById(R.id.item_shop_img);
            shopHolder.shop_price  = convertView.findViewById(R.id.item_shop_price);
            shopHolder.shop_name  = convertView.findViewById(R.id.item_shop_name);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        /*number 0 是显示俩个按钮  1  不显示*/
        if(number == 0){
            shopHolder.shop_ll.setVisibility(View.VISIBLE);
        }else if(number == 1){
            shopHolder.shop_ll.setVisibility(View.GONE);
        }
        shopHolder.shop_name.setText(list.getShopName());
        if(list.getShopType().equals("1")){
            shopHolder.shop_price.setText("￥"+list.getShopPrice());
        }else{
            shopHolder.shop_price.setText(list.getShopCredit()+"积分");
        }
        if(list.getShopImage()!=null){
            if(list.getShopImage().equals("")){
                shopHolder.shop_img.setImageResource(R.mipmap.shop_name_img1);
            }else {
                OkHttpUtils.picassoImage(list.getShopImage(),mContext,shopHolder.shop_img);
            }
        }
//        /*库存 是不有*/
//        if(Integer.parseInt(list.getShopKuCun())<=0){
//
//        }
        return convertView;
    }

    class ShopGridHolder{
        LinearLayout shop_ll;
        ImageView shop_img;
        TextView shop_price;
        TextView shop_name;
    }
}
