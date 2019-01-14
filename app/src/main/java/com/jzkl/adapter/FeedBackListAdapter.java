package com.jzkl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzkl.Bean.FeedBack;
import com.jzkl.Bean.Image;
import com.jzkl.R;
import com.jzkl.util.MyGridView;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedBackListAdapter extends BaseAdapter {

    private Context mContext;
    private List<FeedBack> mList;

    List<Image> listImage;
    private int [] feedbackImg = {R.mipmap.shop_name_img2,R.mipmap.shop_name_img2,R.mipmap.shop_name_img2,R.mipmap.shop_name_img2};
    JSONArray array;

    public FeedBackListAdapter(Context mContext, List<FeedBack> mList){
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
        FeedBack list = (FeedBack) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_feedback_detail,null);
            shopHolder.feedback_title  = convertView.findViewById(R.id.item_feedback_title);
            shopHolder.feedback_status  = convertView.findViewById(R.id.item_feedback_status);
            shopHolder.feedback_gird  = convertView.findViewById(R.id.item_feedback_gird);
            shopHolder.feedback_txt  = convertView.findViewById(R.id.item_feedback_txt);
            shopHolder.feedback_time  = convertView.findViewById(R.id.item_feedback_time);
            shopHolder.feedback_descript  = convertView.findViewById(R.id.item_feedback_descript);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.feedback_title.setText(list.getmFeedBackTitle());
        shopHolder.feedback_status.setText(list.getmFeedBackStatus());
        shopHolder.feedback_txt.setText(list.getmFeedBackContent());
        shopHolder.feedback_time.setText(list.getmFeedBackTime());
        if(list.getmFeedBackDescript().equals("null")){
            shopHolder.feedback_descript.setVisibility(View.GONE);
        }else {
            shopHolder.feedback_descript.setVisibility(View.VISIBLE);
            shopHolder.feedback_descript.setText("回复："+list.getmFeedBackDescript());
        }

        array = list.getmList();
        listImage = new ArrayList<>();
        getDate();
        ImageAdapter adapter = new ImageAdapter(mContext,listImage);
        shopHolder.feedback_gird.setAdapter(adapter);
        return convertView;
    }

    private void getDate() {
        for (int i = 0; i <array.length() ; i++) {
            try {
                String img= (String) array.get(i);
                Image image = new Image();
                image.setImageUrl(img);
                listImage.add(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ShopGridHolder{
        TextView feedback_title;
        TextView feedback_status;
        MyGridView feedback_gird;
        TextView feedback_txt;
        TextView feedback_descript;
        TextView feedback_time;
    }
}
