package com.jzkl.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.CarShop;
import com.jzkl.R;
import com.jzkl.adapter.CarListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
* 购物车
* */
public class CarActivity extends BaseActivity implements CarListAdapter.CheckInterface,CarListAdapter.ModifyCountInterface {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.car_ll)
    LinearLayout carLl;
    @BindView(R.id.car_list)
    MyListView carList;
    @BindView(R.id.car_check)
    CheckBox carCheck;
    @BindView(R.id.car_tatil_price)
    TextView carTatilPrice;
    @BindView(R.id.car_but)
    TextView carBut;
    @BindView(R.id.car_tatil_ll)
    LinearLayout car_tatil_ll;

    @BindView(R.id.car_list_no)
    LinearLayout car_list_no;


    List<CarShop> list;
    boolean flag=false;
    CarListAdapter adapter;
    protected ImmersionBar mImmersionBar;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    CustomDialog customDialog;
    String userinfo,token,str;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
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
    }

    @OnClick({R.id.common_back,R.id.common_text,R.id.car_check,R.id.car_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*编辑*/
            case R.id.common_text:
                flag = !flag;
                if(flag){
                    commonText.setText("完成");
                    car_tatil_ll.setVisibility(View.INVISIBLE);
                    carBut.setText("删除所选");
                }else {
                    commonText.setText("编辑");
                    car_tatil_ll.setVisibility(View.VISIBLE);
                    carBut.setText("结算");
                }
                break;
                /*全选*/
            case R.id.car_check:
                if (list.size() != 0) {
                    if (carCheck.isChecked()) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setChoosed(true);
                        }
                        carBut.setBackgroundResource(R.drawable.shape_car_button);
                        adapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setChoosed(false);
                        }
                        carBut.setBackgroundResource(R.drawable.shape_car_button_gray);
                        adapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
                /*结算*/
            case R.id.car_but:
                if(carBut.getText().toString().equals("删除所选")){
                    if (totalCount == 0) {
                        ToastUtil.show("请选择要删除的商品");
                        return;
                    }
                    doDelete();
                }else {
                    if (totalCount == 0) {
                        ToastUtil.show("请选择要结算的商品");
                        return;
                    }
                    lementOnder();
                }
                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_car;
    }

    @Override
    protected void initData() {
        commonTitle.setText("购物车");
        commonText.setVisibility(View.VISIBLE);
        commonText.setText("编辑");

        getUser();

        getDate();
    }

    private void getDate() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.shop_cart_list)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    if(array.length()==0){
                                        car_list_no.setVisibility(View.VISIBLE);
                                        carList.setVisibility(View.GONE);
                                    }else {
                                        car_list_no.setVisibility(View.GONE);
                                        carList.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < array.length(); i++) {
                                            CarShop carShop = new CarShop();
                                            JSONObject jsonObject1 = (JSONObject) array.get(i);
                                            String cartid = jsonObject1.getString("id");
                                            String itemId = jsonObject1.getString("itemId");
                                            String title = jsonObject1.getString("title");
                                            String logoUrl = jsonObject1.getString("logoUrl");
                                            String price = jsonObject1.getString("price");
                                            String credit = jsonObject1.getString("credit");
                                            String type = jsonObject1.getString("type");
                                            int num = jsonObject1.getInt("num");
                                            String standardName = jsonObject1.getString("standardName");
                                            String standardId = jsonObject1.getString("standardId");

                                            carShop.setCarShopId(cartid);
                                            carShop.setCarItemId(itemId);
                                            carShop.setCarShopName(title);
                                            carShop.setCarShopPrice(Double.parseDouble(price));
                                            carShop.setCarShopImg(logoUrl);
                                            carShop.setCarShopNum(num);
                                            carShop.setCarShopCredit(credit);
                                            carShop.setCarShopType(type);
                                            carShop.setCarShopColor(standardName);
                                            carShop.setCarShopSizeId(standardId);
                                            list.add(carShop);
                                        }
                                    }
                                }else {
                                    ToastUtil.show(msg);
                                }
                                adapter = new CarListAdapter(CarActivity.this,list);
                                adapter.setCheckInterface(CarActivity.this);
                                adapter.setModifyCountInterface(CarActivity.this);
                                carList.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.show(""+e);
                    }
                });
    }

    /**
     * 结算订单、支付
     */
    private void lementOnder() {
        //选中的需要提交的商品清单
        List<CarShop> newCar = new ArrayList();
        for (CarShop bean:list ){
            boolean choosed = bean.isChoosed();
            if (choosed){
                String shoppingName = bean.getCarShopName();
                double price = bean.getCarShopPrice();
                String shoppingId = bean.getCarShopId();
                String shoppingSizeId = bean.getCarShopSizeId();
                String attribute = bean.getCarShopColor();
                String shopCredit = bean.getCarShopCredit();
                int count = bean.getCarShopNum();
                String shopImg = bean.getCarShopImg();
                String shopType = bean.getCarShopType();
                newCar.add(bean);
            }
        }
        Intent intent = new Intent(this,OrderSureActivity.class);
        intent.putExtra("carShopDetail",new Gson().toJson(newCar));
        intent.putExtra("shopBuy","1");
        intent.putExtra("totalPrice",str);
        startActivity(intent);
        //跳转到支付界面
    }
    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        list.get(position).setChoosed(isChecked);
        if (isAllCheck()){
            carCheck.setChecked(true);
            carBut.setBackgroundResource(R.drawable.shape_car_button);
        }
        else{
            carBut.setBackgroundResource(R.drawable.shape_car_button_gray);
            carCheck.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (CarShop group : list) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < list.size(); i++) {
            CarShop shoppingCartBean = list.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.getCarShopPrice() * shoppingCartBean.getCarShopNum();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat();// 构造方法的字符格式这里如果小数不足2位,会以0补足.
        String price_num = decimalFormat.format(totalPrice);// format 返回的是字符串
        str = price_num;
        carTatilPrice.setText(str);
//        tvSettlement.setText("结算(" + totalCount + ")");
    }

    /**
     * 增加
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        CarShop shoppingCartBean = list.get(position);
        int currentCount = shoppingCartBean.getCarShopNum();
        currentCount++;
        shoppingCartBean.setCarShopNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        adapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        CarShop shoppingCartBean = list.get(position);
        int currentCount = shoppingCartBean.getCarShopNum();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        shoppingCartBean.setCarShopNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        adapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 删除的item暂时没用
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删除按钮操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    @SuppressLint("NewApi")
    private void doDelete() {
        List<CarShop> toBeDeleteGroups = new ArrayList<CarShop>(); //待删除的组元素
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            CarShop group = list.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
                //当循环到最后一个的时候 就不添加逗号,
                str.append(list.get(i).getCarShopId());
                str.append(",");
            }
        }
        String ss = str.substring(0, str.length() - 1);
        /*删除*/
        carDelete(ss,toBeDeleteGroups);
    }
    /*删除*/
    private void carDelete(String cartId, final List<CarShop> toBeDeleteGroups) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("cartIds",cartId);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.shop_car_deleta)
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
                                    list.removeAll(toBeDeleteGroups);
                                    adapter.notifyDataSetChanged();
                                    statistics();
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
        if(userinfo!=""){
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
}
