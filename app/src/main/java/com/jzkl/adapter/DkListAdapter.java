package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Bean.BankList;
import com.jzkl.Bean.DkApply;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class DkListAdapter extends BaseAdapter {

    private Context mContext;
    private List<DkApply> mList;

    public DkListAdapter(Context mContext, List<DkApply> mList){
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
        DkApply dkApply = (DkApply) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_dk_apply,null);
            shopHolder.apply_img  = convertView.findViewById(R.id.item_dk_apply_img);
            shopHolder.apply_title  = convertView.findViewById(R.id.item_dk_apply_title);
            shopHolder.apply_money  = convertView.findViewById(R.id.item_dk_apply_money);
            shopHolder.apply_yj  = convertView.findViewById(R.id.item_dk_apply_yj);
            shopHolder.apply_but  = convertView.findViewById(R.id.item_dk_apply_but);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }

//        if(dkApply.getDkImg().equals("")){
            shopHolder.apply_img.setImageResource(R.mipmap.ab);
//        }else {
//            OkHttpUtils.picassoImage(dkApply.getDkImg(),mContext,shopHolder.apply_img);
//        }
        shopHolder.apply_title.setText(dkApply.getDkTitle());
        shopHolder.apply_money.setText(dkApply.getDkMoney());
        shopHolder.apply_yj.setText("月利率"+dkApply.getDkYj());

        return convertView;
    }

    class ShopGridHolder{
        ImageView apply_img;
        TextView apply_title;
        TextView apply_money;
        TextView apply_yj;
        TextView apply_but;
    }
}
