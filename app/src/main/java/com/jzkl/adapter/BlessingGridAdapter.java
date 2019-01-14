package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzkl.Bean.FeedBackButton;
import com.jzkl.R;

import java.util.List;

public class BlessingGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<FeedBackButton> mList;
    private int mSelection = 0;
    private Resources resources;
    ShopGridHolder shopHolder;

    public BlessingGridAdapter(Context mContext, List<FeedBackButton> mList){
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
        FeedBackButton list = (FeedBackButton) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_blessing_grid,null);
            shopHolder.blessing_qy  = convertView.findViewById(R.id.pop_blessing_qy);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.blessing_qy.setText(list.getFeedbackBConten());

        secletBg(position);
        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = mContext.getResources();
            shopHolder.blessing_qy.setTextColor(resources.getColor(R.color.my));
            /*选中下边有个蓝色线*/
            shopHolder.blessing_qy.setBackgroundResource(R.drawable.shape_blessing_selected);
        }else {
            shopHolder.blessing_qy.setTextColor(resources.getColor(R.color.home_word));
            /*没选中下边白色线*/
            shopHolder.blessing_qy.setBackgroundResource(R.drawable.shape_eidt);
        }
    }

    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }

    class ShopGridHolder{
        TextView blessing_qy;
    }
}
