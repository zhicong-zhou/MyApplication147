package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/*
* 优惠卷
* */
public class MyYouHuiActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.my_youh_list)
    ListView myYouhList;

    List<PubBean> list;
    private String [] yhjName = {"汉拿山自助烤肉","汉拿山自助烤肉2","汉拿山自助烤肉3","汉拿山自助烤肉4","汉拿山自助烤肉5","汉拿山自助烤肉6"};
    private int [] yhjImg = {R.mipmap.shop_bg_img,R.mipmap.shop_bg_img,R.mipmap.shop_bg_img,R.mipmap.shop_bg_img,R.mipmap.shop_bg_img,R.mipmap.shop_bg_img};
    private String [] yhjReduce = {"每满50减5元","每满50减12元","每满50减8元","每满50减5元","每满50减25元","每满50减15元"};
    private String [] yhjAddress = {"高新区科技产业路","高新区科技产业路2","高新区科技产业路3","高新区科技产业路4","高新区科技产业路5","高新区科技产业路6"};
    private String [] yhjClass = {"烤肉","火锅","拌饭","火锅","烤肉","火锅"};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token;
    CommonAdapter mAdapter;

    @Override
    protected void initView() {

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
        return R.layout.activity_my_you_hui;
    }

    @Override
    protected void initData() {
        commonTitle.setText("电子劵");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUser();
        getData();

        myYouhList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PubBean pubBean = (PubBean) myYouhList.getAdapter().getItem(position);
                Intent intent = new Intent(MyYouHuiActivity.this,DianziJuanOrderActivity.class);
                intent.putExtra("yhjId",pubBean.getId());
                intent.putExtra("yhjName",pubBean.getTitle());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        Map<String,String> map = new HashMap<>();
        map.put("limit","10");
        map.put("page","1");

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.My_reduction_list)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            list = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                JSONObject pages = jsonObject.getJSONObject("pages");
                                if (code == 0) {
                                    list = PubBean.getJsonArr(pages, "list");
                                    myYouhList.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                            MyYouHuiActivity.this, list, R.layout.item_yhj_detail) {
                                        @Override
                                        public void convert(final ViewHolder holder, final PubBean pubBean) {
                                            holder.setText(R.id.item_yhj_name, pubBean.getTitle());
                                            holder.setText(R.id.item_yhj_addr, pubBean.getAddress());
                                            holder.setText(R.id.item_yhj_reduce, pubBean.getRule()+":" + pubBean.getDescript());
                                            holder.setText(R.id.item_yhj_class, pubBean.getType());
                                            ImageView imageView = holder.getView(R.id.item_yhj_img);
                                            Glide.with(MyYouHuiActivity.this).load(pubBean.getLogoUrl()).into(imageView);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.show(e+"");
                        customDialog.dismiss();
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

}
