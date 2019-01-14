package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jzkl.Bean.MingXi;
import com.jzkl.Bean.MingXiDetail;
import com.jzkl.Bean.MyOrder;
import com.jzkl.R;
import com.jzkl.util.MyListView;

import java.util.ArrayList;
import java.util.List;

public class MingXiMonthAdapter extends BaseAdapter {

    private Context mContext;
    private List<MingXi> mList;

    private List<MingXiDetail> mListDtail;
    private String detailTitle[] = {"商户提现","商户签到"};
    private String detailTime[] = {"2018-08-16","2018-08-18"};
    private String detailPrice[] = {"48","37"};

    public MingXiMonthAdapter(Context mContext, List<MingXi> mList){
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
        MingXi list = (MingXi) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_integrter_mingxi,null);
            shopHolder.integrter_month  = convertView.findViewById(R.id.item_integrter_month);
            shopHolder.month_list  = convertView.findViewById(R.id.item_mingxi_month_list);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.integrter_month.setText(list.getMingxiMonth());

        mListDtail = new ArrayList<>();
        getData();
        MingXiDetailAdapter adapter = new MingXiDetailAdapter(mContext,mListDtail);
        shopHolder.month_list.setAdapter(adapter);
        return convertView;
    }

    private void getData() {
        for (int i = 0; i <detailTitle.length ; i++) {
            MingXiDetail mingXiDetail = new MingXiDetail();
            mingXiDetail.setDetailPrice(detailPrice[i]);
            mingXiDetail.setDetailTitle(detailTitle[i]);
            mingXiDetail.setDetailTime(detailTime[i]);

            mListDtail.add(mingXiDetail);
        }
    }

    class ShopGridHolder{
        MyListView month_list;
        TextView integrter_month;
    }
}
