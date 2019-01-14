package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.ShopSize;
import com.jzkl.R;

import java.util.List;

public class ShopSizeAdapter extends BaseAdapter {

    private List<ShopSize> list;
    private Context context;
    private int mSelection = 0;
    private Resources resources;
    ShopSizeHolder holder;

    public  ShopSizeAdapter(Context context,List<ShopSize> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShopSize shopSize = (ShopSize) getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        holder = null;

        if(convertView==null){
            holder = new ShopSizeHolder();
            convertView = inflater.inflate(R.layout.item_shop_size,null);
            holder.sizeNmae = convertView.findViewById(R.id.item_shop_sizeNmae);

            convertView.setTag(holder);
        }else {
            holder = (ShopSizeHolder) convertView.getTag();
        }
        holder.sizeNmae.setText(shopSize.getShopSizeColor());
        secletBg(position);
        return convertView;
    }


    private void secletBg(int position) {
        if(position == mSelection){
            resources = context.getResources();
            holder.sizeNmae.setTextColor(resources.getColor(R.color.white));
            /*选中下边有个蓝色线*/
            holder.sizeNmae.setBackgroundResource(R.drawable.shape_size_selectd);
        }else {
            holder.sizeNmae.setTextColor(resources.getColor(R.color.home_word));
            /*没选中下边白色线*/
            holder.sizeNmae.setBackgroundResource(R.drawable.shape_eidt);
        }
    }

    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }

    class ShopSizeHolder{
        TextView sizeNmae;
    }
}
