package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.IntergraterH;
import com.jzkl.Bean.MyOrder;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class IntergHListAdapter extends BaseAdapter {

    private Context mContext;
    private List<IntergraterH> mList;

    public IntergHListAdapter(Context mContext, List<IntergraterH> mList){
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
        IntergraterH list = (IntergraterH) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_integrter_huan,null);
            shopHolder.integr_img  = convertView.findViewById(R.id.item_integr_img);
            shopHolder.integr_name  = convertView.findViewById(R.id.item_integr_name);
            shopHolder.integr_jifen  = convertView.findViewById(R.id.item_integr_jifen);
            shopHolder.integr_price  = convertView.findViewById(R.id.item_integr_price);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(list.getInterHImg().equals("")){
            shopHolder.integr_img.setImageResource(R.mipmap.shop_bg_img);
        }else {
            OkHttpUtils.picassoImage(list.getInterHImg(),mContext,shopHolder.integr_img);
        }
        shopHolder.integr_name.setText(list.getInterHName());
        shopHolder.integr_jifen.setText(list.getInterHNum()+"积分");
        shopHolder.integr_price.setText("市场价：￥"+list.getInterHPrice());

        return convertView;
    }

    class ShopGridHolder{
        ImageView integr_img;
        TextView integr_name;
        TextView integr_jifen;
        TextView integr_price;
    }
}
