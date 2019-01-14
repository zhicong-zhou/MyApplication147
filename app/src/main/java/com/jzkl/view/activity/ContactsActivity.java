package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.EventsAddressInfo;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.Bean.EventsZf;
import com.jzkl.Bean.SolicituList;
import com.jzkl.R;
import com.jzkl.adapter.SolicitudeListAdapter;
import com.jzkl.util.PhoneUtil;
import com.jzkl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 联系人
 * */
public class ContactsActivity extends BaseActivity implements SolicitudeListAdapter.CheckInterface{

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.contacts_list)
    ListView contactsList;
    @BindView(R.id.contacts__submit)
    Button submit;

//    List<SolicituList> list;
    List<SolicituList> cList2;
    SolicitudeListAdapter myAdapter;
    boolean checjFlag=false;
    String mobile;

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
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contacts;
    }

    @OnClick({R.id.common_back,R.id.contacts__submit})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
            case R.id.contacts__submit:
                lementOnder2();
                break;
        }
    }
    @Override
    protected void initData() {
        commonTitle.setText("联系人");

        PhoneUtil phoneUtil = new PhoneUtil(this);
        List<SolicituList> phoneDtos = phoneUtil.getPhone();
        /*中文拼音排序*/
        Comparator comp = new Mycomparator();
        /*去重复*/
        cList2 = removeDuplicateData2(phoneDtos);
        Collections.sort(cList2, comp);

        myAdapter = new SolicitudeListAdapter(this,cList2);
        myAdapter.setCheckInterface(ContactsActivity.this);
        contactsList.setAdapter(myAdapter);
    }
    /*去重复*/
    private List<SolicituList> removeDuplicateData2(List<SolicituList> cList) {
        List<SolicituList> cList2 = new ArrayList<>();
        for (int i = 0; i < cList.size(); i++) {
            if (!cList2.contains(cList.get(i)))
                cList2.add(cList.get(i));
        }
        return cList2;
    }
    //通讯录按中文拼音排序，英文放在最后面
    private class Mycomparator implements Comparator {
        public int compare(Object o1, Object o2) {
            SolicituList c1 = (SolicituList) o1;
            SolicituList c2 = (SolicituList) o2;
            Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
            return cmp.compare(c1.getmSName(), c2.getmSName());
        }
    }

    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        cList2.get(position).setChoosed(isChecked);
        if (isAllCheck())
            checjFlag = false;
        else
            checjFlag = true;
        myAdapter.notifyDataSetChanged();
    }
    /**
     * 结算订单、支付
     */
    private void lementOnder2() {
        //选中的需要提交的商品清单
        List<SolicituList> newCar2 = new ArrayList();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cList2.size(); i++) {
            SolicituList group = cList2.get(i);
            if (group.isChoosed()) {
                String contactsName = group.getmSName();
                String contactsTel = group.getmSTel();
                newCar2.add(group);
                //用户id
                str.append(cList2.get(i).getmSTel());
                str.append(",");
            }
        }
        if(str.length()>1){
            Intent intent = new Intent(this,SolicitudeActivity1.class);
//            intent.putExtra("contactsSelect",new Gson().toJson(newCar2));
            EventBus.getDefault().post(new EventsZf(new Gson().toJson(newCar2)));
            startActivity(intent);
            finish();
        }else {
            ToastUtil.show("请选择祝福对象");
        }
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {
        for (SolicituList group : cList2) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
}
