package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.CarShop;
import com.jzkl.Bean.SolicitudeName;
import com.jzkl.R;
import com.jzkl.adapter.CarListAdapter;
import com.jzkl.adapter.SolicitudeGridAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 *关怀
 * */
public class SolicitudeActivity extends BaseActivity implements SolicitudeGridAdapter.CheckInterface{

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.solicitude_img)
    ImageView solicitudeImg;
    @BindView(R.id.solicitude_name)
    TextView solicitudeName;
    @BindView(R.id.solicitude_content)
    TextView solicitudeContent;
    @BindView(R.id.solicitude_girdview)
    MyGridView sGirdview;
    @BindView(R.id.solicitude_but)
    Button solicitudeBut;

    @BindView(R.id.solicitude_ll)
    RelativeLayout solicitude_ll;

    @BindView(R.id.solicitude_info)
    LinearLayout solicitude_info;
    @BindView(R.id.solicitude_info_num)
    TextView infoNum;


    SolicitudeGridAdapter adapter;
    List<SolicitudeName> list;
    private String[] sName = {"张三", "李四", "王五", "赵六", "张麻子", "昆四"};
    private int[] sImg = {R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo,token;

    @Override
    protected void initView() {

    }

    @OnClick({R.id.common_back,R.id.solicitude_but,R.id.solicitude_ll,R.id.solicitude_info})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*充值*/
            case R.id.solicitude_info:
                Intent intent1 = new Intent(this,AgentRechargeActivity.class);
                intent1.putExtra("rechargeType","0");
                startActivity(intent1);
                break;
                /*送祝福*/
            case R.id.solicitude_but:
                lementOnder();
                break;
                /*列表*/
            case R.id.solicitude_ll:
                Intent intent = new Intent(this,SolicitudeListActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_solicitude;
    }

    @Override
    protected void initData() {
        String messageNum = getIntent().getStringExtra("messageNum");
        infoNum.setText(messageNum);
        commonTitle.setText("关怀");
        getUser();
        /*用户生日列表*/
        getData();
    }

    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        OkHttpUtils.post(Webcon.url + Webcon.birthUserList)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                String msg = jsonObject1.getString("msg");
                                if(code==0){
                                    JSONArray array = jsonObject1.getJSONArray("users");
                                    for (int i = 0; i <array.length() ; i++) {
                                        SolicitudeName solicitudeName = new SolicitudeName();

                                        JSONObject jsonObject11 = (JSONObject) array.get(i);
                                        String userId = jsonObject11.getString("userId");
                                        String headimgUrl = jsonObject11.getString("headimgUrl");
                                        String name = jsonObject11.getString("name");
                                        String mobile = jsonObject11.getString("mobile");

                                        solicitudeName.setsId(userId);
                                        solicitudeName.setsName(name);
                                        solicitudeName.setsImg(headimgUrl);
                                        solicitudeName.setsTel(mobile);

                                        list.add(solicitudeName);
                                    }
                                }else {
//                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter = new SolicitudeGridAdapter(SolicitudeActivity.this,list);
                            adapter.setCheckInterface(SolicitudeActivity.this);
                            sGirdview.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                        ToastUtil.show(""+e);
                    }
                });
    }
    //发送 生日祝福
    private void subBlessing(String messagePerson) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        Map<String,String> map = new HashMap<>();
        map.put("messageTempalteId","0");
        map.put("mobile",messagePerson);

        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.birthday_blessing)
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
                                    ToastUtil.show("发送成功");
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
                        customDialog.dismiss();
                        ToastUtil.show(""+e);
                    }
                });
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

    private void lementOnder() {
        //选中的需要提交的商品清单
        List<SolicitudeName> newCar = new ArrayList();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            SolicitudeName group = list.get(i);
            if (group.isChoosed()) {
                newCar.add(group);
                //用户id
                str.append(list.get(i).getsTel());
                str.append(",");
            }
        }
        if(str.length()>1){
            String messagePerson = str.substring(0, str.length() - 1);
            //发送 生日祝福
            subBlessing(messagePerson);
        }else {
            ToastUtil.show("请选择祝福对象");
        }
//        Intent intent2 = new Intent(this,SolicitudeHappyActivity.class);
//        startActivity(intent2);
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (SolicitudeName group : list) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        list.get(position).setChoosed(isChecked);
        if (isAllCheck())
//            carCheck.setChecked(true);
//        else
//            carCheck.setChecked(false);
        adapter.notifyDataSetChanged();
    }
}
