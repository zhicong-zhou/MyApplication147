package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.navisdk.adapter.impl.BaiduNaviManager;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.MapBean;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.view.map.NormalUtils;
import com.jzkl.view.map.newif.DemoGuideActivity;
import com.jzkl.view.test.BaseStripAdapter;
import com.jzkl.view.test.DotInfo;
import com.jzkl.view.test.StripListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity {

    protected ImmersionBar mImmersionBar;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BaiduMap mBaiduMap;
    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    Double latitude;
    Double longitude;
    String latitudeEnd,longitudeEnd;
    String long_lat;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.mapView_tts)
    TextView mapViewTts;

    private String mSDCardPath = null;
    private static final int authBaseRequestCode = 1;
    private boolean hasInitSuccess = false;
    private BNRoutePlanNode mStartNode = null;
    CustomDialog customDialog;
    private int time = 3;

    static Context mContext;
    private MyLocationConfiguration.LocationMode mCurrentMode;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            if (time == 0) {
                customDialog.dismiss();
                handler.removeMessages(0);
            }
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_map;
    }

    @Override
    protected void initData() {

        /*跟随*/
//        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        longitudeEnd = getIntent().getStringExtra("longitude");
        latitudeEnd = getIntent().getStringExtra("latitude");
        // 地图初始化
        commonTitle.setText("商家详情");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBaiduMap = mMapView.getMap();

        // 隐藏百度的LOGO
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mMapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 开启室内图
        mBaiduMap.setIndoorEnable(true);
        /*自定义定位icon*/
//        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.gps_dppic);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标 true有方向）
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration(config);

        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();

//        /*重新定位*/
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBaiduMap.setMyLocationEnabled(true);
//                mLocClient.registerLocationListener(myListener);
//                // 开启室内图
//                mBaiduMap.setIndoorEnable(true);
//                mLocClient.start();
//            }
//        });

        mFloorListAdapter = new BaseStripAdapter(MapActivity.this);
        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });

        //创建OverlayOptions的集合
