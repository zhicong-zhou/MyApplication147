package com.jzkl.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jzkl.Bean.Address;
import com.jzkl.Bean.CarShop;
import com.jzkl.Bean.EventsCar;
import com.jzkl.R;
import com.jzkl.util.OkHttpUtils;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.AddressEidtorActivity;
import com.jzkl.view.activity.ShopDetailActivity;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class CarListAdapter extends BaseAdapter {

    private Activity  mActivity;
    private List<CarShop> mList;
    ShopGridHolder shopHolder;
    private ModifyCountInterface modifyCountInterface;
    private CheckInterface checkInterface;
    String userinfo, token,minNum,addNum;

    public CarListAdapter(Activity  mContext, List<CarShop> mList) {
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
        getUser();
        final CarShop list = (CarShop) getItem(position);
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_car_list, null);
            shopHolder.car_detail = convertView.findViewById(R.id.item_car_detail);
            shopHolder.car_check = convertView.findViewById(R.id.item_car_check);
            shopHolder.car_img = convertView.findViewById(R.id.item_car_img);
            shopHolder.car_name = convertView.findViewById(R.id.item_car_name);
            shopHolder.car_sure = convertView.findViewById(R.id.item_car_sure);
            shopHolder.car_color = convertView.findViewById(R.id.item_car_color);
            shopHolder.car_minus = convertView.findViewById(R.id.item_car_minus);
            shopHolder.car_edi = convertView.findViewById(R.id.item_car_edi);
            shopHolder.car_add = convertView.findViewById(R.id.item_car_add);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        /*不可编辑的*/
        shopHolder.car_edi.setFocusable(false);
        boolean choosed = list.isChoosed();
        if (choosed){
            shopHolder.car_check.setChecked(true);
        }else{
            shopHolder.car_check.setChecked(false);
        }

        if(list.getCarShopImg().equals("")){
            shopHolder.car_img.setImageResource(R.mipmap.shop_name_img2);
        }else {
            OkHttpUtils.picassoImage(list.getCarShopImg(),mActivity,shopHolder.car_img);
        }
        shopHolder.car_name.setText(list.getCarShopName());
        if(list.getCarShopType().equals("1")){
            shopHolder.car_sure.setText("￥"+list.getCarShopPrice());
        }else {
            shopHolder.car_sure.setText(list.getCarShopCredit()+"积分");
        }
        if(list.getCarShopColor().equals("null")){
            shopHolder.car_color.setText("无规格");
        }else {
            shopHolder.car_color.setText(list.getCarShopColor());
        }
        shopHolder.car_edi.setText(list.getCarShopNum()+"");

        /*详情*/
        shopHolder.car_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShopDetailActivity.class);
                intent.putExtra("shopName", list.getCarShopName());
                intent.putExtra("shopId", list.getCarItemId());
                mActivity.startActivity(intent);
            }
        });
        /*check选择*/
        shopHolder.car_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
            }
        });

        /*减法*/
        shopHolder.car_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, shopHolder.car_edi, shopHolder.car_check.isChecked());//暴露删减接口
                minNum = shopHolder.car_edi.getText().toString().trim();
                updateMinus(list.getCarShopId(),1);
            }
        });

        /*加法*/
        shopHolder.car_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, shopHolder.car_edi, shopHolder.car_check.isChecked());//暴露增加接口
                addNum = shopHolder.car_edi.getText().toString().trim();
                updateMinus(list.getCarShopId(),2);
            }
        });

        /*删除时加点击事件  在加下面这句话*/
//        modifyCountInterface.childDelete(position);//删除 目前只是从item中移除
        return convertView;
    }
    /*购物车 减*/
    private void updateMinus(String cartId,int type) {
        /*type  1 减 2 加*/
        Map<String,String> map = new HashMap<>();
        if(type==1){
            map.put("num",minNum);
        }else if(type==2){
            map.put("num",addNum);
        }
        map.put("cartId",cartId);
        String json = new Gson().toJson(map);
        com.lzy.okhttputils.OkHttpUtils.post(Webcon.url + Webcon.shop_car_updata)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                String code = jsonObject.getString("code");
                                if(code.equals("0")){

                                }else {
                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(mActivity);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ShopGridHolder {
        LinearLayout car_detail;
        ImageView car_img;
        TextView car_name;
        TextView car_sure;
        TextView car_color;
        CheckBox car_check;
        TextView car_minus;
        EditText car_edi;
        TextView car_add;
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
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
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

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }

}
