package com.jzkl.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.ChangeDateForm;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 贷款
 * */
public class HomeDaiKActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.home_dk_ll)
    LinearLayout homeDkLl;
    @BindView(R.id.home_dkinfo_name)
    EditText dkName;
    @BindView(R.id.home_dkinfo_tel)
    EditText dkTel;
    @BindView(R.id.home_dkinfo_age)
    EditText dkAge;
    @BindView(R.id.home_dkinfo_hj1)
    ImageView homeDkinfoHj1;
    @BindView(R.id.home_credit_jiguan_ll)
    LinearLayout jiguanLl1;
    @BindView(R.id.home_credit_jiguan_ll2)
    LinearLayout jiguanLl2;
    @BindView(R.id.home_dkinfo_hj2)
    ImageView homeDkinfoHj2;
    @BindView(R.id.home_dkinfo_bx1)
    ImageView homeDkinfoBx1;
    @BindView(R.id.home_credit_baox_ll)
    LinearLayout baoxLl;
    @BindView(R.id.home_credit_baox_ll2)
    LinearLayout baoxLl2;

    @BindView(R.id.home_dkinfo_bx2)
    ImageView homeDkinfoBx2;
    @BindView(R.id.home_dkinfo_fc1)
    ImageView homeDkinfoFc1;
    @BindView(R.id.home_credit_fangc_ll)
    LinearLayout fangcLl;
    @BindView(R.id.home_credit_fangc_ll2)
    LinearLayout fangcLl2;

    @BindView(R.id.home_dkinfo_fc2)
    ImageView homeDkinfoFc2;
    @BindView(R.id.home_dkinfo_fcred1)
    ImageView homeDkinfoFcred1;
    @BindView(R.id.home_credit_fangc2_ll)
    LinearLayout fangc2Ll;
    @BindView(R.id.home_credit_fangc2_ll2)
    LinearLayout fangc2Ll2;

    @BindView(R.id.home_dkinfo_fcred2)
    ImageView homeDkinfoFcred2;
    @BindView(R.id.home_dkinfo_gjj1)
    ImageView homeDkinfoGjj1;
    @BindView(R.id.home_credit_ggj_ll)
    LinearLayout gjjLl;
    @BindView(R.id.home_credit_ggj_ll2)
    LinearLayout ggjLl2;

    @BindView(R.id.home_dkinfo_gjj2)
    ImageView homeDkinfoGjj2;
    @BindView(R.id.home_dkinfo_hy1)
    ImageView homeDkinfoHy1;
    @BindView(R.id.home_credit_hy_ll)
    LinearLayout hyLl;
    @BindView(R.id.home_credit_hy_ll2)
    LinearLayout hyLl2;

    @BindView(R.id.home_dkinfo_hy2)
    ImageView homeDkinfoHy2;
    @BindView(R.id.home_dkinfo_yyzz1)
    ImageView homeDkinfoYyzz1;
    @BindView(R.id.home_credit_yyzz_ll)
    LinearLayout yyzzLl;
    @BindView(R.id.home_credit_yyzz_ll2)
    LinearLayout yyzzLl2;
    @BindView(R.id.home_dkinfo_yyzz2)
    ImageView homeDkinfoYyzz2;
    @BindView(R.id.home_dkinfo_yyzz_edi)
    TextView yyzzEdi;
    @BindView(R.id.home_dkinfo_yyzz_edi2)
    EditText yyzzEdi2;
    @BindView(R.id.home_dkinfo_car1)
    ImageView homeDkinfoCar1;
    @BindView(R.id.home_credit_car_ll)
    LinearLayout carLl;
    @BindView(R.id.home_credit_car_ll2)
    LinearLayout carLl2;
    @BindView(R.id.home_dkinfo_car2)
    ImageView homeDkinfoCar2;
    @BindView(R.id.home_dkinfo_car21)
    ImageView homeDkinfoCar21;
    @BindView(R.id.home_credit_car2_ll)
    LinearLayout car2Ll;
    @BindView(R.id.home_credit_car2_ll2)
    LinearLayout car2Ll2;

    @BindView(R.id.home_dkinfo_car22)
    ImageView homeDkinfoCar22;
    @BindView(R.id.home_dkinfo_but)
    Button homeDkinfoBut;

    private int flag1 = 0;
    private int flag2 = 0;
    private int flag3 = 0;
    private int flag4 = 0;
    private int flag5 = 0;
    private int flag6 = 0;
    private int flag7 = 0;
    private int flag8 = 0;
    private int flag9 = 0;

    CustomDialog customDialog;
    String userinfo, token, dName, dTel, dAge, dyyzz1, dyyzz2,timeData;
    private TimePickerView pvTime;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_dai_k;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @OnClick({R.id.common_back, R.id.home_credit_jiguan_ll2, R.id.home_credit_jiguan_ll, R.id.home_credit_baox_ll, R.id.home_credit_baox_ll2, R.id.home_credit_fangc_ll, R.id.home_credit_fangc_ll2,
            R.id.home_credit_fangc2_ll, R.id.home_credit_fangc2_ll2, R.id.home_credit_ggj_ll, R.id.home_credit_ggj_ll2, R.id.home_credit_hy_ll, R.id.home_credit_hy_ll2, R.id.home_credit_yyzz_ll,
            R.id.home_credit_yyzz_ll2, R.id.home_credit_car_ll, R.id.home_credit_car_ll2, R.id.home_credit_car2_ll, R.id.home_credit_car2_ll2, R.id.home_dkinfo_but, R.id.home_dkinfo_yyzz_edi})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*有限期*/
            case R.id.home_dkinfo_yyzz_edi:
                dateSelector();
                break;
            /*户籍 是*/
            case R.id.home_credit_jiguan_ll2:
                flag1 = 1;
                homeDkinfoHj2.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoHj1.setImageResource(R.mipmap.home_credit_select);
                break;
            /*否*/
            case R.id.home_credit_jiguan_ll:
                flag1 = 2;
                homeDkinfoHj1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoHj2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*保险 是*/
            case R.id.home_credit_baox_ll2:
                flag2 = 1;
                homeDkinfoBx1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoBx2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*否*/
            case R.id.home_credit_baox_ll:
                flag2 = 2;
                homeDkinfoBx1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoBx2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*房产 按揭 否*/
            case R.id.home_credit_fangc_ll:
                flag3 = 2;
                homeDkinfoFc1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoFc2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*房产 按揭 是*/
            case R.id.home_credit_fangc_ll2:
                flag3 = 1;
                homeDkinfoFc1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoFc2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*房产 大红本 否*/
            case R.id.home_credit_fangc2_ll:
                flag4 = 2;
                homeDkinfoFcred1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoFcred2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*房产 大红本 是*/
            case R.id.home_credit_fangc2_ll2:
                flag4 = 1;
                homeDkinfoFcred1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoFcred2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*住房公积金 否*/
            case R.id.home_credit_ggj_ll:
                flag5 = 2;
                homeDkinfoGjj1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoGjj2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*住房公积金 是*/
            case R.id.home_credit_ggj_ll2:
                flag5 = 1;
                homeDkinfoGjj1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoGjj2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*婚否 否*/
            case R.id.home_credit_hy_ll:
                flag6 = 2;
                homeDkinfoHy1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoHy2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*婚否 是*/
            case R.id.home_credit_hy_ll2:
                flag6 = 1;
                homeDkinfoHy1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoHy2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*营业执照 否*/
            case R.id.home_credit_yyzz_ll:
                flag7 = 2;
                homeDkinfoYyzz1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoYyzz2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*营业执照 是*/
            case R.id.home_credit_yyzz_ll2:
                flag7 = 1;
                homeDkinfoYyzz1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoYyzz2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*车辆状况 拥有 否*/
            case R.id.home_credit_car_ll:
                flag8 = 2;
                homeDkinfoCar1.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoCar2.setImageResource(R.mipmap.home_credit_select);
                break;
            /*车辆状况 拥有 是*/
            case R.id.home_credit_car_ll2:
                flag8 = 1;
                homeDkinfoCar1.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoCar2.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*车辆状况 按揭 否*/
            case R.id.home_credit_car2_ll:
                flag9 = 2;
                homeDkinfoCar21.setImageResource(R.mipmap.home_credit_selected);
                homeDkinfoCar22.setImageResource(R.mipmap.home_credit_select);
                break;
            /*车辆状况 按揭 是*/
            case R.id.home_credit_car2_ll2:
                flag9 = 1;
                homeDkinfoCar21.setImageResource(R.mipmap.home_credit_select);
                homeDkinfoCar22.setImageResource(R.mipmap.home_credit_selected);
                break;
            /*确认*/
            case R.id.home_dkinfo_but:
