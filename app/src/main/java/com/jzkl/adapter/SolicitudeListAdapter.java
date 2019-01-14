package com.jzkl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Bean.SolicituList;
import com.jzkl.R;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.OkHttpUtils;

import java.util.List;

public class SolicitudeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SolicituList> mList;
    ShopGridHolder shopHolder;
    private CheckInterface checkInterface;

    public SolicitudeListAdapter(Context mContext, List<SolicituList> mList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SolicituList list = (SolicituList) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_solicitude_peple, null);
            shopHolder.solicu_ll = convertView.findViewById(R.id.item_solicu_ll);
            shopHolder.solicu_check = convertView.findViewById(R.id.item_solicu_check);
            shopHolder.solicu_head = convertView.findViewById(R.id.item_solicu_head);
            shopHolder.solicu_name = convertView.findViewById(R.id.item_solicu_name);
            shopHolder.solicu_renz = convertView.findViewById(R.id.item_solicu_renz);
            shopHolder.solicu_jibie = convertView.findViewById(R.id.item_solicu_jibie);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        if(list.getmSHead()=="null" || list.getmSHead().equals("null")){
            shopHolder.solicu_head.setImageResource(R.mipmap.user_head);
        }else {
            if(!list.getmSHead().equals("")){
                OkHttpUtils.picassoImage(list.getmSHead(),mContext,shopHolder.solicu_head);
            }else {
                shopHolder.solicu_head.setImageResource(R.mipmap.user_head);
            }
        }

        shopHolder.solicu_name.setText(list.getmSName());
        if(list.getmSRenzJb()!=null){
            shopHolder.solicu_jibie.setText(list.getmSRenzJb());
        }
        if(list.getmSRenz() !="null"){
            shopHolder.solicu_renz.setText(list.getmSRenz());
        }else {
            shopHolder.solicu_renz.setText(list.getmSTel());
        }

        boolean choosed = list.isChoosed();
        if (choosed){
            shopHolder.solicu_check.setChecked(true);
        }else{
            shopHolder.solicu_check.setChecked(false);
        }
        /*check选择*/
        shopHolder.solicu_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
            }
        });
        return convertView;
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }

    class ShopGridHolder {
        LinearLayout solicu_ll;
        CheckBox solicu_check;
        ImageViewPlus solicu_head;
        TextView solicu_name;
        TextView solicu_renz;
        TextView solicu_jibie;
    }
}
