package com.jzkl.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.ImageFactory;
import com.jzkl.util.RegularUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SetRenZActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.set_renz_name)
    EditText setRenzName;
    @BindView(R.id.set_renz_tel)
    EditText setRenzTel;
    @BindView(R.id.set_renz_ident)
    EditText setRenzIdent;
    @BindView(R.id.set_renz_bankNo)
    EditText setRenzBankNo;

    @BindView(R.id.sulicu_happy_sc_img)
    ImageView scImg;
    @BindView(R.id.sulicu_happy_bank_img)
    ImageView bankImg;

    @BindView(R.id.my_renz_scll)
    LinearLayout my_renz_scll;
    @BindView(R.id.my_renz_sc)
    LinearLayout my_renz_sc;
    @BindView(R.id.my_renz_sc_bank)
    LinearLayout my_renz_sc_bank;
    @BindView(R.id.my_renz_sc_bankll)
    LinearLayout my_renz_sc_bankll;
    @BindView(R.id.set_renz_but)
    Button setRenzBut;

    @BindView(R.id.my_renz_zmll)
    LinearLayout my_renz_zmll;
    @BindView(R.id.my_renz_zm)
    LinearLayout my_renz_zm;
    @BindView(R.id.sulicu_rz_zm_img)
    ImageView zm_img;

    @BindView(R.id.my_renz_fmll)
    LinearLayout my_renz_fmll;
    @BindView(R.id.my_renz_fm)
    LinearLayout my_renz_fm;
    @BindView(R.id.sulicu_rz_fm_img)
    ImageView fm_img;

    @BindView(R.id.my_renz_bank02_ll)
    LinearLayout my_renz_bank02_ll;
    @BindView(R.id.my_renz_bank02)
    LinearLayout my_renz_bank02;
    @BindView(R.id.sulicu_happy_bank02_img)
    ImageView bank02_img;

    @BindView(R.id.renz_reason)
    TextView renz_reason;

    protected ImmersionBar mImmersionBar;
    String userinfo, token;
    CustomDialog customDialog;
    String rName, rTel, rIdent, rBank, imgUrl, imgUrl1, realnameStr, resultStr,
            imgFmUrl1,imgScUrl1,imgBank2Url1;

    private  File cameraFile,cameraFile2,cameraFile3,cameraFile4,cameraFile5;
    private Uri imageUri,imageUri2,imageUri3,imageUri4,imageUri5;
    public static final int TAKE_PHOTO = 1;
    public static final int TAKE_PHOTO2 = 12;
    public static final int TAKE_PHOTO3 = 13;
    public static final int TAKE_PHOTO4 = 14;
    public static final int TAKE_PHOTO5 = 15;

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
        return R.layout.activity_set_ren_z;
    }


    @Override
    protected void initData() {

        realnameStr = getIntent().getStringExtra("realnameStr");
        commonTitle.setText("实名认证");

        setRenzTel.addTextChangedListener(this);
        setRenzName.addTextChangedListener(this);
        setRenzIdent.addTextChangedListener(this);
        setRenzBankNo.addTextChangedListener(this);

        getUser();
        /*认证信息*/
        getRenzInfo();
    }

    private void getRenzInfo() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("type", "realName");

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
                                    String reason = identification.getString("reason");
                                    String mobile = info.getString("mobile");
                                    String name = info.getString("name");
                                    String idno = info.getString("idno");

                                    imgUrl = info.getString("idnoImgUrl");
                                    imgUrl1 = info.getString("bankImgUrl");

                                    setRenzBut.setText(resultStr);

                                    boolean flag = info.has("bankNo");
                                    String bankNo="";
                                    if(flag){
                                        bankNo = info.getString("bankNo");
                                    }
                                    boolean imgFm = info.has("idnoImgBackUrl");
                                    if(imgFm){
                                        imgFmUrl1 = info.getString("idnoImgBackUrl");
                                    }
                                    boolean flagSc = info.has("idnoImgHeadUrl");
                                    if(flagSc){
                                        imgScUrl1 = info.getString("idnoImgHeadUrl");
                                    }
                                    boolean flagBank = info.has("bankImgBackUrl");
                                    if(flagBank){
                                        imgBank2Url1 = info.getString("bankImgBackUrl");
                                    }
                                    if(!resultStr.equals("认证失败")){
                                        setRenzTel.setFocusable(false);
                                        setRenzName.setFocusable(false);
                                        setRenzIdent.setFocusable(false);
                                        setRenzBankNo.setFocusable(false);

                                        renz_reason.setVisibility(View.VISIBLE);
                                        renz_reason.setText(resultStr);
                                    }else {
                                        renz_reason.setVisibility(View.VISIBLE);
                                        renz_reason.setText("失败原因："+reason);

                                        setRenzTel.setFocusable(true);
                                        setRenzName.setFocusable(true);
                                        setRenzIdent.setFocusable(true);
                                        setRenzBankNo.setFocusable(true);
                                    }
                                    setRenzTel.setText(mobile);
                                    setRenzName.setText(name);
                                    setRenzIdent.setText(idno);
                                    setRenzBankNo.setText(bankNo);
                                    if(imgUrl!=null){
                                        /*身份证正面照*/
                                        if (imgUrl.equals("")) {
                                            my_renz_zm.setVisibility(View.VISIBLE);
                                            zm_img.setVisibility(View.GONE);
                                        } else {
                                            my_renz_zm.setVisibility(View.GONE);
                                            zm_img.setVisibility(View.VISIBLE);
                                            com.jzkl.util.OkHttpUtils.picassoImage(imgUrl, SetRenZActivity.this, zm_img);
                                        }
                                    }
                                    if(imgFmUrl1!=null){
                                        /*身份证反面照*/
                                        if (imgFmUrl1.equals("")) {
                                            my_renz_fm.setVisibility(View.VISIBLE);
                                            fm_img.setVisibility(View.GONE);
                                        } else {
                                            my_renz_fm.setVisibility(View.GONE);
                                            fm_img.setVisibility(View.VISIBLE);
                                            com.jzkl.util.OkHttpUtils.picassoImage(imgFmUrl1, SetRenZActivity.this, fm_img);
                                        }
                                    }
                                    if(imgBank2Url1!=null){
                                        /*银行卡反面照*/
                                        if (imgBank2Url1.equals("")) {
                                            my_renz_bank02.setVisibility(View.VISIBLE);
                                            bank02_img.setVisibility(View.GONE);
                                        } else {
                                            my_renz_bank02.setVisibility(View.GONE);
                                            bank02_img.setVisibility(View.VISIBLE);
                                            com.jzkl.util.OkHttpUtils.picassoImage(imgBank2Url1, SetRenZActivity.this, bank02_img);
                                        }
                                    }
                                    if(imgScUrl1!=null){
                                        /*手持身份证照*/
                                        if (imgScUrl1.equals("")) {
                                            my_renz_sc.setVisibility(View.VISIBLE);
                                            scImg.setVisibility(View.GONE);
                                        } else {
                                            my_renz_sc.setVisibility(View.GONE);
                                            scImg.setVisibility(View.VISIBLE);
                                            com.jzkl.util.OkHttpUtils.picassoImage(imgScUrl1, SetRenZActivity.this, scImg);
                                        }
                                    }
                                    if(imgUrl1!=null){
                                        /*银行卡照*/
                                        if (imgUrl1.equals("")) {
                                            my_renz_sc_bank.setVisibility(View.VISIBLE);
                                            bankImg.setVisibility(View.GONE);
                                        } else {
                                            my_renz_sc_bank.setVisibility(View.GONE);
                                            bankImg.setVisibility(View.VISIBLE);
                                            com.jzkl.util.OkHttpUtils.picassoImage(imgUrl1, SetRenZActivity.this, bankImg);
                                        }
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
                        ToastUtil.show("" + e);
                    }
                });
    }

    private void getRenz() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("idno", rIdent);
        map.put("mobile", rTel);
        map.put("name", rName);
        map.put("bankNo", rBank);
        map.put("bankImgUrl", imgUrl1);
        map.put("idnoImgUrl", imgUrl);
        map.put("idnoImgBackUrl", imgFmUrl1);
        map.put("idnoImgHeadUrl", imgScUrl1);
        map.put("bankImgBackUrl", imgBank2Url1);

        final String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.my_user_renz)
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
                                    ToastUtil.show("提交成功");
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
                    public void onCacheError(Call call, Exception e) {
                        super.onCacheError(call, e);
                    }
                });
    }

    @OnClick({R.id.common_back, R.id.my_renz_scll, R.id.my_renz_sc_bankll, R.id.set_renz_but,R.id.my_renz_zmll,R.id.my_renz_fmll,R.id.my_renz_bank02_ll})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*手持身份证*/
            case R.id.my_renz_scll:
                getDialog(4);
                break;
            /*银行卡*/
            case R.id.my_renz_sc_bankll:
                getDialog(2);
                break;
                /*正面*/
            case R.id.my_renz_zmll:
                getDialog(1);
                break;
                /*反面*/
            case R.id.my_renz_fmll:
                getDialog(3);
                break;
                /*银行卡反面*/
            case R.id.my_renz_bank02_ll:
                getDialog(5);
                break;
            /*确认*/
            case R.id.set_renz_but:
                if (resultStr == null) {
                    rName = setRenzName.getText().toString().trim();
                    rTel = setRenzTel.getText().toString().trim();
                    rIdent = setRenzIdent.getText().toString().trim();
                    rBank = setRenzBankNo.getText().toString().trim();
                    if (rName.equals("")) {
                        ToastUtil.show("姓名不能为空");
                        return;
                    } else if (rTel.equals("")) {
                        ToastUtil.show("电话不能为空");
                        return;
                    } else if (rTel.length()!=11) {
                        ToastUtil.show("电话号不合法");
                        return;
                    } else if (rIdent.equals("")) {
                        ToastUtil.show("身份证号不能为空");
                        return;
                    } else if (!RegularUtil.isIDacard(rIdent)) {
                        ToastUtil.show("身份证号不合法");
                        return;
                    } else if (rBank.equals("")) {
                        ToastUtil.show("银行卡号不合法");
                        return;
                    } else if (imgUrl == null) {
                        ToastUtil.show("身份证正面照不能为空");
                        return;
                    }else if (imgFmUrl1 == null) {
                        ToastUtil.show("身份证反面照不能为空");
                        return;
                    } else if (imgScUrl1 == null) {
                        ToastUtil.show("手持身份证照不能为空");
                        return;
                    }else if (imgUrl1 == null) {
                        ToastUtil.show("银行卡正面照不能为空");
                        return;
                    }else if (imgBank2Url1 == null) {
                        ToastUtil.show("银行卡反面照不能为空");
                        return;
                    }
                    getRenz();
                } else {
                    if (resultStr.equals("认证失败")) {
                        rName = setRenzName.getText().toString().trim();
                        rTel = setRenzTel.getText().toString().trim();
                        rIdent = setRenzIdent.getText().toString().trim();
                        rBank = setRenzBankNo.getText().toString().trim();
                        if (rName.equals("")) {
                            ToastUtil.show("姓名不能为空");
                            return;
                        } else if (rTel.equals("")) {
                            ToastUtil.show("电话不能为空");
                            return;
                        } else if (rIdent.equals("")) {
                            ToastUtil.show("身份证号不能为空");
                            return;
                        } else if (!RegularUtil.isIDacard(rIdent)) {
                            ToastUtil.show("身份证号不合法");
                            return;
                        } else if (rBank.equals("")) {
                            ToastUtil.show("银行卡号不合法");
                            return;
                        } else if (imgUrl == null) {
                            ToastUtil.show("身份证照不能为空");
                            return;
                        } else if (imgUrl1 == null) {
                            ToastUtil.show("银行卡照不能为空11");
                            return;
                        }
                        getRenz();
                    } else {
                        ToastUtil.show(resultStr);
                    }
                }
                break;
        }
    }

    private void getDialog(final int type) {
        AndPermission.with(SetRenZActivity.this).permission(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                openCamera(type);
            }
            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                Toast.makeText(SetRenZActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    /**
     * 从相机获取图片
     */
    private void openCamera(int type) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(type == 1){
            cameraFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/wanmo/" + System.currentTimeMillis() + ".jpg");
            cameraFile.getParentFile().mkdirs();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(cameraFile);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(SetRenZActivity.this, getPackageName() + ".fileprovider", cameraFile);
            }
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }else if(type == 2){
            cameraFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/wanmo/" + System.currentTimeMillis() + ".jpg");
            cameraFile2.getParentFile().mkdirs();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri2 = Uri.fromFile(cameraFile2);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri2 = FileProvider.getUriForFile(SetRenZActivity.this, getPackageName() + ".fileprovider", cameraFile2);
            }
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
            startActivityForResult(intent, TAKE_PHOTO2);
        }else if(type == 3){
            cameraFile3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/wanmo/" + System.currentTimeMillis() + ".jpg");
            cameraFile3.getParentFile().mkdirs();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri3 = Uri.fromFile(cameraFile3);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri3 = FileProvider.getUriForFile(SetRenZActivity.this, getPackageName() + ".fileprovider", cameraFile3);
            }
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);
            startActivityForResult(intent, TAKE_PHOTO3);
        }else if(type == 4){
            cameraFile4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/wanmo/" + System.currentTimeMillis() + ".jpg");
            cameraFile4.getParentFile().mkdirs();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri4 = Uri.fromFile(cameraFile4);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri4 = FileProvider.getUriForFile(SetRenZActivity.this, getPackageName() + ".fileprovider", cameraFile4);
            }
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri4);
            startActivityForResult(intent, TAKE_PHOTO4);
        }else if(type == 5){
            cameraFile5 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/wanmo/" + System.currentTimeMillis() + ".jpg");
            cameraFile5.getParentFile().mkdirs();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri5 = Uri.fromFile(cameraFile5);
            } else {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri5 = FileProvider.getUriForFile(SetRenZActivity.this, getPackageName() + ".fileprovider", cameraFile5);
            }
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
            startActivityForResult(intent, TAKE_PHOTO5);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*身份证 拍照*/
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        my_renz_zm.setVisibility(View.GONE);
                        zm_img.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath());
                        zm_img.setImageBitmap(bitmap);
                        /*压缩图片*/
                        File file = ImageFactory.compressImage(bitmap);
                        String imgUrl = Uri.fromFile(file).getPath();
                        cameraFile = new File(imgUrl);
                        subImage(cameraFile,1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                /*银行卡 拍照*/
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        my_renz_sc_bank.setVisibility(View.GONE);
                        bankImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(cameraFile2.getAbsolutePath());
                        bankImg.setImageBitmap(bitmap);
                        /*压缩图片*/
                        File file = ImageFactory.compressImage(bitmap);
                        String imgUrl = Uri.fromFile(file).getPath();
                        cameraFile2 = new File(imgUrl);
                        subImage(cameraFile2,2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            /*身份证 反面 拍照*/
            case TAKE_PHOTO3:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        my_renz_fm.setVisibility(View.GONE);
                        fm_img.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(cameraFile3.getAbsolutePath());
                        fm_img.setImageBitmap(bitmap);
                        /*压缩图片*/
                        File file = ImageFactory.compressImage(bitmap);
                        String imgUrl = Uri.fromFile(file).getPath();
                        cameraFile3 = new File(imgUrl);
                        subImage(cameraFile3,3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            /*手持 身份证 拍照*/
            case TAKE_PHOTO4:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        my_renz_sc.setVisibility(View.GONE);
                        scImg.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(cameraFile4.getAbsolutePath());
                        scImg.setImageBitmap(bitmap);
                        /*压缩图片*/
                        File file = ImageFactory.compressImage(bitmap);
                        String imgUrl = Uri.fromFile(file).getPath();
                        cameraFile4 = new File(imgUrl);
                        subImage(cameraFile4,4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            /*银行卡反 拍照*/
            case TAKE_PHOTO5:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        my_renz_bank02.setVisibility(View.GONE);
                        bank02_img.setVisibility(View.VISIBLE);
                        Bitmap bitmap = BitmapFactory.decodeFile(cameraFile5.getAbsolutePath());
                        bank02_img.setImageBitmap(bitmap);
                        /*压缩图片*/
                        File file = ImageFactory.compressImage(bitmap);
                        String imgUrl = Uri.fromFile(file).getPath();
//                        String imgUrl = Uri.fromFile(cameraFile5).getPath();
                        cameraFile5 = new File(imgUrl);
                        subImage(cameraFile5,5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void subImage(File file, final int type) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
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
                                        imgUrl = jsonObject.getString("url");
                                    } else if (type == 2) {
                                        imgUrl1 = jsonObject.getString("url");
                                    }else if (type == 3) {
                                        imgFmUrl1 = jsonObject.getString("url");
                                    }else if (type == 4) {
                                        imgScUrl1 = jsonObject.getString("url");
                                    }else if (type == 5) {
                                        imgBank2Url1 = jsonObject.getString("url");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            ToastUtil.show("失败了");
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
//                        ToastUtil.show(e + "");
                    }
                    @Override
                    public void parseNetworkFail(Call call, IOException e) {
                        super.parseNetworkFail(call, e);
                        customDialog.dismiss();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (setRenzTel.getText().toString().trim().length() == 11 && !setRenzName.getText().toString().trim().equals("")
                && setRenzIdent.getText().toString().trim().length() >= 1 && !setRenzBankNo.getText().toString().trim().equals("")) {//判断账号是否11位
            setRenzBut.setBackground(getResources().getDrawable(R.drawable.shape_button));
            setRenzBut.setTextColor(Color.parseColor("#ffffff"));
        } else {
            setRenzBut.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
            setRenzBut.setTextColor(Color.parseColor("#737679"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
