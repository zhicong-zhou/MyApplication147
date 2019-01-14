package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.FullyGridLayoutManager;
import com.jzkl.util.GridImageAdapter;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.PhotoUtils;
import com.jzkl.util.PictureUtils;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.luck.picture.lib.tools.PictureFileUtils.getDiskCacheDir;

public class UserInfoActivity extends BaseActivity{

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.userinfo_recycler)
    ImageViewPlus headImg;
    @BindView(R.id.userinfo_arrow)
    ImageView userinfoArrow;
    @BindView(R.id.userinfo_rl)
    RelativeLayout userinfoRl;
    @BindView(R.id.userinfo_name)
    TextView userinfoName;
    @BindView(R.id.userinfo_userId)
    TextView userinfoUserId;
    @BindView(R.id.userinfo_tel)
    TextView userinfoTel;
    @BindView(R.id.userinfo_renz_status)
    TextView uRenz;
    @BindView(R.id.set_renz)
    RelativeLayout setRenz;
    @BindView(R.id.userinfo_business_status)
    TextView uBusiness;
    @BindView(R.id.userinfo_business)
    RelativeLayout userinfoBusiness;
    @BindView(R.id.userinfo_agent_status)
    TextView uAgent;
    @BindView(R.id.userinfo_agent)
    RelativeLayout userinfoAgent;

    @BindView(R.id.userinfo_jibie)
    TextView userinfo_jibie;
    @BindView(R.id.userinfo_rate)
    TextView userinfo_rate;


    private List strings;
    Intent intent;

    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userinfo,token,realnameStr,merchantStr,imgUrl;

    private  String cachPath;
    private  File cacheFile;
    private  File cameraFile;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int CROP_PHOTO = 3;


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
    @OnClick({R.id.common_back, R.id.userinfo_rl,R.id.set_renz,R.id.userinfo_business,R.id.userinfo_agent,R.id.common_text})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*头像*/
            case R.id.userinfo_rl:
                strings = new ArrayList<>();
                strings.add("拍照");
                strings.add("选择相册");
                DialogUIUtils.showBottomSheetAndCancel(this, strings, "取消", new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        switch (position) {
                            case 0:
                                AndPermission.with(UserInfoActivity.this).permission(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                                    @Override
                                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                        openCamera();
                                    }
                                    @Override
                                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                        Toast.makeText(UserInfoActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                                    }
                                }).start();
                                break;
                            case 1:
                                AndPermission.with(UserInfoActivity.this).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                                    @Override
                                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                        openAlbum();
                                    }
                                    @Override
                                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                                        Toast.makeText(UserInfoActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                                    }
                                }).start();
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onBottomBtnClick() {
                        super.onBottomBtnClick();
                    }
                }).show();
                break;
                /*实名认证*/
            case R.id.set_renz:
                intent = new Intent(this, SetRenZActivity.class);
                intent.putExtra("realnameStr",realnameStr);
                startActivity(intent);
                break;
                /*商户认证*/
            case R.id.userinfo_business:
                intent = new Intent(this, BusinessRenzActivity.class);
                intent.putExtra("merchantStr",merchantStr);
                startActivity(intent);
                break;
                /*代理*/
            case R.id.userinfo_agent:
                /*AgentActivity 不用了*/
                intent = new Intent(this, HomeAgentActivity.class);
                startActivity(intent);
                break;
                /*完成*/
            case R.id.common_text:
                finish();
                break;
        }
    }
    /*上传服务器*/
    private void getHeadImg(File imageUrl) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.img + Webcon.userServer)
                .params("file", imageUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            customDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    imgUrl = jsonObject.getString("url");
                                    getSubImg(imgUrl);
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
                        ToastUtil.show(e+"");
                    }
                });
    }
    /*上传修改*/
    private void getSubImg(String imageUrl) {
        final Map<String, String> map = new HashMap<>();
        map.put("url", imageUrl);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.userImg)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    ToastUtil.show("上传成功");
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
                        ToastUtil.show(e+"");
                    }
                });
    }
    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    JSONObject user = jsonObject.getJSONObject("user");
                                    realnameStr = user.getString("realnameStr");
                                    merchantStr = user.getString("merchantStr");
                                    String levelStr = user.getString("levelStr");

                                    uRenz.setText(realnameStr);
                                    uBusiness.setText(merchantStr);
                                    uAgent.setText(levelStr);
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setUserInfo(UserInfoActivity.this, s);
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
                        ToastUtil.show(e+"");
                    }
                });
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_info;
    }
    @Override
    protected void initData() {
        cachPath=getDiskCacheDir(this)+ "/handimg.jpg";//图片路径
        cacheFile =getCacheFile(new File(getDiskCacheDir(this)),"handimg.jpg");

        commonTitle.setText("个人资料");
        commonText.setVisibility(View.VISIBLE);
        commonText.setText("完成");
        getUser();
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
    private void openCamera() {
        cameraFile = getCacheFile(new File(getDiskCacheDir(this)),"output_image.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(cameraFile);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, getPackageName() + ".fileprovider", cameraFile);
        }
        // 启动相机程序
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    /**
     * 相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file, int size) {
        Log.i("TAG",getImageContentUri(this,file)+"裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this,file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 180);
            intent.putExtra("outputY", 180);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, CROP_PHOTO);
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

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        String imagePath= uriToPath(uri);
//        displayImage(imagePath); // 根据图片路径显示图片

        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
        startPhotoZoom(new File(imagePath),350);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
//        displayImage(imagePath);
        Log.i("TAG","file://"+imagePath+"选择图片的URI"+uri);
        startPhotoZoom(new File(imagePath),350);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String uriToPath(Uri uri) {
        String path=null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return  path;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            /*相机*/
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        startPhotoZoom(cameraFile,350);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                /*相册*/
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            /*身份证 裁剪*/
            case CROP_PHOTO:
                try {
                    if (resultCode==RESULT_OK){
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(cachPath))));
                        headImg.setImageBitmap(bitmap);
                        String imgUrl =  Uri.fromFile(cacheFile).getPath();
                        File file = new File(imgUrl);
                        getHeadImg(file);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }
    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            getUserInfo1();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo1() {
        userinfo = new SharedPreferencesUtil().getUserInfo(this);
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                JSONObject user = json.getJSONObject("user");
                String uName = user.getString("name");
                String userId = user.getString("userId");
                String mobile = user.getString("mobile");
                boolean flag = user.has("headimgUrl");
                String rate = user.getString("rate");
                String settfee = user.getString("settfee");
                String levelApp = user.getString("levelApp");
                String headimgUrl="";
                if(flag){
                    headimgUrl = user.getString("headimgUrl");
                }
                userinfoName.setText(uName);
                userinfoUserId.setText(userId);
                userinfoTel.setText(mobile);
                userinfo_jibie.setText(levelApp);
                userinfo_rate.setText(rate+"%+"+settfee);
                if(headimgUrl!=null){
                    if(headimgUrl.equals("null") || headimgUrl.equals("")){
                        headImg.setImageResource(R.mipmap.user_head);
                    }else {
                        com.jzkl.util.OkHttpUtils.picassoImage(headimgUrl,UserInfoActivity.this,headImg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*用户信息*/
        getUserInfo();
    }
}
