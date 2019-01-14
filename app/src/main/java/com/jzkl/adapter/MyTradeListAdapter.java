package com.jzkl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzkl.Bean.Address;
import com.jzkl.Bean.MyTrade;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;

import java.util.List;

public class MyTradeListAdapter extends BaseAdapter {

    private Activity  mActivity;
    private List<MyTrade> mList;
    ShopGridHolder shopHolder;

    public MyTradeListAdapter(Activity  mContext, List<MyTrade> mList) {
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
        final MyTrade myTrade = (MyTrade) getItem(position);

        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_trade_list, null);
            shopHolder.trade_card = convertView.findViewById(R.id.item_trade_card);
            shopHolder.trade_money = convertView.findViewById(R.id.item_trade_money);
            shopHolder.trade_hua = convertView.findViewById(R.id.item_trade_hua);
            shopHolder.trade_time = convertView.findViewById(R.id.item_trade_time);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.trade_card.setText(myTrade.getTradeCard());
        shopHolder.trade_money.setText(myTrade.getTradeMoney());
        shopHolder.trade_hua.setText("消费"+myTrade.getTradeHua());
        shopHolder.trade_time.setText("时间"+myTrade.getTradeTime());

        return convertView;
    }
    class ShopGridHolder {
        TextView trade_card;
        TextView trade_money;
        TextView trade_hua;
        TextView trade_time;
    }
}
