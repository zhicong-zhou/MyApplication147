package com.jzkl.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.jzkl.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class PictureUtils {

    private static int aspect_ratio_x = 3, aspect_ratio_y = 4;

    private PictureUtils() {
    }

    // 预览图片
    public static void preViewImg(Activity context, int themeId, int position, List<LocalMedia> list) {
        LocalMedia media = list.get(position);
        String pictureType = media.getPictureType();
        int mediaType = PictureMimeType.pictureToVideo(pictureType);
        switch (mediaType) {
            case 1:
                // 预览图片 可自定长按保存路径
                //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                PictureSelector.create(context)
                        .themeStyle(themeId)
                        .openExternalPreview(position, list);
                break;
            /*case 2:
                // 预览视频
                PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                break;
            case 3:
                // 预览音频
                PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                break;*/
        }
    }

    // 检查权限
    public static boolean isHasPer(Activity activity, String... strings) {
        if (strings != null && strings.length > 0) {
            for (String str : strings) {
                if (ContextCompat.checkSelfPermission(activity, str)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    // 申请权限
    public static void requestPer(final Activity activity, String... strings) {

        if (activity == null || strings == null || strings.length == 0) {
            return;
        }

        RxPermissions permissions = new RxPermissions(activity);
        permissions.request(strings).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(activity);
                } else {
                    Toast.makeText(activity,
                            activity.getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    // 处理返回结果
    public static <T> List<T> handleResult(int requestCode, int resultCode, Intent data) {

        List<T> selectList = null;

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = (List<T>) PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    break;
            }
        }

        return selectList;
    }

    // 创建
    public static void createPicture(Activity activity, List<LocalMedia> selectList, int chooseMode, int themeId, int minSelectNum,
                                     int maxSelectNum, int imageSpanCount, int isSingle, int forResult) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(minSelectNum)// 最小选择数量
                .imageSpanCount(imageSpanCount)// 每行显示个数
                .selectionMode(isSingle)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(true)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(forResult);//结果回调onActivityResult code

    }

}
