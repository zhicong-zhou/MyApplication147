package com.jzkl.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jzkl.Bean.Charge;
import com.jzkl.R;
import java.util.List;

public class AgentChargeGridAdapter extends BaseAdapter {

    List<Charge> list;
    Context context;
    ChargeHolder shopHolder;
    private int mSelection = 0;
    private Resources resources;

    public AgentChargeGridAdapter(Context context,List<Charge> list){
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
        Charge charge = (Charge) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        shopHolder = null;

        if(convertView==null){
            shopHolder = new ChargeHolder();
            convertView = inflater.inflate(R.layout.item_agent_charge_grid,null);
            shopHolder.charge_ll  = convertView.findViewById(R.id.item_charge_ll);
            shopHolder.charge_money  = convertView.findViewById(R.id.item_charge_money);
            shopHolder.charge_num  = convertView.findViewById(R.id.item_charge_num);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ChargeHolder) convertView.getTag();
        }
        shopHolder.charge_money.setText(charge.getChargeMoney());
        shopHolder.charge_num.setText(charge.getChargeTitle());

        secletBg(position);
        return convertView;
    }

    private void secletBg(int position) {
        if(position == mSelection){
            resources = context.getResources();
            /*选中下边有个蓝色线*/
            shopHolder.charge_ll.setBackgroundResource(R.drawable.shape_charge_selected);
            shopHolder.charge_money.setTextColor(resources.getColor(R.color.white));
            shopHolder.charge_num.setTextColor(resources.getColor(R.color.white));
        }else {
            /*没选中下边白色线*/
            shopHolder.charge_ll.setBackgroundResource(R.drawable.shape_charge_select);
            shopHolder.charge_money.setTextColor(resources.getColor(R.color.home_word));
            shopHolder.charge_num.setTextColor(resources.getColor(R.color.home_word));
        }
    }


    //返回当前CheckBox选中的位置,便于获取值.
    public void  setSelection(int selection){
        this.mSelection = selection;
        notifyDataSetChanged();
    }



    class ChargeHolder{
        LinearLayout charge_ll;
        TextView charge_money;
        TextView charge_num;
    }
}
