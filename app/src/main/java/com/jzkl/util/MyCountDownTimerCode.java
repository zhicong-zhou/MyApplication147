package com.jzkl.util;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.jzkl.R;

/**
 * 倒计时60秒
 */
//复写倒计时
public class MyCountDownTimerCode extends CountDownTimer {
    private Button button;

    public MyCountDownTimerCode(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    //计时过程
    @Override
    public void onTick(long l) {
            /*点击后变成灰色*/
        button.setTextColor(Color.parseColor("#CBCCCC"));
        button.setBackgroundResource(R.drawable.shape_yanz_button_tran);
        //防止计时过程中重复点击
        button.setClickable(false);
        button.setText(l / 1000 + "s");
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        button.setText("重新获取");

            /*变回原来颜色*/
        button.setTextColor(Color.parseColor("#D07D80"));
        button.setBackgroundResource(R.drawable.shape_yanz_button_tran);
        //设置可点击
        button.setClickable(true);
    }
}