//        for(int i=0;i<DotInfo.initData().size();i++){
//            DotInfo dotInfo1 = DotInfo.initData().get(i);
//            Latitude = dotInfo1.getDotLat();
//            Longitude = dotInfo1.getDotLon();
//            LatLng point = new LatLng(Latitude,Longitude);
//            //构建Marker图标
//            BitmapDescriptor bitmap = BitmapDescriptorFactory .fromResource(R.mipmap.gps_dppic);
//            //构建MarkerOption，用于在地图上添加Marker
//            MarkerOptions options = new MarkerOptions();
//            OverlayOptions oOptions = options.position(point) .icon(bitmap) .draggable(true);
//            //在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(oOptions);
//        }
        /*浮点*/
        getMarker();
        /*浮点 点击事件*/
        initListener();
        if (initDirs()) {
            initNavi();
        }
    }
    /*浮点*/
    private void getMarker() {
        List<DotInfo> dotInfoList = new ArrayList();
        DotInfo dotInfo = new DotInfo();
        dotInfo.setDotLat(Double.valueOf(latitudeEnd));
        dotInfo.setDotLon(Double.valueOf(longitudeEnd));
        dotInfoList.add(dotInfo);

        String marker = new Gson().toJson(dotInfoList);
        try {
            JSONArray array = new JSONArray(marker);
            for (int i = 0; i <array.length() ; i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                double Latitude = Double.parseDouble(jsonObject.getString("dotLat"));
                double Longitude = Double.parseDouble(jsonObject.getString("dotLon"));
                LatLng point = new LatLng(Latitude, Longitude);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.gps_dppic);
                //构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions();
                OverlayOptions oOptions = options.position(point).icon(bitmap).draggable(true);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(oOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        if (mBaiduMap != null) {
            /*浮点点击事件*/
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    customDialog = new CustomDialog(MapActivity.this, R.style.custom_dialog2);
                    customDialog.show();
                    /*倒计时加 圈 不然连续点会报错*/
                    handler.sendEmptyMessageDelayed(0, 1000);
                    /*跳转*/
                    LatLng position = marker.getPosition();
                    if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
                        routeplanToNavi(position, BNRoutePlanNode.CoordinateType.WGS84);
                    }
                    return false;
                }
            });
        }
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = MapActivity.this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, MapActivity.this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
            } else {
                doNext();
            }
        } else {
            doNext();
        }
    }

    private void doNext() {
        BaiduNaviManagerFactory.getBaiduNaviManager().init(MapActivity.this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(MapActivity.this, result, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(MapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(MapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(MapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    // 外置tts时需要实现的接口回调
    private IBNTTSManager.IBNOuterTTSPlayerCallback mTTSCallback = new IBNTTSManager.IBNOuterTTSPlayerCallback() {

        @Override
        public int getTTSState() {
//            /** 播放器空闲 */
//            int PLAYER_STATE_IDLE = 1;
//            /** 播放器正在播报 */
//            int PLAYER_STATE_PLAYING = 2;
            return PLAYER_STATE_IDLE;
        }

        @Override
        public int playTTSText(String text, String s1, int i, String s2) {
            Log.e("BNSDKDemo", "playTTSText:" + text);
            return 0;
        }

        @Override
        public void stopTTS() {
            Log.e("BNSDKDemo", "stopTTS");
        }
    };

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(MapActivity.this.getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);
        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("BNSDKDemo", "ttsCallback.onPlayError");
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.e("BNSDKDemo", "ttsHandler.msg.what=" + msg.what);
                    }
                }
        );
    }

    private void routeplanToNavi(LatLng position, final int coType) {

        if (!hasInitSuccess) {
            Toast.makeText(MapActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(longitude, latitude, "山西财经大学", "山西财经大学", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(position.longitude, position.latitude, "凯通大厦", "凯通大厦", coType);
        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(MapActivity.this, "算路开始", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(MapActivity.this, "算路成功", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(MapActivity.this, "算路失败", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(MapActivity.this, "算路成功准备进入导航", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(MapActivity.this,
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    /*=============================导航结束=========================================*/

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        private String lastFloor = null;

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();
            long_lat = longitude + "," + latitude;

            /*开启导航*/
            mapViewTts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog = new CustomDialog(MapActivity.this, R.style.CustomDialog);
                    customDialog.show();
                    /*倒计时加 圈 不然连续点会报错*/
                    handler.sendEmptyMessageDelayed(0, 1000);
                    getData();
                }
            });

            String bid = location.getBuildingID();
            if (bid != null && mMapBaseIndoorMapInfo != null) {
                Log.i("indoor", "bid = " + bid + " mid = " + mMapBaseIndoorMapInfo.getID());
                if (bid.equals(mMapBaseIndoorMapInfo.getID())) {// 校验是否满足室内定位模式开启条件
                    // Log.i("indoor","bid = mMapBaseIndoorMapInfo.getID()");
                    String floor = location.getFloor().toUpperCase();// 楼层
                    Log.i("indoor", "floor = " + floor + " position = " + mFloorListAdapter.getPosition(floor));
                    Log.i("indoor", "radius = " + location.getRadius() + " type = " + location.getNetworkLocationType());

                    boolean needUpdateFloor = true;
                    if (lastFloor == null) {
                        lastFloor = floor;
                    } else {
                        if (lastFloor.equals(floor)) {
                            needUpdateFloor = false;
                        } else {
                            lastFloor = floor;
                        }
                    }
                    if (needUpdateFloor) {// 切换楼层
                        mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                        mFloorListAdapter.setSelectedPostion(mFloorListAdapter.getPosition(floor));
                        mFloorListAdapter.notifyDataSetInvalidated();
                    }

                    if (!location.isIndoorLocMode()) {
                        mLocClient.startIndoorMode();// 开启室内定位模式，只有支持室内定位功能的定位SDK版本才能调用该接口
                        Log.i("indoor", "start indoormod");
                    }
                }
            }

            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    private void getData() {
        LatLng position = new LatLng(Double.parseDouble(latitudeEnd), Double.parseDouble(longitudeEnd));
        /*跳转*/
        if (BaiduNaviManagerFactory.getBaiduNaviManager().isInited()) {
            routeplanToNavi(position, BNRoutePlanNode.CoordinateType.WGS84);
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if (BaiduNaviManager.isNaviInited())
            BaiduNaviManager.getInstance().uninit();
        super.onDestroy();
    }
}
