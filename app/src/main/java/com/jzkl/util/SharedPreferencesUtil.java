package com.jzkl.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/6.
 */

public class SharedPreferencesUtil {
    /*存token*/
    public void setToken(Context context, String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", str);
        editor.commit();
    }
    /*获取存token*/
    public String getToken(Context context){
        SharedPreferences myPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return  myPreferences.getString("token","");
    }

    /*存token*/
    public void setUserInfo(Context context, String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userinfo", str);
        editor.commit();
    }
    /*获取存token*/
    public String getUserInfo(Context context){
        SharedPreferences myPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return  myPreferences.getString("userinfo","");
    }

    /*存手势开关状态*/
    public void setLogin(Context context, String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginStatus", str);
        editor.commit();
    }
    /*获取*/
    public String getLogin(Context context){
        SharedPreferences myPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return  myPreferences.getString("loginStatus","");
    }

    /*存语音播报*/
    public void setTTs(Context context, String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ttsStatus", str);
        editor.commit();
    }
    /*获取语音播报*/
    public String getTTs(Context context){
        SharedPreferences myPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return  myPreferences.getString("ttsStatus","");
    }

    /*存手势密码*/
    public void setLoginPwd(Context context, String str){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginPwd", str);
        editor.commit();
    }
    /*获取*/
    public String getLoginPwd(Context context){
        SharedPreferences myPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        return  myPreferences.getString("loginPwd","");
    }
}
