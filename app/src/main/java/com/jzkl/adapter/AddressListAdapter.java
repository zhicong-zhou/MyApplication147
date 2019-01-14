package com.jzkl.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.google.gson.Gson;
import com.jzkl.Bean.Address;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.AddressActivity;
import com.jzkl.view.activity.AddressEidtorActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class AddressListAdapter extends BaseAdapter {

    private Activity  mActivity;
    private List<Address> mList;
    ShopGridHolder shopHolder;
    String userinfo, token,isDefault;
    CustomDialog customDialog;
    private int mSelection=-1;
    private Resources resources;

    public AddressListAdapter(Activity  mContext, List<Address> mList) {
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
        final Address list = (Address) getItem(position);

        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        shopHolder = null;

        if (convertView == null) {
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_address_list, null);
            shopHolder.addre_name = convertView.findViewById(R.id.item_addre_name);
            shopHolder.addre_tel = convertView.findViewById(R.id.item_addre_tel);
            shopHolder.addre_area = convertView.findViewById(R.id.item_addre_area);
            shopHolder.addre_moren = convertView.findViewById(R.id.item_addre_moren);
            shopHolder.addre_check = convertView.findViewById(R.id.item_addre_check);
            shopHolder.addre_edt = convertView.findViewById(R.id.item_addre_edt);
            shopHolder.addre_del = convertView.findViewById(R.id.item_addre_del);
            convertView.setTag(shopHolder);

        } else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        shopHolder.addre_name.setText(list.getAddressName());
        shopHolder.addre_tel.setText(list.getAddressTel());
        shopHolder.addre_area.setText(list.getAddressAear());

        /*编辑*/
        shopHolder.addre_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AddressEidtorActivity.class);
                intent.putExtra("addrId",list.getAddressId());
                intent.putExtra("addrName",list.getAddressName());
                intent.putExtra("addrTel",list.getAddressTel());
                intent.putExtra("addrAera",list.getAddressAear());
                intent.putExtra("addrAddress",list.getAddressAddress());
                intent.putExtra("addrAddressDefault",list.getAddressisDefault());
                intent.putExtra("addressStauts","1");
                mActivity.startActivity(intent);
            }
        });

        /*删除*/
        shopHolder.addre_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poWin(position,list.getAddressId());
            }
        });

        if(list.getAddressisDefault().equals("1")){
            shopHolder.addre_check.setImageResource(R.mipmap.pay_selected);
        }else {
            shopHolder.addre_check.setImageResource(R.mipmap.pay_select);
        }

        shopHolder.addre_moren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upData(position,list.getAddressAear(),list.getAddressAddress(),list.getAddressName(),list.getAddressTel(),list.getAddressId());
            }
        });
        return convertView;
    }

    /*默认修改*/
    private void upData(final int position, final String aera, final String addressDe, final String addrName, final String addrTel, final String addrId) {
        Map<String,String> map = new HashMap<>();

        String url = Webcon.url + Webcon.address_eid;
        map.put("address",aera);
        map.put("detail",addressDe);
        map.put("isDefault","1");
        map.put("name",addrName);
        map.put("phone",addrTel);
        map.put("addressId",addrId);

        String json = new Gson().toJson(map);

        OkHttpUtils.post(url)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                if(code==0){
                                    ToastUtil.show("成功");
                                    EventBus.getDefault().post(new EventsWIFI("isDefault"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                    }
                });
    }
    private void poWin(final int position, final String addressId) {

        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(mActivity, R.style.AlertDialog);
//        layoutDialog.setTitle("提示");
//        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        /*获取屏幕宽高*/

        dialogContent.setText("确定删除吗？");
        dialogBtnConfirm.setText("确定");
        dialogBtnCancel.setText("取消");

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getDel(position,addressId);
            }
        });
        /*取消*/
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    /*删除地址*/
    private void getDel(final int position,String addressId) {
        customDialog = new CustomDialog(mActivity,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("addressId",addressId);

        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url+Webcon.address_del)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if(code == 0){
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    ToastUtil.show("删除失败");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                        customDialog.dismiss();
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
        TextView addre_name;
        TextView addre_tel;
        TextView addre_area;
        LinearLayout addre_moren;
        ImageView addre_check;
        LinearLayout addre_edt;
        LinearLayout addre_del;
    }
}
