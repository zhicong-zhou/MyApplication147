package com.jzkl.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator .
 */

public class ChangeDateForm {
    //转时间格式的
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getTime(Date date) {//可根据需要自行截取数据显示 HH:mm:ss  yyyy年-MM月-dd
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
    public static String getTime2(Date date) {//可根据需要自行截取数据显示 HH:mm:ss  yyyy年-MM月-dd
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
