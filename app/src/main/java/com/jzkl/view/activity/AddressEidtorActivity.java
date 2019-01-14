package com.jzkl.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.city.JsonBean;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.GetJsonDataUtil;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.SlideButton;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class AddressEidtorActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.addr_eid_name)
    EditText addrEidName;
    @BindView(R.id.addr_eid_tel)
    EditText addrEidTel;
    @BindView(R.id.addr_eid_areatxt)
    TextView addrEidAreatxt;
    @BindView(R.id.addr_eid_area)
    TextView addrEidArea;
    @BindView(R.id.addr_eid_address)
    EditText addrEidAddress;
    @BindView(R.id.addr_eid_set)
    SlideButton addrEidSet;
    @BindView(R.id.addr_eid_rl)
    RelativeLayout addr_eid_rl;


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    String addressStauts,provice,city,district;
    protected ImmersionBar mImmersionBar;
    String userinfo, token,addrName,addrTel,addrArea,addrDetail,isDefault,url;
    String addrAddressDefault,addrId;
    CustomDialog customDialog;

    @Override
    protected void initView() {
        initJsonData();
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_address_eidtor;
    }

    @Override
    protected void initData() {
        commonTitle.setText("收货地址");
        commonText.setVisibility(View.VISIBLE);

        getUser();
        addressStauts = getIntent().getStringExtra("addressStauts");
        if(addressStauts.equals("1")){
            String addressName = getIntent().getStringExtra("addrName");
            String addressTel = getIntent().getStringExtra("addrTel");
            addrId = getIntent().getStringExtra("addrId");
            String addrAera = getIntent().getStringExtra("addrAera");
            String addrAddress = getIntent().getStringExtra("addrAddress");
            isDefault = getIntent().getStringExtra("addrAddressDefault");

            addrEidName.setText(addressName);
            addrEidTel.setText(addressTel);
            addrEidArea.setText(addrAera);
            addrEidAddress.setText(addrAddress);

            if(isDefault.equals("1")){
                addrEidSet.setOpen(true);
            }else {
                addrEidSet.setOpen(false);
            }
        }
        /*开关默认是不选中*/
        isDefault = "2";

        /*开关*/
        addrEidSet.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
            @Override
            public void onButtonChange(SlideButton view, boolean isOpen) {
                if(isOpen){
                    isDefault = "1";
                }else {
                    isDefault = "2";
                }
            }
        });

    }

    @OnClick({R.id.common_back,R.id.addr_eid_rl,R.id.common_text})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*地区*/
            case R.id.addr_eid_rl:
                /*隐藏软键盘*/
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addrEidArea.getWindowToken(), 0);

                ShowPickerView();
                break;
                /*保存*/
            case R.id.common_text:
                addrName = addrEidName.getText().toString().trim();
                addrTel = addrEidTel.getText().toString().trim();
                addrArea = addrEidArea.getText().toString().trim();
                addrDetail = addrEidAddress.getText().toString().trim();

                if(addrName.equals("")){
                    ToastUtil.show("收人姓名不能为空");
                    return;
                }else if(addrTel.equals("")){
                    ToastUtil.show("收人手机号不能为空");
                    return;
                }else if(addrArea.equals("请选择所在地区")){
                    ToastUtil.show("请选择所在地区");
                    return;
                }else if(addrDetail.equals("")){
                    ToastUtil.show("收人地址不能为空");
                    return;
                }
                subSave();
                break;
        }
    }

    private void subSave() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        Map<String,String> map = new HashMap<>();
        /*addressStauts 0 是添加 1 是编辑*/
        if(addressStauts.equals("0")){
            url = Webcon.url + Webcon.address_add;
            map.put("address",addrArea);
            map.put("detail",addrDetail);
            map.put("isDefault",isDefault);
            map.put("name",addrName);
            map.put("phone",addrTel);
        }else if(addressStauts.equals("1")){
            url = Webcon.url + Webcon.address_eid;
            map.put("address",addrArea);
            map.put("detail",addrDetail);
            map.put("isDefault",isDefault);
            map.put("name",addrName);
            map.put("phone",addrTel);
            map.put("addressId",addrId);
        }
        String json = new Gson().toJson(map);

        OkHttpUtils.post(url)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                String msg = jsonObject1.getString("msg");
                                if(code==0){
                                    if(addressStauts.equals("0")){
                                        ToastUtil.show("添加成功");
                                    }else if(addressStauts.equals("1")){
                                        ToastUtil.show("修改成功");
                                    }
                                    finish();
                                }else {
                                    if(addressStauts.equals("0")){
                                        ToastUtil.show(msg);
                                    }else if(addressStauts.equals("1")){
                                        ToastUtil.show(msg);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                        ToastUtil.show(e+"");
                    }

                });
    }

    private void ShowPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);

                provice = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                district = options3Items.get(options1).get(options2).get(options3);

//                getDtatlist(3,0,1,1);
                addrEidArea.setText(tx);
                addrEidArea.setTextColor(Color.parseColor("#333333"));
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#333333"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#333333"))//取消按钮文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
    }

    /*解析本地  地区分类*/
    private void initJsonData() {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this,"provinces.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}
