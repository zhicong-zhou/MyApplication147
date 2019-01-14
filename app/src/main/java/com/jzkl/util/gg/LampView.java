package com.jzkl.util.gg;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.jzkl.R;
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
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhihong on 2016/7/3.
 * 跑马灯
 */
public class LampView extends FrameLayout {

    @BindView(R.id.lamp_view)
    VerticalLampView lampView;
    private Context mContext = null;
    private List<LampBean> list=new ArrayList<>();

    public LampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        setView();
    }

    private void setView() {
        inflate(getContext(), R.layout.lamp_layout, this);
        ButterKnife.bind(this, this);

        initData();
        initView();
    }

    private void initView() {
        lampView.setData((ArrayList<LampBean>) list);
        lampView.setTextSize(12);
        lampView.setTimer(2000);
        lampView.start();

    }
    private void initData() {
        /*首页公告*/
        Map<String, String> map = new HashMap<>();
        map.put("type", "3");
        String json =  new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.home_banner)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            list = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            JSONArray array = jsonObject.getJSONArray("icon");
                            if(code.equals("0")){
                                for (int i = 0; i <array.length() ; i++) {
                                    LampBean bean = new LampBean();
                                    JSONObject jsonObject1 = (JSONObject) array.get(i);
                                    String  zhaiyao = jsonObject1.getString("descript");
                                    bean.title = zhaiyao;
                                    list.add(bean);
                                }
                                initView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });

//        LampBean bean = new LampBean();
//        bean.title = "用户 135****8058 成功贷款";
//        bean.info = "50万元";
//        list.add(bean);
//
//        bean = new LampBean();
//        bean.title = "用户 136****3639 成功贷款";
//        bean.info = "10万元";
//        list.add(bean);
//
//        bean = new LampBean();
//        bean.title = "用户 158****8348 成功贷款";
//        bean.info = "12万元";
//        list.add(bean);
//
//        bean = new LampBean();
//        bean.title = "用户 138****3781 成功贷款";
//        bean.info = "15万元";
//        list.add(bean);
//
//        bean = new LampBean();
//        bean.title = "用户 159****1348 成功贷款";
//        bean.info = "18万元";
//        list.add(bean);
    }
}
