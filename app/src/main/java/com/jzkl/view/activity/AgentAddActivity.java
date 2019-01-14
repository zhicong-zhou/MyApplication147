package com.jzkl.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.util.ChangeDateForm;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 更多 新增代理（商户）
 * */
public class AgentAddActivity extends BaseActivity {

    String addStatus;

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.agent_all_time)
    TextView agentAllTime;
    @BindView(R.id.agent_all_peple)
    TextView agentAllPeple;
    @BindView(R.id.agent_all_list)
    MyListView agentList;
    @BindView(R.id.agent_all_timell)
    LinearLayout agentTimell;
    @BindView(R.id.agent_list_no)
    LinearLayout agent_list_no;


    //时间控件
    private TimePickerView pvTime;
//    List<AgentAll> list;
    private String [] agentName = {"张三","李四","旺旺","张三","李四","旺旺","旺旺"};
    private String [] agentStatus = {"已实名","未实名","已实名","未实名","已实名","未实名","未实名"};
    private int [] agentImg = {R.mipmap.my_head,R.mipmap.my_head,R.mipmap.my_head,R.mipmap.my_head,R.mipmap.my_head,R.mipmap.my_head,R.mipmap.my_head};
    protected ImmersionBar mImmersionBar;

    List<PubBean> list = new ArrayList<>();
    CustomDialog customDialog;
    CommonAdapter mAdapter;
    String userinfo,token,timeData;

    @Override
    protected void initView() {

    }

    @OnClick({R.id.common_back,R.id.agent_all_timell})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*选择时间*/
            case R.id.agent_all_timell:
                dateSelector();
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
        return R.layout.activity_agent_add;
    }

    @Override
    protected void initData() {
        addStatus = getIntent().getStringExtra("addStatus");
        getUser();

        Calendar calendar = Calendar.getInstance();
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
//        timeData = year + "-" + month;
//        agentAllTime.setText(year + "年" + month + "月");
        timeData = "";
        agentAllTime.setText("全部");

        /*0  是新增代理  1 是新增商户*/
        if (addStatus.equals("0")) {
            commonTitle.setText("全部代理");
            getData("agency",timeData);

        } else if (addStatus.equals("1")) {
            getData("merchant",timeData);
            commonTitle.setText("全部商户");
        }
        /*打电话*/
//        agentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                /*ACTION_DIAL 跳转 拨号页面 ACTION_CALL直接拨打*/
//                AndPermission.with(AgentAddActivity.this).permission(Manifest.permission.CALL_PHONE).callback(new PermissionListener() {
//                    @Override
//                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
//                        PubBean pubBean = (PubBean) agentList.getAdapter().getItem(position);
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        Uri data = Uri.parse("tel:"+ pubBean.getMobile());
//                        intent.setData(data);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
////                        Uri packageURI = Uri.parse("package:" + getPackageName());
////                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                        startActivity(intent);
//                        Toast.makeText(AgentAddActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
//                    }
//                }).start();
//            }
//        });
    }

    private void getData(String type,String timeData) {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("date", timeData);
        map.put("type",type);
        map.put("limit","1000");
        map.put("page","1");
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.incomeMore)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (list.size() > 0) {
                            list.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                JSONObject page = jsonObject.getJSONObject("page");
                                if (code == 0) {
                                    list = PubBean.getJsonArr(page, "records");
                                    if(list.size()==0){
                                        agent_list_no.setVisibility(View.VISIBLE);
                                        agentList.setVisibility(View.GONE);
                                    }else {
                                        agent_list_no.setVisibility(View.GONE);
                                        agentList.setVisibility(View.VISIBLE);

                                        /*0  是新增代理  1 是新增商户*/
                                        if (addStatus.equals("0")) {
                                            agentAllPeple.setText("新增" + list.size() + "名代理");
                                        } else if (addStatus.equals("1")) {
                                            agentAllPeple.setText("实名" + list.size() + "人");
                                        }
                                        agentList.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                                AgentAddActivity.this, list, R.layout.item_agent_list) {
                                            @Override
                                            public void convert(final ViewHolder holder, final PubBean pubBean) {
                                                holder.setText(R.id.item_agent_name, pubBean.getName());
                                                holder.setText(R.id.item_agent_status, pubBean.getRealnameStr());
                                                holder.setText(R.id.item_agent_level, pubBean.getLevelApp());

                                                ImageView imageView = holder.getView(R.id.item_agent_img);
                                                if(pubBean.getHeadimgUrl()=="null"){
                                                    imageView.setImageResource(R.mipmap.user_head);
                                                }else {
                                                    com.jzkl.util.OkHttpUtils.picassoImage(pubBean.getHeadimgUrl(),AgentAddActivity.this,imageView);
//                                                    Glide.with(AgentAddActivity.this).load(pubBean.getHeadimgUrl()).into(imageView);
                                                }
                                            }
                                        });
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

    //时间选择器
    public void dateSelector() {
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                agentAllTime.setText(ChangeDateForm.getTime(date));

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

                        if (addStatus.equals("0")) {
                            getData("agency",timeData);
                        } else if (addStatus.equals("1")) {
                            getData("merchant",timeData);
                        }

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
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.DKGRAY)
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
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
