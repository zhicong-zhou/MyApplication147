package com.jzkl.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class BankListAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankList> mList;

    public BankListAdapter(Context mContext, List<BankList> mList){
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
            convertView = inflater.inflate(R.layout.item_credit_list,null);
            shopHolder.bank_img  = convertView.findViewById(R.id.item_bank_img);
            shopHolder.bank_name  = convertView.findViewById(R.id.item_bank_name);
            shopHolder.bank_but  = convertView.findViewById(R.id.item_credit_bank_but);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(bankList.getBankUserImg().equals("")){
            shopHolder.bank_img.setImageResource(R.mipmap.ab);
        }else {
            OkHttpUtils.picassoImage(bankList.getBankUserImg(),mContext,shopHolder.bank_img);
        }
        shopHolder.bank_name.setText(bankList.getBankName());
        shopHolder.bank_but.setText(bankList.getBankClass());


        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) shopHolder.bank_but.getLayoutParams();
        params.width= (int) ((width/3-30)*0.8);//设置当前控件布局的高度
        shopHolder.bank_but.setLayoutParams(params);//将设置好的布局参数应用到控件中

        return convertView;
    }

    class ShopGridHolder{
        ImageView bank_img;
        TextView bank_name;
        TextView bank_but;
    }
}
