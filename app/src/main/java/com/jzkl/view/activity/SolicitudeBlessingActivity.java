package com.jzkl.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.FeedBackButton;
import com.jzkl.R;
import com.jzkl.adapter.BlessingGridAdapter;
import com.jzkl.adapter.BlessingListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyListView;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 送上祝福
 * */
public class SolicitudeBlessingActivity extends BaseActivity {


    @BindView(R.id.recharge_blessing_back)
    TextView blessingBack;
    @BindView(R.id.recharge_blessing_title)
    TextView blessingTitle;
    @BindView(R.id.recharge_blessing_rl)
    RelativeLayout rechargeBlessingRl;
    @BindView(R.id.recharge_blessing_head)
    RelativeLayout rechargeBlessingHead;
    @BindView(R.id.solic_blessing_img01)
    ImageView solicBlessingImg01;
    @BindView(R.id.solic_blessing_rl01)
    RelativeLayout solicBlessingRl01;
    @BindView(R.id.solic_blessing_img02)
    ImageView solicBlessingImg02;
    @BindView(R.id.solic_blessing_rl02)
    RelativeLayout solicBlessingRl02;
    @BindView(R.id.solic_blessing_img03)
    ImageView solicBlessingImg03;
    @BindView(R.id.solic_blessing_rl03)
    RelativeLayout solicBlessingRl03;
    @BindView(R.id.solic_blessing_but)
    Button solicBlessingBut;

    CustomDialog customDialog;
    protected ImmersionBar mImmersionBar;
    private String blessing_first[] = {"企业祝福","个人祝福"};
    private String blessing_second[] = {"节假日","天气类","祝福类"};
    private int blessing_three[] = {R.string.blessing_content1,R.string.blessing_content2,R.string.blessing_content3,R.string.blessing_content4
            ,R.string.blessing_content5,R.string.blessing_content6};
    List<FeedBackButton> list;
    String userinfo,token,firstContent,secondContent,threeContent,threeId ;
    String messagePerson;
    MyListView blessing_list3;
    BlessingListAdapter adapter3;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_solicitude_blessing;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(false, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @OnClick({R.id.recharge_blessing_back, R.id.solic_blessing_rl01, R.id.solic_blessing_rl02, R.id.solic_blessing_rl03, R.id.solic_blessing_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.recharge_blessing_back:
                finish();
                break;
            /*第一个 公司类*/
            case R.id.solic_blessing_rl01:
                popFirst(1);
                break;
            /*第二个 节日类*/
            case R.id.solic_blessing_rl02:
                popFirst(2);
                break;
            /*第三个 祝福语*/
            case R.id.solic_blessing_rl03:
                popFirst(3);
                break;
            /*确认*/
            case R.id.solic_blessing_but:
                if(threeContent == null){
                    ToastUtil.show("请选择祝福语");
                    return;
                }
                subBlessing();
                break;
        }
    }

    /*人群 和 类型 弹框*/
    private void popFirst(int type) {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_blessing_first, null);
        ImageView blessing_close = dialogView.findViewById(R.id.pop_blessing_close);
        TextView blessing_title = dialogView.findViewById(R.id.pop_blessing_title);
        LinearLayout grid_ll1 = dialogView.findViewById(R.id.grid_ll1);
        LinearLayout grid_ll2 = dialogView.findViewById(R.id.grid_ll2);
        ScrollView grid_ll3 = dialogView.findViewById(R.id.grid_ll3);
        final MyGridView blessing_grid = dialogView.findViewById(R.id.pop_blessing_grid);
        final MyGridView blessing_grid2 = dialogView.findViewById(R.id.pop_blessing_grid2);
        blessing_list3 = dialogView.findViewById(R.id.pop_blessing_list3);
        if(type == 1){
            blessing_title.setText("选择客户人群");
            grid_ll1.setVisibility(View.VISIBLE);
            grid_ll2.setVisibility(View.GONE);
            grid_ll3.setVisibility(View.GONE);
        }else if(type == 2){
            blessing_title.setText("选择祝福类型");
            grid_ll1.setVisibility(View.GONE);
            grid_ll2.setVisibility(View.VISIBLE);
            grid_ll3.setVisibility(View.GONE);
        }else if(type == 3){
            blessing_title.setText("选择祝福语");
            grid_ll1.setVisibility(View.GONE);
            grid_ll2.setVisibility(View.GONE);
            grid_ll3.setVisibility(View.VISIBLE);
        }
        Button blessing_sure = dialogView.findViewById(R.id.pop_blessing_sure);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this, R.style.AlertDialog);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.8;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹窗在底部*/
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        list = new ArrayList<>();
        final BlessingGridAdapter adapter = new BlessingGridAdapter(SolicitudeBlessingActivity.this,list);
        /*type 1 人群 2 类型*/
        if(type == 1){
            getData1();
            blessing_grid.setAdapter(adapter);
            blessing_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setSelection(position);
                    FeedBackButton backButton = (FeedBackButton) blessing_grid.getAdapter().getItem(position);
                    firstContent = backButton.getFeedbackBConten();
                }
            });
        }else if(type == 2){
            getData2();
            blessing_grid2.setAdapter(adapter);
            blessing_grid2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setSelection(position);
                    FeedBackButton backButton = (FeedBackButton) blessing_grid2.getAdapter().getItem(position);
                    secondContent = backButton.getFeedbackBConten();
                }
            });
        }else if(type == 3){
            getData3();
            blessing_list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter3.setSelection(position);
                    FeedBackButton backButton = (FeedBackButton) blessing_list3.getAdapter().getItem(position);
                    threeContent = backButton.getFeedbackBConten();
                    threeId = backButton.getFeedbackBId();
                }
            });
        }
        /*关闭*/
        blessing_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /*确认*/
        blessing_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getData3() {
        Map<String,String> map = new HashMap<>();
        map.put("type","1");
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.incomeMore_info_mb)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
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
                                        list.add(feedBackButton);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    threeContent = jsonObject0.getString("discript");
                                    threeId = jsonObject0.getString("id");
                                    adapter3 = new BlessingListAdapter(SolicitudeBlessingActivity.this,list);
                                    blessing_list3.setAdapter(adapter3);
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
                        ToastUtil.show("" + e);
                    }
                });
//        for (int i = 0; i < blessing_three.length; i++) {
//            FeedBackButton feedBackButton = new FeedBackButton();
//            feedBackButton.setFeedbackBConten(getResources().getString(blessing_three[i]));
//            list.add(feedBackButton);
//        }
    }
    /*送心意*/
    private void subBlessing() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("messageTempalteId",threeId);
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
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                String code = jsonObject.getString("code");
                                if(code.equals("0")){
                                    finish();
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

    private void getData2() {
        for (int i = 0; i <blessing_second.length ; i++) {
            FeedBackButton feedBackButton = new FeedBackButton();
            feedBackButton.setFeedbackBConten(blessing_second[i]);
            list.add(feedBackButton);
        }
    }
    private void getData1() {
        for (int i = 0; i <blessing_first.length ; i++) {
            FeedBackButton feedBackButton = new FeedBackButton();
            feedBackButton.setFeedbackBConten(blessing_first[i]);
            list.add(feedBackButton);
        }
    }

    @Override
    protected void initData() {
        messagePerson = getIntent().getStringExtra("messagePerson");
        blessingTitle.setText("送上祝福");
        getUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
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
