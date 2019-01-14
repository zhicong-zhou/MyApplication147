package com.jzkl.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntegrterDetailActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;

    @BindView(R.id.item_integr_detail_name)
    TextView itemIntegrDetailName;
    @BindView(R.id.item_integr_detail_jifen)
    TextView itemIntegrDetailJifen;
    @BindView(R.id.item_integr_detail_price)
    TextView itemIntegrDetailPrice;
    @BindView(R.id.item_integr_detail_img)
    ImageView itemIntegrDetailImg;
    @BindView(R.id.item_integr_detail_jifentxt)
    TextView itemIntegrDetailJifentxt;
    @BindView(R.id.item_integr_detail_but)
    LinearLayout detailBut;
    @BindView(R.id.item_integr_detail_time)
    TextView itemIntegrDetailTime;
    @BindView(R.id.item_integr_detail_content)
    TextView itemIntegrDetailContent;

    String intergraName;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
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
        return R.layout.activity_integrter_detail;
    }

    @Override
    protected void initData() {
        intergraName = getIntent().getStringExtra("intergraName");
        commonTitle.setText(intergraName);

    }

    @OnClick({R.id.common_back, R.id.item_integr_detail_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*兑换*/
            case R.id.item_integr_detail_but:
                popWin();
                break;

        }
    }

    private void popWin() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
//        layoutDialog.setTitle("提示");
//        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntegrterDetailActivity.this,IntergrterSuccessActivity.class);
                intent.putExtra("intergraName",intergraName);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        /*取消*/
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
