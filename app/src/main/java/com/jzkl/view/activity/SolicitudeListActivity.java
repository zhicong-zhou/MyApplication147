package com.jzkl.view.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.CarShop;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.Bean.EventsZf;
import com.jzkl.Bean.SolicituList;
import com.jzkl.Bean.SolicitudeName;
import com.jzkl.R;
import com.jzkl.adapter.SolicitudeGridAdapter;
import com.jzkl.adapter.SolicitudeListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.fragment.IncomeFragment;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 关怀的全部用户
 * */
public class SolicitudeListActivity extends BaseActivity implements SolicitudeListAdapter.CheckInterface {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.solicitude_list_ll)
    LinearLayout solicitudeListLl;
    @BindView(R.id.solicitude_list)
    MyListView solicitudeList;
    @BindView(R.id.solicitude_list2)
    MyListView solicitude_list2;

    @BindView(R.id.solicitude_list_but)
    Button solicitudeListBut;

    @BindView(R.id.solicitude_contacts_list)
    TextView contacts_list;


    boolean checjFlag=false;
    SolicitudeListAdapter adapter;
    List<SolicituList> list;
    private int [] mSHead = {R.mipmap.shop_name_img1,R.mipmap.shop_name_img1,R.mipmap.shop_name_img1,R.mipmap.shop_name_img1,R.mipmap.shop_name_img1,R.mipmap.shop_name_img1};
    private String [] mSName = {"张三","李四","旺旺","张三","李四","旺旺"};
    private String [] mSRenz = {"已实名","未实名","已实名","已实名","未实名","已实名"};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo,token,contactsSelect;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_solicitude_list;
    }

    @OnClick({R.id.common_back,R.id.solicitude_list_but,R.id.common_text,R.id.solicitude_contacts_list})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*全选*/
            case R.id.common_text:
                checjFlag = !checjFlag;
                if (list.size() != 0) {
                    if (checjFlag) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setChoosed(true);
                        }
                        adapter.notifyDataSetChanged();
                        commonText.setText("反选");
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setChoosed(false);
                        }
                        adapter.notifyDataSetChanged();
                        commonText.setText("全选");
                    }
                }
                break;
                /*送上祝福*/
            case R.id.solicitude_list_but:
                lementOnder2();
                break;
                /*联系人列表*/
            case R.id.solicitude_contacts_list:
                AndPermission.with(SolicitudeListActivity.this).permission(Manifest.permission.READ_CONTACTS).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(SolicitudeListActivity.this,ContactsActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Toast.makeText(SolicitudeListActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();

                break;
        }
    }

    /*邀请推广切换*/
//    @Subscribe
//    public void  onEvent(EventsWIFI event){
//        contactsSelect =event.getWifiStatus();
//        if(!contactsSelect.equals("")){
//            getContactsAll();
//        }
//    }

    @Override
    protected void initData() {
//        EventBus.getDefault().register(this);
        commonTitle.setText("全部联系人");
        commonText.setVisibility(View.VISIBLE);
        commonText.setText("全选");

        getUser();
        getData();
    }

    private void getContactsAll() {
        try {
            list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(contactsSelect);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                String headimgUrl = json.getString("mSHead");
                String name = json.getString("mSName");
                String mobile = json.getString("mSTel");
                String mSRenz = json.getString("mSRenz");
                boolean flag = json.has("mSRenzJb");
                String mSRenzJb = null;
                if(flag){
                    mSRenzJb = json.getString("mSRenzJb");
                }
                SolicituList solicituList = new SolicituList(name,mobile);
                solicituList.setmSHead(headimgUrl);
                solicituList.setmSName(name);
                solicituList.setmSTel(mobile);
                solicituList.setmSRenz(mSRenz);
                solicituList.setmSRenzJb(mSRenzJb);

                list.add(solicituList);
            }
            adapter = new SolicitudeListAdapter(SolicitudeListActivity.this,list);
            adapter.setCheckInterface(SolicitudeListActivity.this);
            solicitude_list2.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*全部客户列表*/
    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("date","");
        map.put("type","all");
        map.put("limit","1000");
        map.put("page","1");
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.birthday_blessing_list)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                if(code==0){
                                    JSONObject jsonObject11 = jsonObject1.getJSONObject("page");
                                    JSONArray array = jsonObject11.getJSONArray("records");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject json = (JSONObject) array.get(i);
                                        String name = json.getString("name");
                                        String mobile = json.getString("mobile");
                                        String headimgUrl = json.getString("headimgUrl");
                                        String createTime = json.getString("createTime");
                                        String merchantStr = json.getString("merchantStr");
                                        String levelStr = json.getString("levelStr");
                                        String realnameStr = json.getString("realnameStr");
                                        String levelApp = json.getString("levelApp");

                                        SolicituList solicituList = new SolicituList(name,mobile);
                                        solicituList.setmSHead(headimgUrl);
                                        solicituList.setmSName(name);
                                        solicituList.setmSTel(mobile);
                                        solicituList.setmSRenz(realnameStr);
                                        solicituList.setmSRenzJb(levelApp);

                                        list.add(solicituList);
                                    }
                                }else {
                                    ToastUtil.show("暂无数据");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter = new SolicitudeListAdapter(SolicitudeListActivity.this,list);
                            adapter.setCheckInterface(SolicitudeListActivity.this);
                            solicitudeList.setAdapter(adapter);
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

    /**
     * 结算订单、支付
     */
    private void lementOnder2() {
        //选中的需要提交的商品清单
        List<SolicituList> newCar2 = new ArrayList();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            SolicituList group = list.get(i);
            if (group.isChoosed()) {
                newCar2.add(group);
                //用户id
                str.append(list.get(i).getmSTel());
                str.append(",");
            }
        }
        if(str.length()>1){
            String zhufu = new Gson().toJson(newCar2);
            EventBus.getDefault().post(new EventsZf(zhufu));
            finish();
//            String messagePerson = str.substring(0, str.length() - 1);
//            Intent intent = new Intent(this,SolicitudeBlessingActivity.class);
//            intent.putExtra("messagePerson",messagePerson);
//            startActivity(intent);
        }else {
            ToastUtil.show("请选择祝福对象");
        }
    }
    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    public void checkGroup(int position, boolean isChecked) {
        list.get(position).setChoosed(isChecked);
        if (isAllCheck())
            checjFlag = false;
        else
            checjFlag = true;
        adapter.notifyDataSetChanged();
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (SolicituList group : list) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

}
