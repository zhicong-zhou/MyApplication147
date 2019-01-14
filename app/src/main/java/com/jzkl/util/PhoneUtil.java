package com.jzkl.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.jzkl.Bean.SolicituList;

import java.util.ArrayList;
import java.util.List;

public class PhoneUtil {
    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public PhoneUtil(Context context){
        this.context = context;
    }

    //获取所有联系人
    public List<SolicituList> getPhone(){
        List<SolicituList> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while (cursor.moveToNext()){
            SolicituList phoneDto = new SolicituList(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(NUM)));
            phoneDto.setmSTel(cursor.getString(cursor.getColumnIndex(NUM)));
            phoneDto.setmSName(cursor.getString(cursor.getColumnIndex(NAME)));
            phoneDto.setmSRenz("null");
            phoneDto.setmSHead("null");
            phoneDtos.add(phoneDto);
        }
        return phoneDtos;
    }
}
