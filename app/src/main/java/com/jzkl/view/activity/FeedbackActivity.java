package com.jzkl.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.FeedBack;
import com.jzkl.R;
import com.jzkl.adapter.FeedBackListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class FeedbackActivity extends BaseActivity {
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.feedback_list)
    ListView feedbackList;

    List<FeedBack> list;
    private String [] feedbackTitle = {"不稳定","速度慢","不好用","不稳定","速度慢","不好用"};
    private String [] feedbackContent = {"问题描述1","问题描述12","问题描述13","问题描述14","问题描述15","问题描述16"};
    private String [] feedbackStatus = {"待处理","已完成","待处理","已完成","待处理","已完成"};
    private String [] feedbackTime = {"2018-04-14","2018-012-14","2018-08-14","2018-06-14","2018-05-14","2018-04-24"};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token;

    @Override
    protected void initView() {

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
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {

        commonTitle.setText("反馈记录");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
        getData();

    }

    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        OkHttpUtils.post(Webcon.url + Webcon.problem_list)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if(code==0){
                                    JSONArray array = jsonObject.getJSONArray("faults");
                                    for (int i = 0; i < array.length(); i++) {
                                        FeedBack feedBack = new FeedBack();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String type = jsonObject1.getString("type");
                                        String resultStr = jsonObject1.getString("resultStr");
                                        String descript = jsonObject1.getString("descript");
                                        String createTime = jsonObject1.getString("createTime");
                                        String dealDescript = jsonObject1.getString("dealDescript");
                                        JSONArray pictureArray = jsonObject1.getJSONArray("pictureArray");

                                        feedBack.setmFeedBackTitle(type);
                                        feedBack.setmFeedBackStatus(resultStr);
                                        feedBack.setmFeedBackContent(descript);
                                        feedBack.setmFeedBackTime(createTime);
                                        feedBack.setmFeedBackDescript(dealDescript);
                                        feedBack.setmList(pictureArray);

                                        list.add(feedBack);
                                    }
                                }else {
                                    ToastUtil.show("暂无列表");
                                }
                                FeedBackListAdapter adapter = new FeedBackListAdapter(FeedbackActivity.this,list);
                                feedbackList.setAdapter(adapter);
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
//        for (int i = 0; i < feedbackTitle.length; i++) {
//            FeedBack feedBack = new FeedBack();
//            feedBack.setmFeedBackTitle(feedbackTitle[i]);
//            feedBack.setmFeedBackContent(feedbackContent[i]);
//            feedBack.setmFeedBackStatus(feedbackStatus[i]);
//            feedBack.setmFeedBackTime(feedbackTime[i]);
//            list.add(feedBack);
//        }
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
