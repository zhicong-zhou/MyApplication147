package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Bean.FeedBackButton;
import com.jzkl.R;

import java.util.List;

public class BlessingListAdapter extends BaseAdapter {

    private Context mContext;
    private List<FeedBackButton> mList;
    private int mSelection = 0;
    private Resources resources;
    ShopGridHolder shopHolder;

    public BlessingListAdapter(Context mContext, List<FeedBackButton> mList){
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
            convertView = inflater.inflate(R.layout.item_blessing_list,null);
            shopHolder.blessing_ll  = convertView.findViewById(R.id.item_blessing_ll);
            shopHolder.blessing_list3  = convertView.findViewById(R.id.item_blessing_list3);
            shopHolder.blessing3_name  = convertView.findViewById(R.id.item_blessing3_name);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.blessing_list3.setText(list.getFeedbackBConten());
        shopHolder.blessing3_name.setText(list.getFeedbackBName()+"：");

        secletBg(position);
        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = mContext.getResources();
            /*选中下边有个蓝色线*/
            shopHolder.blessing_ll.setBackgroundResource(R.drawable.shape_blessing_selected);
            shopHolder.blessing_list3.setTextColor(resources.getColor(R.color.my));
            shopHolder.blessing3_name.setTextColor(resources.getColor(R.color.my));
        }else {
            /*没选中下边白色线*/
            shopHolder.blessing_ll.setBackgroundResource(R.drawable.shape_eidt);
            shopHolder.blessing_list3.setTextColor(resources.getColor(R.color.comment_word));
            shopHolder.blessing3_name.setTextColor(resources.getColor(R.color.home_word));
        }
    }

    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }

    class ShopGridHolder{
        LinearLayout blessing_ll;
        TextView blessing_list3;
        TextView blessing3_name;
    }
}
