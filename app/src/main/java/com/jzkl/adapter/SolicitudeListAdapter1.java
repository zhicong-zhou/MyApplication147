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

public class SolicitudeListAdapter1 extends BaseAdapter {

    private Context mContext;
    private List<SolicituList> mList;
    ShopGridHolder shopHolder;
    private CheckInterface checkInterface;

    public SolicitudeListAdapter1(Context mContext, List<SolicituList> mList) {
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
            convertView = inflater.inflate(R.layout.item_solicitude_grid, null);
            shopHolder.silici_ll = convertView.findViewById(R.id.item_silici_ll);
            shopHolder.silici_img = convertView.findViewById(R.id.item_silici_img);
            shopHolder.silici_name = convertView.findViewById(R.id.item_silici_name);
            shopHolder.silici_check = convertView.findViewById(R.id.item_silici_check);
            convertView.setTag(shopHolder);
        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }

        if(list.getmSHead()=="null" || list.getmSHead().equals("null")){
            shopHolder.silici_img.setImageResource(R.mipmap.user_head);
        }else {
            if(!list.getmSHead().equals("")){
                OkHttpUtils.picassoImage(list.getmSHead(),mContext,shopHolder.silici_img);
            }else {
                shopHolder.silici_img.setImageResource(R.mipmap.user_head);
            }
        }
        shopHolder.silici_name.setText(list.getmSName());
        boolean choosed = list.isChoosed();
        if (choosed){
            shopHolder.silici_check.setChecked(true);
        }else{
            shopHolder.silici_check.setChecked(false);
        }
        /*check选择*/
        shopHolder.silici_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup1(position, ((CheckBox) v).isChecked());//向外暴露接口
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
        void checkGroup1(int position, boolean isChecked);
    }

    class ShopGridHolder {
        LinearLayout silici_ll;
        ImageViewPlus silici_img;
        TextView silici_name;
        CheckBox silici_check;
    }
}
