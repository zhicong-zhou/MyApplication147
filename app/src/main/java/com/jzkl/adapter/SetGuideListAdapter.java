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
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class SetGuideListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankList> mList;

    public SetGuideListAdapter(Context mContext, List<BankList> mList){
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
        BankList bankList = (BankList) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_set_guide_list,null);
            shopHolder.guide_num  = convertView.findViewById(R.id.item_set_guide_num);
            shopHolder.guide_name  = convertView.findViewById(R.id.item_guide_name);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
         shopHolder.guide_num.setText(bankList.getBankNo());
        shopHolder.guide_name.setText(bankList.getBankName());
        return convertView;
    }

    class ShopGridHolder{
        TextView guide_num;
        TextView guide_name;
    }
}
