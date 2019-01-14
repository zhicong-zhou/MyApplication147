package com.jzkl.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.text.method.ReplacementTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jzkl.Bean.BankList;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class BankAddListAdapter extends BaseAdapter {

    private Activity mContext;
    private List<BankList> mList;
    CustomDialog customDialog;
    String userinfo,token;

    public BankAddListAdapter(Activity mContext, List<BankList> mList){
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
        getUser();
        final BankList bankList = (BankList) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_bank_add_list,null);
            shopHolder.bank_bg  = convertView.findViewById(R.id.item_bank_add_list_bg);
            shopHolder.bank_img  = convertView.findViewById(R.id.item_bank_add_list_img);
            shopHolder.bank_name  = convertView.findViewById(R.id.item_bank_add_list_name);
            shopHolder.bank_class  = convertView.findViewById(R.id.item_bank_add_list_class);
            shopHolder.bank_card  = convertView.findViewById(R.id.item_bank_add_list_card);
            shopHolder.bank_but  = convertView.findViewById(R.id.item_bank_add_list_del);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
//        shopHolder.bank_bg.setBackgroundResource(bankList.getBankImg());
        String imgName = bankList.getBankCode();
        /*大写转小写*/
        String imgName2 = imgName.toLowerCase();
        //得到application对象
        ApplicationInfo appInfo = mContext.getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"drawable" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        if(appInfo.packageName!=null){
            int resID = mContext.getResources().getIdentifier(imgName2, "mipmap", appInfo.packageName);
            shopHolder.bank_img.setBackgroundResource(resID);
        }else {
            int resID = mContext.getResources().getIdentifier(imgName2, "mipmap", "ab");
            shopHolder.bank_img.setBackgroundResource(resID);
        }

        shopHolder.bank_name.setText(bankList.getBankName());
        shopHolder.bank_class.setText(bankList.getBankClass());
        shopHolder.bank_card.setText(bankList.getBankNo());

        shopHolder.bank_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopWin(position,bankList.getBankId());
            }
        });
        return convertView;
    }

    class InputCapLowerToUpper extends ReplacementTransformationMethod {
        @Override
        protected char[] getOriginal() {
            char[] lower = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
            return lower;
        }

        @Override
        protected char[] getReplacement() {
            char[] upper = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
            return upper;
        }
    }

    /*解绑*/
    private void PopWin(final int position, final String bankId) {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        dialogContent.setText("确定要解绑吗？");
        dialogBtnCancel.setText("取消");
        dialogBtnConfirm.setText("确定");
//        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
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
                getDel(position,bankId);
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
    /*解绑*/
    private void getDel(final int position, String bankId) {
        customDialog = new CustomDialog(mContext,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("bankcardid",bankId);

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_unbind)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    ToastUtil.show(msg);
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

    class ShopGridHolder{
        RelativeLayout bank_bg;
        ImageView bank_img;
        TextView bank_name;
        TextView bank_class;
        TextView bank_card;
        TextView bank_but;
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(mContext);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
