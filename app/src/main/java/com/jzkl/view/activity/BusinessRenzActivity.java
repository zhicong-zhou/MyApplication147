package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.PhotoUtils;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

/*
 * 商家认证
 * */
public class BusinessRenzActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.sulicu_happy_busName)
    EditText sulicuHappyBusName;
    @BindView(R.id.sulicu_happy_tel)
    EditText sulicuHappyTel;
    @BindView(R.id.sulicu_happy_Name)
    EditText sulicu_happy_Name;
    @BindView(R.id.sulicu_happy_addr)
    EditText sulicuHappyAddr;
    @BindView(R.id.sulicu_happy_but)
    Button sulicuHappyBut;

    /*门头*/
    @BindView(R.id.sulicu_happy_mtll)
    LinearLayout mtll;
    @BindView(R.id.sulicu_happy_mt)
    LinearLayout sulicuHappyMt;
    @BindView(R.id.sulicu_happy_mt_img)
    ImageView mtImg;
    /*内景*/
    @BindView(R.id.sulicu_happy_njll)
    LinearLayout njll;
    @BindView(R.id.sulicu_happy_nj)
    LinearLayout sulicuHappyNj;
    @BindView(R.id.sulicu_happy_nj_img)
    ImageView njImg;
    /*收银台*/
    @BindView(R.id.sulicu_happy_sytll)
    LinearLayout sytll;
    @BindView(R.id.sulicu_happy_syt)
    LinearLayout sulicuHappySyt;
    @BindView(R.id.sulicu_happy_syt_img)
    ImageView sytImg;

    /*身份证正面*/
    @BindView(R.id.sulicu_happy_zmll)
    LinearLayout zmll;
    @BindView(R.id.sulicu_happy_zm)
    LinearLayout idenZm;
    @BindView(R.id.sulicu_happy_zm_img)
    ImageView zmImg;
    /*身份证反面*/
    @BindView(R.id.fan_ll)
    LinearLayout fan_ll;
    @BindView(R.id.sulicu_happy_fm)
    LinearLayout idenFm;
    @BindView(R.id.sulicu_happy_fm_img)
    ImageView fmImg;
    /*营业执照*/
    @BindView(R.id.sulicu_happy_yyzzll)
    LinearLayout yyzzll;
    @BindView(R.id.sulicu_happy_yyzz)
    LinearLayout sulicuHappyYyzz;
    @BindView(R.id.sulicu_happy_yyzz_img)
    ImageView yyzzImg;

    protected ImmersionBar mImmersionBar;

    String userinfo, token, merchantStr, resultStr;
    CustomDialog customDialog;
    String bName, bTel, linkman, bAddress, imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6;

    public final int CODE_TAKE_PHOTO = 1;
    public final int CODE_TAKE_PHOTO2 = 12;
    public final int CODE_TAKE_PHOTO3 = 13;
    public final int CODE_TAKE_PHOTO4 = 14;
    public final int CODE_TAKE_PHOTO5 = 15;
    public final int CODE_TAKE_PHOTO6 = 16;
    public final int CODE_SELECT_IMAGE = 2;//相册 正面
    public final int CODE_SELECT_IMAGE2 = 22;//相册 反面
    public final int CODE_SELECT_IMAGE3 = 23;//营业执照
    public final int CODE_SELECT_IMAGE4 = 24;//收银台
    public final int CODE_SELECT_IMAGE5 = 25;//门头
    public final int CODE_SELECT_IMAGE6 = 26;//门头
    private final int CODE_CROP_CARMER = 31;
    private final int CODE_CROP_CARMER2 = 32;
    private final int CODE_CROP_CARMER3 = 33;
    private final int CODE_CROP_CARMER4 = 34;
    private final int CODE_CROP_CARMER5 = 35;
    private final int CODE_CROP_CARMER6 = 36;
    private List strings;
    Uri uriForFile, uriForFile2, uriForFile3, uriForFile4, uriForFile5, uriForFile6, cropUri, cropUri2, cropUri3, cropUri4, cropUri5, cropUri6;
    Intent intent;
    String mobile, name, iddress, idnoImgZUrl, idnoImgFUrl, businessImgUrl, cashImgUrl, interiorImgUrl, doorheadImgUrl;


    private  String cachPath,cachPath2,cachPath3,cachPath4,cachPath5,cachPath6;
    private  File cacheFile,cacheFile2,cacheFile3,cacheFile4,cacheFile5,cacheFile6;
    private  File cameraFile,cameraFile2,cameraFile3,cameraFile4,cameraFile5,cameraFile6;
    private Uri imageUri,imageUri2,imageUri3,imageUri4,imageUri5,imageUri6;
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO2 = 12;
    public static final int TAKE_PHOTO3 = 13;
    public static final int TAKE_PHOTO4 = 14;
    public static final int TAKE_PHOTO5 = 15;
    public static final int TAKE_PHOTO6 = 16;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CHOOSE_PHOTO2 = 22;
    public static final int CHOOSE_PHOTO3 = 23;
    public static final int CHOOSE_PHOTO4 = 24;
    public static final int CHOOSE_PHOTO5 = 25;
    public static final int CHOOSE_PHOTO6 = 26;
    public static final int CROP_PHOTO = 3;
    public static final int CROP_PHOTO2 = 32;
    public static final int CROP_PHOTO3 = 33;
    public static final int CROP_PHOTO4 = 34;
    public static final int CROP_PHOTO5 = 35;
    public static final int CROP_PHOTO6 = 36;


    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
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
        return R.layout.activity_business_renz;
    }

    @OnClick({R.id.common_back, R.id.sulicu_happy_zmll, R.id.fan_ll, R.id.sulicu_happy_yyzzll, R.id.sulicu_happy_sytll, R.id.sulicu_happy_mtll, R.id.sulicu_happy_njll, R.id.sulicu_happy_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*身份证正面*/
            case R.id.sulicu_happy_zmll:
                getDialog(1);
                break;
            /*反面*/
            case R.id.fan_ll:
                getDialog(2);
                break;
            /*营业执照*/
            case R.id.sulicu_happy_yyzzll:
                getDialog(3);
                break;
            /*收银台*/
            case R.id.sulicu_happy_sytll:
                getDialog(4);
                break;
            /*门头*/
            case R.id.sulicu_happy_mtll:
                getDialog(5);
                break;
            /*内景*/
            case R.id.sulicu_happy_njll:
                getDialog(6);
                break;
            /*上传审核*/
            case R.id.sulicu_happy_but:
                if (resultStr == null) {
                    bName = sulicuHappyBusName.getText().toString().trim();
                    linkman = sulicu_happy_Name.getText().toString().trim();
                    bTel = sulicuHappyTel.getText().toString().trim();
                    bAddress = sulicuHappyAddr.getText().toString().trim();
                    if (bName.equals("")) {
                        ToastUtil.show("商户名不能为空");
                        return;
                    } else if (linkman.equals("")) {
                        ToastUtil.show("联系人不能为空");
                        return;
                    } else if (bTel.equals("")) {
                        ToastUtil.show("电话不能为空");
                        return;
                    } else if (bAddress.equals("")) {
                        ToastUtil.show("地址不能为空");
                        return;
                    } else if (imgUrl1 == null) {
                        ToastUtil.show("身份证正面不能为空");
                        return;
                    } else if (imgUrl2 == null) {
                        ToastUtil.show("身份证反面不能为空");
                        return;
                    } else if (imgUrl3 == null) {
                        ToastUtil.show("营业执照不能为空");
                        return;
                    } else if (imgUrl4 == null) {
                        ToastUtil.show("营业执照不能为空");
                        return;
                    } else if (imgUrl5 == null) {
                        ToastUtil.show("门头照不能为空");
                        return;
                    } else if (imgUrl6 == null) {
                        ToastUtil.show("内景照片不能为空");
                        return;
                    }
                    subData();
                } else {
                    ToastUtil.show(resultStr);
                }
                break;
        }
    }

    private void getDialog(final int type) {
        AndPermission.with(BusinessRenzActivity.this).permission(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                openCamera(type);
            }
            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                Toast.makeText(BusinessRenzActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    /**
     * 将图片存到本地
     * 获得本地图片路径
     * @param context
     * @return
     */
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    private  File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 从相机获取图片
     */
    private void openCamera(int type) {
        if(type == 1){
            cameraFile = getCacheFile(new File(getDiskCacheDir(this)),"output_image.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(cameraFile);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }else if(type == 2){
            cameraFile2 = getCacheFile(new File(getDiskCacheDir(this)),"output_image2.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri2 = Uri.fromFile(cameraFile2);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri2 = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile2);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
            startActivityForResult(intent, TAKE_PHOTO2);
        }else if(type == 3){
            cameraFile3 = getCacheFile(new File(getDiskCacheDir(this)),"output_image3.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri3 = Uri.fromFile(cameraFile3);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri3 = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile3);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);
            startActivityForResult(intent, TAKE_PHOTO3);
        }else if(type == 4){
            cameraFile4 = getCacheFile(new File(getDiskCacheDir(this)),"output_image4.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri4 = Uri.fromFile(cameraFile4);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri4 = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile4);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri4);
            startActivityForResult(intent, TAKE_PHOTO4);
        }else if(type == 5){
            cameraFile5= getCacheFile(new File(getDiskCacheDir(this)),"output_image5.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri5 = Uri.fromFile(cameraFile5);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri5 = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile5);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
            startActivityForResult(intent, TAKE_PHOTO5);
        }else if(type == 6){
            cameraFile6 = getCacheFile(new File(getDiskCacheDir(this)),"output_image6.jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri6 = Uri.fromFile(cameraFile6);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri6 = FileProvider.getUriForFile(BusinessRenzActivity.this, getPackageName() + ".fileprovider", cameraFile6);
            }
            // 启动相机程序
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri6);
            startActivityForResult(intent, TAKE_PHOTO6);
        }
    }
    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file, int size,int type) {
        Log.i("TAG",getImageContentUri(this,file)+"裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this,file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0.1);
            intent.putExtra("aspectY", 0.1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 400);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra("circleCrop", false);
            if(type==1){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO);
            }else if(type==2){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile2));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO2);
            }else if(type==3){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile3));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO3);
            }else if(type==4){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile4));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO4);
            }else if(type==5){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile5));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO5);
            }else if(type==6){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile6));//定义输出的File Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, CROP_PHOTO6);
            }
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转化地址为content开头
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    @Override
    protected void initData() {
        cachPath=getDiskCacheDir(this)+ "/handimg.jpg";//图片路径
        cachPath2=getDiskCacheDir(this)+ "/handimg2.jpg";//图片路径
        cachPath3=getDiskCacheDir(this)+ "/handimg3.jpg";//图片路径
        cachPath4=getDiskCacheDir(this)+ "/handimg4.jpg";//图片路径
        cachPath5=getDiskCacheDir(this)+ "/handimg5.jpg";//图片路径
        cachPath6=getDiskCacheDir(this)+ "/handimg6.jpg";//图片路径
        cacheFile =getCacheFile(new File(getDiskCacheDir(this)),"handimg.jpg");
        cacheFile2 =getCacheFile(new File(getDiskCacheDir(this)),"handimg2.jpg");
        cacheFile3 =getCacheFile(new File(getDiskCacheDir(this)),"handimg3.jpg");
        cacheFile4 =getCacheFile(new File(getDiskCacheDir(this)),"handimg4.jpg");
        cacheFile5 =getCacheFile(new File(getDiskCacheDir(this)),"handimg5.jpg");
        cacheFile6 =getCacheFile(new File(getDiskCacheDir(this)),"handimg6.jpg");

        merchantStr = getIntent().getStringExtra("merchantStr");
        commonTitle.setText("商家认证");

        sulicuHappyBusName.addTextChangedListener(this);
        sulicu_happy_Name.addTextChangedListener(this);
        sulicuHappyTel.addTextChangedListener(this);
        sulicuHappyAddr.addTextChangedListener(this);

        getUser();
        /*商户审核信息*/
        getRenzInfo();
    }

    private void getRenzInfo() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("type", "merchant");
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.my_shop_renz_info)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONObject info = jsonObject1.getJSONObject("info");
                                    JSONObject identification = jsonObject1.getJSONObject("identification");
                                    resultStr = identification.getString("resultStr");
                                    mobile = info.getString("mobile");
                                    name = info.getString("name");
                                    linkman = info.getString("linkman");
                                    iddress = info.getString("iddress");

                                    idnoImgZUrl = info.getString("idnoImgZUrl");
                                    idnoImgFUrl = info.getString("idnoImgFUrl");
                                    businessImgUrl = info.getString("businessImgUrl");
                                    cashImgUrl = info.getString("cashImgUrl");
                                    interiorImgUrl = info.getString("interiorImgUrl");
                                    doorheadImgUrl = info.getString("doorheadImgUrl");

                                    idenZm.setVisibility(View.GONE);
                                    zmImg.setVisibility(View.VISIBLE);
                                    idenFm.setVisibility(View.GONE);
                                    fmImg.setVisibility(View.VISIBLE);
                                    sulicuHappyYyzz.setVisibility(View.GONE);
                                    yyzzImg.setVisibility(View.VISIBLE);
                                    sulicuHappySyt.setVisibility(View.GONE);
                                    sytImg.setVisibility(View.VISIBLE);
                                    sulicuHappyMt.setVisibility(View.GONE);
                                    mtImg.setVisibility(View.VISIBLE);
                                    sulicuHappyNj.setVisibility(View.GONE);
                                    njImg.setVisibility(View.VISIBLE);
                                    /*身份正面*/
                                    if (idnoImgZUrl.equals("")) {
                                        idenZm.setVisibility(View.VISIBLE);
                                        zmImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(idnoImgZUrl, BusinessRenzActivity.this, zmImg);
                                    }
                                    /*身份反面*/
                                    if (idnoImgFUrl.equals("")) {
                                        idenFm.setVisibility(View.VISIBLE);
                                        fmImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(idnoImgFUrl, BusinessRenzActivity.this, fmImg);
                                    }
                                    /*营业执照*/
                                    if (businessImgUrl.equals("")) {
                                        sulicuHappyYyzz.setVisibility(View.VISIBLE);
                                        yyzzImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(businessImgUrl, BusinessRenzActivity.this, yyzzImg);
                                    }
                                    /*收银台*/
                                    if (cashImgUrl.equals("")) {
                                        sulicuHappySyt.setVisibility(View.VISIBLE);
                                        sytImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(cashImgUrl, BusinessRenzActivity.this, sytImg);
                                    }
                                    /*内景*/
                                    if (interiorImgUrl.equals("")) {
                                        sulicuHappyNj.setVisibility(View.VISIBLE);
                                        njImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(interiorImgUrl, BusinessRenzActivity.this, njImg);
                                    }
                                    /*门头*/
                                    if (doorheadImgUrl.equals("")) {
                                        sulicuHappyMt.setVisibility(View.VISIBLE);
                                        mtImg.setVisibility(View.GONE);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(doorheadImgUrl, BusinessRenzActivity.this, mtImg);
                                    }

                                    sulicuHappyBusName.setFocusable(false);
                                    sulicu_happy_Name.setFocusable(false);
                                    sulicuHappyTel.setFocusable(false);
                                    sulicuHappyAddr.setFocusable(false);

                                    sulicuHappyBusName.setText(name);
                                    sulicu_happy_Name.setText(linkman);
                                    sulicuHappyTel.setText(mobile);
                                    sulicuHappyAddr.setText(iddress);
                                } else {
                                    idenZm.setVisibility(View.VISIBLE);
                                    zmImg.setVisibility(View.GONE);

                                    idenFm.setVisibility(View.VISIBLE);
                                    fmImg.setVisibility(View.GONE);

                                    sulicuHappyYyzz.setVisibility(View.VISIBLE);
                                    yyzzImg.setVisibility(View.GONE);

                                    sulicuHappySyt.setVisibility(View.VISIBLE);
                                    sytImg.setVisibility(View.GONE);

                                    sulicuHappyNj.setVisibility(View.VISIBLE);
                                    njImg.setVisibility(View.GONE);

                                    sulicuHappyMt.setVisibility(View.VISIBLE);
                                    mtImg.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                        ToastUtil.show(e + "");
                    }

                });
    }

    private void subData() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("name", bName);
        map.put("linkman", linkman);//联系人
        map.put("mobile", bTel);
        map.put("iddress", bAddress);
        map.put("businessImgUrl", imgUrl3);//营业照片
        map.put("cashImgUrl", imgUrl4);//收银台照
        map.put("doorheadImgUrl", imgUrl5);//门头照片 ,
        map.put("idnoImgFUrl", imgUrl2);//身份证反面照片 ,
        map.put("idnoImgZUrl", imgUrl1);//身份证正面照片 ,
        map.put("interiorImgUrl", imgUrl6);//内景照片 ,

        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.my_shop_renz)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                String msg = jsonObject1.getString("msg");
                                if (code == 0) {
                                    ToastUtil.show("上传成功");
                                    finish();
                                } else {
                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                        ToastUtil.show(e + "");
                    }
                });
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*身份证正面 拍照*/
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile,350,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    idenZm.setVisibility(View.VISIBLE);
                    zmImg.setVisibility(View.GONE);
                }
                break;
            /*身份证反面 拍照*/
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile2,350,2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    idenFm.setVisibility(View.VISIBLE);
                    fmImg.setVisibility(View.GONE);
                }
                break;
            /*营业执照 拍照*/
            case TAKE_PHOTO3:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile3,350,3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    sulicuHappyYyzz.setVisibility(View.VISIBLE);
                    yyzzImg.setVisibility(View.GONE);
                }
                break;
            /*收银台 拍照*/
            case TAKE_PHOTO4:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile4,350,4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    sulicuHappySyt.setVisibility(View.VISIBLE);
                    sytImg.setVisibility(View.GONE);
                }
                break;
            /*门头 拍照*/
            case TAKE_PHOTO5:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile5,350,5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    sulicuHappyMt.setVisibility(View.VISIBLE);
                    mtImg.setVisibility(View.GONE);
                }
                break;
            /*内景 拍照*/
            case TAKE_PHOTO6:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile6,350,6);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    sulicuHappyNj.setVisibility(View.VISIBLE);
                    njImg.setVisibility(View.GONE);
                }
                break;
            /*正 身份证 裁剪*/
            case CROP_PHOTO:
                try {
                    if (resultCode==RESULT_OK){
                        idenZm.setVisibility(View.GONE);
                        zmImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
                        zmImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile).getPath();
                        File file = new File(imgUrl);
                        subImage(file,1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            /*反 身份证 裁剪*/
            case CROP_PHOTO2:
                try {
                    if (resultCode==RESULT_OK){
                        idenFm.setVisibility(View.GONE);
                        fmImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath2))));
                        fmImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile2).getPath();
                        File file = new File(imgUrl);
                        subImage(file,2);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            /*营业执照 裁剪*/
            case CROP_PHOTO3:
                try {
                    if (resultCode==RESULT_OK){
                        sulicuHappyYyzz.setVisibility(View.GONE);
                        yyzzImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath3))));
                        yyzzImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile3).getPath();
                        File file = new File(imgUrl);
                        subImage(file,3);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            /*收银台 裁剪*/
            case CROP_PHOTO4:
                try {
                    if (resultCode==RESULT_OK){
                        sulicuHappySyt.setVisibility(View.GONE);
                        sytImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath4))));
                        sytImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile4).getPath();
                        File file = new File(imgUrl);
                        subImage(file,4);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            /*门头 裁剪*/
            case CROP_PHOTO5:
                try {
                    if (resultCode==RESULT_OK){
                        sulicuHappyMt.setVisibility(View.GONE);
                        mtImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath5))));
                        mtImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile5).getPath();
                        File file = new File(imgUrl);
                        subImage(file,5);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            /*内景 裁剪*/
            case CROP_PHOTO6:
                try {
                    if (resultCode==RESULT_OK){
                        sulicuHappyNj.setVisibility(View.GONE);
                        njImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath6))));
                        njImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile6).getPath();
                        File file = new File(imgUrl);
                        subImage(file,6);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void subImage(File file, final int type) {
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    if (type == 1) {
                                        imgUrl1 = jsonObject.getString("url");
                                    } else if (type == 2) {
                                        imgUrl2 = jsonObject.getString("url");
                                    } else if (type == 3) {
                                        imgUrl3 = jsonObject.getString("url");
                                    } else if (type == 4) {
                                        imgUrl4 = jsonObject.getString("url");
                                    } else if (type == 5) {
                                        imgUrl5 = jsonObject.getString("url");
                                    } else if (type == 6) {
                                        imgUrl6 = jsonObject.getString("url");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                    }
                });
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!sulicuHappyBusName.getText().toString().trim().equals("") && !sulicu_happy_Name.getText().toString().trim().equals("")
                && sulicuHappyTel.getText().toString().trim().length() == 11 && !sulicuHappyAddr.getText().toString().trim().equals("")) {//判断账号是否11位
            sulicuHappyBut.setBackground(getResources().getDrawable(R.drawable.shape_button));
            sulicuHappyBut.setTextColor(Color.parseColor("#ffffff"));
        } else {
            sulicuHappyBut.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
            sulicuHappyBut.setTextColor(Color.parseColor("#737679"));
        }
    }
    @Override
    public void afterTextChanged(Editable s) {

    }
}