//                Intent intent = new Intent(HomeDaiKActivity.this,DkListApplyActivity.class);
//                startActivity(intent);
                dName = dkName.getText().toString().trim();
                dTel = dkTel.getText().toString().trim();
                dAge = dkAge.getText().toString().trim();
                dyyzz1 = yyzzEdi.getText().toString().trim();
                dyyzz2 = yyzzEdi2.getText().toString().trim();
                if (dName.equals("")) {
                    ToastUtil.show("姓名不能为空");
                    return;
                } else if (dTel.equals("")) {
                    ToastUtil.show("手机号不能为空");
                    return;
                } else if (dTel.length()!=11) {
                    ToastUtil.show("手机号不合法");
                    return;
                } else if (dAge.equals("")) {
                    ToastUtil.show("年龄不能为空");
                    return;
                } else if (flag1 == 0) {
                    ToastUtil.show("请选择籍贯");
                    return;
                } else if (flag2 == 0) {
                    ToastUtil.show("请选择保险");
                    return;
                } else if (flag3 == 0) {
                    ToastUtil.show("请选择房产（按揭）");
                    return;
                } else if (flag4 == 0) {
                    ToastUtil.show("请选择房产（大红本）");
                    return;
                } else if (flag5 == 0) {
                    ToastUtil.show("请选择住房公积金");
                    return;
                } else if (flag6 == 0) {
                    ToastUtil.show("请选择婚否");
                    return;
                } else if (flag7 == 0) {
                    ToastUtil.show("请选择营业执照");
                    return;
                } else if (dyyzz1.equals("请选择")) {
                    ToastUtil.show("请选择有效期");
                    return;
                } else if (dyyzz2.equals("")) {
                    ToastUtil.show("请填写工作单位");
                    return;
                } else if (flag8 == 0) {
                    ToastUtil.show("请选择车辆状况（拥有）");
                    return;
                }
                subApply();
                break;
        }
    }

    /*提交*/
    private void subApply() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("age", dAge);
        map.put("name", dName);
        map.put("phone", dTel);
        map.put("businessLicense", flag7 + "," + dyyzz1);//营业执照 ,
        map.put("businessLicenseNum", dyyzz2);//营业执照编号
        map.put("house", flag3 + "," + flag4);// 房产
        map.put("houseFund", String.valueOf(flag5));//住房公积金 ,
        map.put("insurance", String.valueOf(flag2));//保险
        map.put("marriage", String.valueOf(flag6));// 婚姻状态
        map.put("register", String.valueOf(flag1));//户籍
        map.put("vehicleState", flag8 + "," + flag9);//车辆状态

        Log.e("ADAD",dAge + dName + dTel + flag7 +flag3 + flag4 + flag5 + flag2);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.home_credit_apply)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    ToastUtil.show("提交成功");
                                    finish();
                                } else {
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
                        customDialog.dismiss();
                        ToastUtil.show("" + e);
                    }
                });
    }

    @Override
    protected void initData() {
        commonTitle.setText("贷款申请审核");
        getUser();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if (userinfo != "") {
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

    //时间选择器
    public void dateSelector() {
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                yyzzEdi.setText(ChangeDateForm.getTime2(date));
                timeData = ChangeDateForm.getTime(date);

            }
        }).setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView ivCancel = v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.DKGRAY)
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

}
