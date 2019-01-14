package com.jzkl.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzkl.Bean.PayWay;
import com.jzkl.R;
import com.jzkl.view.activity.AddressEidtorActivity;

import java.util.List;

public class PayWayListAdapter extends BaseAdapter {

    private Activity  mActivity;
    private List<PayWay> mList;
    private int mSelection = 0;
    ShopGridHolder shopHolder;
    private Resources resources;

    public PayWayListAdapter(Activity  mContext, List<PayWay> mList) {
        this.mActivity = mContext;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        PayWay list = (PayWay) getItem(position);

        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_pay_way, null);
            shopHolder.pay_check = convertView.findViewById(R.id.itme_pay_check);
            shopHolder.item_pay_img = convertView.findViewById(R.id.item_pay_img);
            shopHolder.item_pay_title = convertView.findViewById(R.id.item_pay_title);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.item_pay_img.setImageResource(list.getPayWayImg());
        shopHolder.item_pay_title.setText(list.getPayWayTitle());

        secletBg(position);
        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = mActivity.getResources();
            /*选中下边有个蓝色线*/
            shopHolder.pay_check.setImageResource(R.mipmap.pay_selected);
        }else {
            /*没选中下边白色线*/
            shopHolder.pay_check.setImageResource(R.mipmap.pay_select);
        }
    }


    //返回当前CheckBox选中的位置,便于获取值.
    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }


    class ShopGridHolder {
        ImageView item_pay_img;
        TextView item_pay_title;
        ImageView pay_check;
    }
}
