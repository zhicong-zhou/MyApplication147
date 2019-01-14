package com.jzkl.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 引导页
 * */
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.point1)
    ImageView point1;
    @BindView(R.id.point2)
    ImageView point2;
//    @BindView(R.id.point3)
//    ImageView point3;
    @BindView(R.id.point4)
    ImageView point4;
    @BindView(R.id.ll)
    LinearLayout ll;
    private boolean isFirstIn;
    private List<View> views;
    private ViewPagerAdapter vpAdapter;
    private int[] ids = { R.id.point1, R.id.point2,R.id.point4 };
    private ImageView[] dots;
    private TextView start_btn;
    SharedPreferences sp;


    @Override
    protected void initView() {
        initViews();
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.guide_one, null));
        views.add(inflater.inflate(R.layout.guide_two, null));
//        views.add(inflater.inflate(R.layout.guide_three, null));
        views.add(inflater.inflate(R.layout.guide_four, null));

        vpAdapter = new ViewPagerAdapter(views, this);
        viewpager.setAdapter(vpAdapter);

        viewpager.setOnPageChangeListener(this);
    }

    private void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = findViewById(ids[i]);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {

        sp = getSharedPreferences("zzc", MODE_PRIVATE);
        isFirstIn = sp.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            goGuide();
        }else{
            goHome();
        }
    }

    private void goHome() {
        Intent intent = new Intent(GuideActivity.this,WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void goGuide() {
        /*第三個*/
        start_btn = views.get(2).findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
                /*保存状态*/
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isFirstIn", false);
                editor.apply();
            }
        });

    }

    /** 当页面被滑动时候调用 */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    /** 当前新的页面被选中时调用 */
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < ids.length; i++) {
//        for (int i = 0; i < 2; i++) {
            if (position == i) {
                // 亮点
                dots[i].setImageResource(R.mipmap.login_point_selected);
            } else {
                // 暗点
                dots[i].setImageResource(R.mipmap.login_point);
            }
        }
    }

    /** 滑动状态改变的时候 */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
