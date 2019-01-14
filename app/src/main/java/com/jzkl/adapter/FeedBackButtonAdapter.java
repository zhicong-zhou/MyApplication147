package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzkl.Bean.FeedBackButton;
import com.jzkl.Bean.Image;
import com.jzkl.R;
import com.jzkl.util.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class FeedBackButtonAdapter extends BaseAdapter {

    private Context mContext;
    private List<FeedBackButton> mList;
    private int mSelection = 0;
    private Resources resources;
    ShopGridHolder shopHolder;

    public FeedBackButtonAdapter(Context mContext, List<FeedBackButton> mList){
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
            convertView = inflater.inflate(R.layout.item_feedback_button,null);
            shopHolder.feedback_content  = convertView.findViewById(R.id.item_feedback_content);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.feedback_content.setText(list.getFeedbackBConten());

        secletBg(position);
        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = mContext.getResources();
            shopHolder.feedback_content.setTextColor(resources.getColor(R.color.white));
            /*选中下边有个蓝色线*/
            shopHolder.feedback_content.setBackgroundResource(R.drawable.shape_size_selectd);
        }else {
            shopHolder.feedback_content.setTextColor(resources.getColor(R.color.home_word));
            /*没选中下边白色线*/
            shopHolder.feedback_content.setBackgroundResource(R.drawable.shape_feedback);
        }
    }

    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }

    class ShopGridHolder{
        TextView feedback_content;
    }
}
