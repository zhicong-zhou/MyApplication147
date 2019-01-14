package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.Bean.EventsZf;
import com.jzkl.Bean.FeedBackButton;
import com.jzkl.Bean.SolicituList;
import com.jzkl.Bean.SolicitudeName;
import com.jzkl.R;
import com.jzkl.adapter.BlessingListAdapter;
import com.jzkl.adapter.SolicitudeGridAdapter;
import com.jzkl.adapter.SolicitudeGridAdapter1;
import com.jzkl.adapter.SolicitudeListAdapter1;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.HorizontalListView;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.fragment.IncomeFragment;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 *关怀
 * */
public class SolicitudeActivity1 extends BaseActivity implements SolicitudeListAdapter1.CheckInterface {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.solicitude_girdview)
    MyGridView sGirdview;
    @BindView(R.id.solicitude_but)
    Button solicitudeBut;
    @BindView(R.id.solicitude_info)
    LinearLayout solicitude_info;
    @BindView(R.id.solicitude_info_num)
    TextView infoNum;

    @BindView(R.id.solicitude_add_list)
    HorizontalListView solicitudeAddList;
    @BindView(R.id.solicitude_add)
    LinearLayout solicitudeAdd;
    @BindView(R.id.solicitude_qyzf_img)
    CheckBox zf01;
    @BindView(R.id.solicitude_grzf_img)
    CheckBox zf02;
    @BindView(R.id.solicitude_zf_img01)
    CheckBox zfClass01;
    @BindView(R.id.solicitude_zf_img02)
    CheckBox zfClass02;
    @BindView(R.id.solicitude_zf_img03)
    CheckBox zfClass03;
    @BindView(R.id.solicitude_say_list)
    MyListView sayList;
    @BindView(R.id.solicitude_sub_but)
    TextView subBut;
    @BindView(R.id.solic_scr)
    ScrollView scrollView;

    SolicitudeGridAdapter1 adapter;
    SolicitudeListAdapter1 adapterAll;
    BlessingListAdapter adapter3;
    List<SolicitudeName> list;
    List<SolicituList> listAll;
    List<FeedBackButton> listFeedback;
    private String[] sName = {"张三", "李四", "王五", "赵六", "张麻子", "昆四"};
    private int[] sImg = {R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img, R.mipmap.pop_shop_img};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token, threeContent, threeId, zhufu;

    @Override
    protected void initView() {

    }

    @OnClick({R.id.common_back, R.id.solicitude_but, R.id.solicitude_info, R.id.solicitude_qyzf_img, R.id.solicitude_grzf_img, R.id.solicitude_zf_img01, R.id.solicitude_zf_img02,
            R.id.solicitude_zf_img03, R.id.solicitude_add, R.id.solicitude_sub_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*充值*/
            case R.id.solicitude_info:
                Intent intent1 = new Intent(this, AgentRechargeActivity.class);
                intent1.putExtra("rechargeType", "0");
                startActivity(intent1);
                break;
            /*送祝福*/
            case R.id.solicitude_but:
                lementOnder();
                break;
            /*祝福人群 企业*/
            case R.id.solicitude_qyzf_img:
                zf01.setChecked(true);
                zf02.setChecked(false);
                break;
            /*祝福人群 个人*/
            case R.id.solicitude_grzf_img:
                zf01.setChecked(false);
                zf02.setChecked(true);
                break;
            /*祝福类型 节日*/
            case R.id.solicitude_zf_img01:
                zfClass01.setChecked(true);
                zfClass02.setChecked(false);
                zfClass03.setChecked(false);
                break;
            case R.id.solicitude_zf_img02:
                zfClass01.setChecked(false);
                zfClass02.setChecked(true);
                zfClass03.setChecked(false);
                break;
            case R.id.solicitude_zf_img03:
                zfClass01.setChecked(false);
                zfClass02.setChecked(false);
                zfClass03.setChecked(true);
                break;
            /*添加 联系人*/
            case R.id.solicitude_add:
                Intent intent = new Intent(this, SolicitudeListActivity.class);
                startActivity(intent);
                break;
            /*送祝福语 */
            case R.id.solicitude_sub_but:
                if (zf01.isChecked() == false && zf02.isChecked() == false) {
                    ToastUtil.show("请选择祝福人群");
                } else if (zfClass01.isChecked() == false && zfClass02.isChecked() == false && zfClass03.isChecked() == false) {
                    ToastUtil.show("请选择祝福类型");
                } else if (listAll == null) {
                    ToastUtil.show("请选择祝福对象");
                } else {
                    lementOnder2();
                }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_solicitude1;
    }

    /*选择了的联系人*/
    @Subscribe
    public void onEvent(EventsZf event) {
        zhufu = event.getZfSay();
        listAll = new ArrayList<>();
        getSelect();
        adapterAll = new SolicitudeListAdapter1(SolicitudeActivity1.this, listAll);
        adapterAll.setCheckInterface(SolicitudeActivity1.this);
        solicitudeAddList.setAdapter(adapterAll);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        String messageNum = getIntent().getStringExtra("messageNum");
        infoNum.setText(messageNum);
        commonTitle.setText("关怀");
        getUser();
        /*用户生日列表*/
        getData();
        /*祝福语*/
        getData3();
        sayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter3.setSelection(position);
                FeedBackButton backButton = (FeedBackButton) sayList.getAdapter().getItem(position);
                threeContent = backButton.getFeedbackBConten();
                threeId = backButton.getFeedbackBId();
            }
        });
        sayList.setFocusable(false);
    }

    /*用户生日列表*/
    private void getData() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        OkHttpUtils.post(Webcon.url + Webcon.birthUserList)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                String msg = jsonObject1.getString("msg");
                                if (code == 0) {
                                    JSONArray array = jsonObject1.getJSONArray("users");
                                    for (int i = 0; i < array.length(); i++) {
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
                                } else {
//                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter = new SolicitudeGridAdapter1(SolicitudeActivity1.this, list);
                            sGirdview.setAdapter(adapter);
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


    /*选择了的联系人*/
    private void getSelect() {
        try {
            JSONArray array = new JSONArray(zhufu);
            if (array.length() == 0) {
                solicitudeAddList.setVisibility(View.GONE);
            } else
                solicitudeAddList.setVisibility(View.VISIBLE);
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = (JSONObject) array.get(i);
                String name = json.getString("mSName");
                String mobile = json.getString("mSTel");
                String headimgUrl = json.getString("mSHead");
                String realnameStr = json.getString("mSRenz");
                boolean flag = json.has("mSRenzJb");
                String levelApp = "";
                if (flag) {
                    levelApp = json.getString("mSRenzJb");
                }
                SolicituList solicituList = new SolicituList(name, mobile);
                solicituList.setmSHead(headimgUrl);
                solicituList.setmSName(name);
                solicituList.setmSTel(mobile);
                solicituList.setmSRenz(realnameStr);
//                solicituList.setmSRenzJb(levelApp);
                listAll.add(solicituList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData3() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.incomeMore_info_mb)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                listFeedback = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i < array.length(); i++) {
                                        FeedBackButton feedBackButton = new FeedBackButton();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String id = jsonObject1.getString("id");
                                        String name = jsonObject1.getString("name");
                                        String discript = jsonObject1.getString("discript");
                                        feedBackButton.setFeedbackBId(id);
                                        feedBackButton.setFeedbackBName(name);
                                        feedBackButton.setFeedbackBConten(discript);
                                        listFeedback.add(feedBackButton);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    threeContent = jsonObject0.getString("discript");
                                    threeId = jsonObject0.getString("id");
                                    adapter3 = new BlessingListAdapter(SolicitudeActivity1.this, listFeedback);
                                    sayList.setAdapter(adapter3);
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
                        ToastUtil.show("" + e);
                    }
                });
    }

    //发送 生日祝福
    private void subBlessing(String messagePerson) {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("messageTempalteId", "0");
        map.put("mobile", messagePerson);

        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.birthday_blessing)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                String msg = jsonObject1.getString("msg");
                                if (code == 0) {
                                    ToastUtil.show("发送成功");
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

    /*送心意*/
    private void subBlessingAll(String messagePerson) {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("messageTempalteId", threeId);
        map.put("mobile", messagePerson);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.birthday_blessing)
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
                                String code = jsonObject.getString("code");
                                if (code.equals("0")) {
                                    finish();
                                    ToastUtil.show("发送成功");
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
            newCar.add(group);
            //用户id
            str.append(list.get(i).getsTel());
            str.append(",");
        }
        if (str.length() > 1) {
            String messagePerson = str.substring(0, str.length() - 1);
            //发送 生日祝福
            subBlessing(messagePerson);
        } else {
            ToastUtil.show("请选择祝福对象");
        }
//        Intent intent2 = new Intent(this,SolicitudeHappyActivity.class);
//        startActivity(intent2);
    }
    /*====================联系人=============================*/

    /**
     * 结算订单、支付
     */
    private void lementOnder2() {
        //选中的需要提交的商品清单
        List<SolicituList> newCar2 = new ArrayList();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < listAll.size(); i++) {
            SolicituList group = listAll.get(i);
            if (group.isChoosed()) {
                newCar2.add(group);
                //用户id
                str.append(listAll.get(i).getmSTel());
                str.append(",");
            }
        }
        if (str.length() > 1) {
            String messagePerson = str.substring(0, str.length() - 1);
            subBlessingAll(messagePerson);
        } else {
            ToastUtil.show("请选择祝福对象");
        }
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck1() {
        for (SolicituList group : listAll) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup1(int position, boolean isChecked) {
        listAll.get(position).setChoosed(isChecked);
        if (isAllCheck1())
            adapterAll.notifyDataSetChanged();
    }
}
