package com.jzkl.Bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/9.
 */

public class MapBean {

    public MapBean() {

    }

    List<MapBean> lists = new ArrayList<>();
    List listSs = new ArrayList();

    private String mapAddr;
    private String mapDAddr;
    private String mapDis;
    private String star_level;
    private String img_url;
    private String person;
    private String content;
    private Double longitude;
    private Double latitude;

    public String getMapAddr() {
        return mapAddr;
    }

    public void setMapAddr(String mapAddr) {
        this.mapAddr = mapAddr;
    }

    public String getMapDAddr() {
        return mapDAddr;
    }

    public void setMapDAddr(String mapDAddr) {
        this.mapDAddr = mapDAddr;
    }

    public String getMapDis() {
        return mapDis;
    }

    public void setMapDis(String mapDis) {
        this.mapDis = mapDis;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getStar_level() {
        return star_level;
    }

    public void setStar_level(String star_level) {
        this.star_level = star_level;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MapBean(JSONObject json) {

        try {
            //成功有约 首页数据
            this.mapAddr = json.optString("title");
            this.mapDAddr = json.optString("area");
            this.mapDis = json.optString("range");
            this.img_url = json.optString("img_url");
            this.star_level = json.optString("star_level");
            this.person = json.optString("person");
            this.content = json.optString("content");

            this.longitude = json.optDouble("longitude");
            this.latitude = json.optDouble("latitude");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MapBean getJsonObj(JSONObject json, String objName) {
        String jName = json.optString(objName);
        MapBean bean = null;
        if (null != jName && !"".equals(jName)) {
            JSONObject jsonObj = json.optJSONObject(objName);
            bean = new MapBean(jsonObj);
        } else {
            bean = new MapBean();
        }
        return bean;
    }

    public static List<MapBean> getJsonArr(JSONObject json, String arrName) {
        List<MapBean> list = new ArrayList<MapBean>();
        String jName = json.optString(arrName);
        if (null != jName && !"".equals(jName)) {
            JSONArray array = json.optJSONArray(arrName);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.optJSONObject(i);
                    if (item != null) {
                        list.add(new MapBean(item));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return "MapBean{" +
                "mapAddr='" + mapAddr + '\'' +
                ", mapDAddr='" + mapDAddr + '\'' +
                ", mapDis='" + mapDis + '\'' +
                '}';
    }
}
