package com.jzkl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.jzkl.util.img.ImageFileCache;
import com.jzkl.util.img.ImageMemoryCache;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created on 2017/10/26.
 */

public class OkHttpUtils {
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    private Handler mHandler;

    private Context mContext;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private String jsomString;

    private OkHttpUtils(Context context) {
        super();
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.readTimeout(20, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(15, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        clientBuilder.addInterceptor(interceptor);
        mOkHttpClient = clientBuilder.build();
        this.mContext = context.getApplicationContext();
        mHandler = new Handler(mContext.getMainLooper());
    }


    /**
     * get方法连接拼加参数
     *
     * @param
     * @return
     */
//    private String setUrlParams(Map<String, String> mapParams) {
//        String strParams = "";
//        if (mapParams != null) {
//            Iterator<String> iterator = mapParams.keySet().iterator();
//            String key = "";
//            while (iterator.hasNext()) {
//                key = iterator.next().toString();
//                strParams += "&" + key + "=" + mapParams.get(key);
//            }
//        }
//
//        return strParams;
//    }


    public static OkHttpUtils getInstance(Context context) {
        OkHttpUtils temp = mInstance;
        if (temp == null) {
            synchronized (OkHttpUtils.class) {
                temp = mInstance;
                if (temp == null) {
                    temp = new OkHttpUtils(context);
                    mInstance = temp;
                }
            }
        }
        return temp;
    }


    /**
     * post请求参数
     *
     * @param BodyParams
     * @return
     */
    private RequestBody SetPostRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * Post上传图片的参数
     *
     * @param BodyParams
     * @param
     * @return
     */
    private RequestBody SetFileRequestBody(Map<String, String> BodyParams, Map<String, String> filePathParams) {
        // 带文件的Post参数
        RequestBody body = null;
        MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
            }
        }
        if (filePathParams != null) {
            Iterator<String> iterator = filePathParams.keySet().iterator();
            String key = "";
            int i = 0;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                i++;
                MultipartBodyBuilder.addFormDataPart(key, filePathParams.get(key));
                fileBody = RequestBody.create(MEDIA_TYPE_PNG, new File(filePathParams.get(key)));
                MultipartBodyBuilder.addFormDataPart(key, i + ".png", fileBody);
            }
        }
        body = MultipartBodyBuilder.build();
        return body;
    }

    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private String setGetUrlParams(Map<String, String> mapParams) {
        String strParams = "";
       int mapPosition=0;
        if (mapParams != null) {
            Iterator<String> iterator = mapParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                mapPosition+=1;
                if (mapPosition==0){
                    strParams += "?" + key + "=" + mapParams.get(key);
                }else {
                    strParams += "&" + key + "=" + mapParams.get(key);
                }
            }
        }
        Log.i("TTTTTTTTTTT",strParams+"");
        return strParams;
    }

    /**
     * 实现post请求
     *
     * @param reqUrl
     * @param headersParams
     * @param params
     * @param callback
     */
    public void doPost(String reqUrl, Map<String, String> headersParams, Map<String, String> params, final NetCallback callback) {
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.method("POST", SetPostRequestBody(params));
        RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
        Request request = RequestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                jsomString = response.body().string();
//                Log.e("QQQQQQQQQQQQQ", jsomString);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(0, jsomString);
                        call.cancel();
                    }
                });
            }

            @Override
            public void onFailure(final Call call, final IOException exception) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        callback.onFailure(-1, exception.getMessage());
                        call.cancel();
                    }
                });
            }
        });
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private Headers SetHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
            }
        }
        headers = headersbuilder.build();
        return headers;
    }

    /**
     * 实现get请求
     *
     * @param reqUrl
     * @param headersParams
     * @param params
     * @param callback
     */
    public void doGet(String reqUrl, Map<String, String> headersParams, Map<String, String> params, final NetCallback callback) {
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl + setGetUrlParams(params));// 添加URL地址 自行加 ?
        RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
        Request request = RequestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String getjsom = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(0, getjsom);
                        call.cancel();
                    }
                });
            }

            @Override
            public void onFailure(final Call call, final IOException exception) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("DDDD2","失败");
                        callback.onFailure(-1, exception.getMessage());
                        call.cancel();
                    }
                });
            }
        });
    }

    /**
     * 实现加载图片
     *
     * @param context
     * @param reqUrl
     * @param headersParams
     * @param imageView
     * @param defResImag    imageView.setImageResource(context.getResources().getIdentifier(defResImag, "drawable", context.getPackageName()));
     */
    public void loadImage(final Context context, final String reqUrl, Map<String, String> headersParams, final ImageView imageView, final String defResImag) {
        Bitmap result;
        // 从内存缓存中获取图片
        final ImageMemoryCache memoryCache = new ImageMemoryCache(context);
        result = memoryCache.getBitmapFromCache(reqUrl);
        if (result != null) {
            imageView.setImageBitmap(result);
            return;
        }
        // 从硬盘缓存中获取图片
        final ImageFileCache fileCache = new ImageFileCache(context);
        result = fileCache.getImage(context, reqUrl);
        if (result != null) {
            imageView.setImageBitmap(result);
            // 添加到内存缓存
            memoryCache.addBitmapToCache(reqUrl, result);
            return;
        }
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        // RequestBuilder.headers(SetHeaders(headersParams));// 添加请求头
        Request request = RequestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                final Bitmap decodeStream = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (decodeStream != null) {
                                imageView.setImageBitmap(decodeStream);
                                // 缓存在文件
                                fileCache.saveBitmap(context, decodeStream, reqUrl);
                                // 缓存在内存
                                memoryCache.addBitmapToCache(reqUrl, decodeStream);
                            } else {
                                // 加载图片
                                imageView.setImageResource(context.getResources().getIdentifier(defResImag, "drawable", context.getPackageName()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            call.cancel();
                        }
                    }
                });
            }

            @Override
            public void onFailure(final Call call, final IOException exception) {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // 加载图片
                        imageView.setImageResource(context.getResources().getIdentifier(defResImag, "drawable", context.getPackageName()));
                        call.cancel();
                    }
                });
            }
        });
    }

    public abstract static class NetCallback {
        public abstract void onFailure(int code, String msg);

        public abstract void onSuccess(int code, String content);

        public abstract void loadImage(Bitmap bitmap);
    }
    /*图片picasso加载*/
    public static void picassoImage(String url, Context context, ImageView imageView) {

        Picasso.with(context).load(url).config(Bitmap.Config.RGB_565).into(imageView);
    }

}
