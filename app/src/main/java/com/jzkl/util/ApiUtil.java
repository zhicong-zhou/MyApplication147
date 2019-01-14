package com.jzkl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtil {
    /**
     * 将img标签中的src进行二次包装
     * @param content 内容
     * @param replaceHttp 需要在src中加入的域名
     * @param size 需要在src中将文件名加上_size
     * @return
     */
    public static String repairContent(String content,String replaceHttp,int size){
        String patternStr="<img\\s*([^>]*)\\s*src=\\\"(.*?)\\\"\\s*([^>]*)>";
        Pattern pattern = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        String result = content;
        while(matcher.find()) {
            String src = matcher.group(2);
            String replaceSrc = "";
            if(src.lastIndexOf(".")>0){
                //if(size>0) replaceSrc = src.substring(0,src.lastIndexOf("."))+"_"+size+src.substring(src.lastIndexOf("."));

                replaceSrc = src.substring(0,src.lastIndexOf("."))+src.substring(src.lastIndexOf("."));

            }
            if(!src.startsWith("http://")&&!src.startsWith("https://")){
                replaceSrc = replaceHttp + replaceSrc;
            }
            result = result.replaceAll(src,replaceSrc);
        }
//        Log.e("content ==",content);
//        Log.e("result == ",result);
        return result;
    }
}
