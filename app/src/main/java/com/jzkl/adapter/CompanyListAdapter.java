package com.jzkl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Bean.Company;
import com.jzkl.Bean.DkApply;
import com.jzkl.R;

import java.util.List;

public class CompanyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Company> mList;

    public CompanyListAdapter(Context mContext, List<Company> mList){
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
        Company company = (Company) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_company_detail,null);
            shopHolder.company_img  = convertView.findViewById(R.id.item_company_img);
            shopHolder.company_name  = convertView.findViewById(R.id.item_company_name);
            shopHolder.company_tel  = convertView.findViewById(R.id.item_company_tel);
            shopHolder.company_address  = convertView.findViewById(R.id.item_company_address);
            shopHolder.company_decs  = convertView.findViewById(R.id.item_company_decs);
            convertView.setTag(shopHolder);
        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }

        shopHolder.company_img.setImageResource(company.getmCompanyImg());
        shopHolder.company_name.setText("联系人姓名："+company.getmCompanyName());
        shopHolder.company_tel.setText("业务电话："+company.getmCompanyTel());
        shopHolder.company_address.setText("营业地址："+company.getmCompanyAear());
        shopHolder.company_decs.setText(mContext.getResources().getString(company.getmCompanyDecs()));

        return convertView;
    }

    class ShopGridHolder{
        ImageView company_img;
        TextView company_name;
        TextView company_tel;
        TextView company_address;
        TextView company_decs;
    }
}
