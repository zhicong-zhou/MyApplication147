package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.FeedBackButton;
import com.jzkl.Bean.ImageUrl;
import com.jzkl.R;
import com.jzkl.adapter.FeedBackButtonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.FullyGridLayoutManager;
import com.jzkl.util.GridImageAdapter;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyTextWatcher;
import com.jzkl.util.PictureUtils;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 故障
 * */
public class SaleProblemActivity extends BaseActivity {

    String serviceName;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.sale_problem_ll)
    LinearLayout saleProblemLl;
    @BindView(R.id.sale_problem_list)
    MyGridView myGridView;
    @BindView(R.id.sale_problem_edi)
    EditText saleProblemEdi;
    @BindView(R.id.sale_problem_txtnum)
    TextView txtNum;
    @BindView(R.id.sale_problem_imgnum)
    TextView mProblemnum;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.sale_problem_but)
    Button saleProblemBut;
    @BindView(R.id.sale_problem_scr)
    ScrollView saleProblemScr;
    @BindView(R.id.sale_problem_tel)
    LinearLayout saleProblemTel;


    List<FeedBackButton> list;
    private String[] feedbackBut = {"可以更好", "不稳定", "随便说说", "速度慢", "不好用", "性能差", "其他"};
    FeedBackButtonAdapter buttonAdapter;

    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private int maxSelectNum = 4;
    private int aspect_ratio_x = 3, aspect_ratio_y = 4;
    private int themeId;
    private int chooseMode = PictureMimeType.ofImage();
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token,problemContent,edi;
    String  url1,url2,url3,url4,imageUrlaa;
    String img01,img02,img03,img04;
    private int time=3;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            if (time==0){
                customDialog.dismiss();
                handler.removeMessages(0);
            }
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };

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
        return R.layout.activity_sale_problem;
    }


    @OnClick({R.id.common_back, R.id.common_text, R.id.sale_problem_but, R.id.sale_problem_tel})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*反馈记录*/
            case R.id.common_text:
                Intent intent = new Intent(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            /*提交*/
            case R.id.sale_problem_but:
                customDialog = new CustomDialog(SaleProblemActivity.this,R.style.CustomDialog);
                customDialog.show();
//                /*倒计时加 圈 不然连续点会报错*/
                handler.sendEmptyMessageDelayed(0,1000);

                edi = saleProblemEdi.getText().toString().trim();
                if(edi.equals("")){
                    ToastUtil.show("反馈意见不能为空");
                }
                StringBuilder stringBuilder = new StringBuilder();
                if(selectList.size()>0){
                    for (int i = 0; i <selectList.size() ; i++) {
                        String imageUrl = selectList.get(i).getPath();
                        stringBuilder.append(imageUrl);
                        stringBuilder.append(",");
                    }
                    String ss = stringBuilder.substring(0, stringBuilder.length() - 1);
                    String[] result = ss.split(",");
                    if(result!=null){
                        if(selectList.size()==4){
                            img01 = result[0];
                            img02 = result[1];
                            img03 = result[2];
                            img04 = result[3];
                            getHeadImg(img01);
                        }else if(selectList.size()==3){
                            img01 = result[0];
                            img02 = result[1];
                            img03 = result[2];
                            getHeadImg(img01);
                        }else if(selectList.size()==2){
                            img01 = result[0];
                            img02 = result[1];
                            getHeadImg(img01);
                        }else if(selectList.size()==1){
                            img01 = result[0];
                            getHeadImg(img01);
                        }
                    }
                }
                break;
            /*客服电话*/
            case R.id.sale_problem_tel:
                /*ACTION_DIAL 跳转 拨号页面 ACTION_CALL直接拨打*/
                AndPermission.with(this).permission(Manifest.permission.CALL_PHONE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent2 = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "035714554252");
                        intent2.setData(data);
                        startActivity(intent2);
                    }
                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        Uri packageURI = Uri.parse("package:" + getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        Toast.makeText(SaleProblemActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();
                break;
        }
    }

    /*提交反馈*/
    private void subProble(String listUrl) {
        Map<String,Object> map = new HashMap<>();
        map.put("descript",edi);
        map.put("pictures",listUrl);
        map.put("type",problemContent);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.problem_sub)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject  =new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if(code == 0){
                                    ToastUtil.show("上传成功");
                                    finish();
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
                    }
                });
    }

    @Override
    protected void initData() {

        serviceName = getIntent().getStringExtra("serviceName");
        commonTitle.setText(serviceName);

        commonText.setVisibility(View.VISIBLE);
        commonText.setText("反馈记录");

        getUser();
        /*获取gird反馈信息*/
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        getData();
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeedBackButton feedBackButton = (FeedBackButton) myGridView.getAdapter().getItem(position);
                problemContent = feedBackButton.getFeedbackBConten();
                buttonAdapter.setSelection(position);
            }
        });

        /*显示输入框的数字*/
        saleProblemEdi.addTextChangedListener(new MyTextWatcher(saleProblemEdi, txtNum, 200, this));

        /*==========选择照片============默认样式黑色==============================*/
        themeId = R.style.picture_default_style;
        FullyGridLayoutManager manager = new FullyGridLayoutManager(SaleProblemActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new GridImageAdapter(SaleProblemActivity.this, onAddPicClickListener, 0);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        /*item预览的点击事件*/
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    PictureUtils.preViewImg(SaleProblemActivity.this, themeId, position, selectList);
                }
            }
        });
    }
    /*获取gird反馈信息*/
    private void getData() {
        OkHttpUtils.post(Webcon.url + Webcon.problem_info)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            list = new ArrayList<>();
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONArray array = jsonObject1.getJSONArray("feedbackTypes");
                                    for (int i = 0; i < array.length(); i++) {
                                        FeedBackButton feedBackButton = new FeedBackButton();
                                        JSONObject jsonObject11 = (JSONObject) array.get(i);
                                        String value = jsonObject11.getString("value");

                                        feedBackButton.setFeedbackBConten(value);
                                        list.add(feedBackButton);
                                    }

                                    buttonAdapter = new FeedBackButtonAdapter(SaleProblemActivity.this, list);
                                    myGridView.setAdapter(buttonAdapter);

                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    problemContent = jsonObject0.getString("value");

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
                    }
                });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            /*检查是否有权限*/
            if (PictureUtils.isHasPer(SaleProblemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                enterCamera();
            } else {
                PictureUtils.requestPer(SaleProblemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    };

    private void enterCamera() {
        // true ? PictureConfig.MULTIPLE : PictureConfig.SINGLE
        // PictureConfig.CHOOSE_REQUEST
        PictureUtils.createPicture(this, selectList, chooseMode, themeId,
                1, maxSelectNum, 4, true ? PictureConfig.MULTIPLE : PictureConfig.SINGLE,
                PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectList = PictureUtils.handleResult(requestCode, resultCode, data);
        if(selectList!=null){
            mProblemnum.setText(selectList.size()+"/4");
            for (LocalMedia media : selectList) {
                Log.i("图片-----》", media.getPath());
                String url = media.getPath();
            }
            adapter.setList(selectList);
            adapter.notifyDataSetChanged();
        }
    }

    /*上传服务器*/
    private void getHeadImg(final String imageUrl) {
        File img=new File(imageUrl);
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", img)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    url1 = jsonObject.getString("url");
                                    ToastUtil.show("成功");
                                    if(img02!=null){
                                        getHeadImg2(img02);
                                    }else {
                                        subProble(url1);
                                    }
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
                    }
                });
    }

    /*上传服务器*/
    private void getHeadImg2(final String imageUrl) {
        File img=new File(imageUrl);
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", img)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    url2 = jsonObject.getString("url");
                                    ToastUtil.show("成功2");
                                    if(img03!=null){
                                        getHeadImg3(img03);
                                    }else {
                                        subProble(url1 + "," + url2);
                                    }
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
                    }
                });
    }

    /*上传服务器*/
    private void getHeadImg3(final String imageUrl) {
        File img=new File(imageUrl);
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", img)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    url3 = jsonObject.getString("url");
                                    if(img04!=null){
                                        getHeadImg4(img04);
                                    }else {
                                        subProble(url1 + "," + url2 +"," + url3);
                                    }
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
                    }
                });
    }

    /*上传服务器*/
    private void getHeadImg4(final String imageUrl) {
        File img=new File(imageUrl);
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", img)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    url4 = jsonObject.getString("url");
                                    if(selectList.size()==4){
                                        subProble(url1 + "," + url2 +"," + url3 +"," + url4);
                                    }
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
                    }
                });
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}
