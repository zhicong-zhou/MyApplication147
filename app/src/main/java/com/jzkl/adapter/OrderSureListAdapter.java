package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.MyOrder;
import com.jzkl.Bean.OrderSure;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class OrderSureListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderSure> mList;

    public OrderSureListAdapter(Context mContext, List<OrderSure> mList){
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
        OrderSure list = (OrderSure) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_order_sure,null);
            shopHolder.sure_img  = convertView.findViewById(R.id.item_order_sure_img);
            shopHolder.sure_name  = convertView.findViewById(R.id.item_order_sure_name);
            shopHolder.sure_color  = convertView.findViewById(R.id.item_order_sure_color);
            shopHolder.sure_num  = convertView.findViewById(R.id.item_order_sure_num);
            shopHolder.sure_price  = convertView.findViewById(R.id.item_order_sure_price);
            shopHolder.sure_kprice  = convertView.findViewById(R.id.item_order_sure_kprice);
            shopHolder.sure_kuaidi  = convertView.findViewById(R.id.item_order_sure_kuaidi);
            shopHolder.sure_feedback  = convertView.findViewById(R.id.item_order_sure_feedback);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(list.getSureImg().equals("")){
            shopHolder.sure_img.setImageResource(R.mipmap.pop_shop_img);
        }else {
            OkHttpUtils.picassoImage(list.getSureImg(),mContext,shopHolder.sure_img);
        }
        shopHolder.sure_name.setText(list.getSureName());
        shopHolder.sure_color.setText(list.getSureColor());
        shopHolder.sure_num.setText("x"+list.getSureNum());
        if(list.getSureType().equals("1")){
            shopHolder.sure_price.setText("￥"+list.getSurePrice());
        }else {
            shopHolder.sure_price.setText(list.getSurePrice()+"积分");
        }
        /*list.getSureKPrice()*/
        shopHolder.sure_kprice.setText("￥0.00");
        shopHolder.sure_kuaidi.setText(list.getSureKStatus());
        shopHolder.sure_feedback.setText(list.getSureFeedback());

        return convertView;
    }

    class ShopGridHolder{
        ImageView sure_img;
        TextView sure_name;
        TextView sure_color;
        TextView sure_num;
        TextView sure_price;
        TextView sure_kprice;
        TextView sure_kuaidi;
        EditText sure_feedback;
    }
}
