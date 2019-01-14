package com.jzkl.view.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaron on 2016/10/17.
 */

public class DotInfo {

    private String dotId;
    private double dotLat;
    private double dotLon;

    @Override
    public String toString() {
        return "DotInfo{" +
                "dotId='" + dotId + '\'' +
                ", dotLat=" + dotLat +
                ", dotLon=" + dotLon +
                '}';
    }

    public String getDotId() {
        return dotId;
    }

    public void setDotId(String dotId) {
        this.dotId = dotId;
    }

    public double getDotLat() {
        return dotLat;
    }

    public void setDotLat(double dotLat) {
        this.dotLat = dotLat;
    }

    public double getDotLon() {
        return dotLon;
    }

    public void setDotLon(double dotLon) {
        this.dotLon = dotLon;
    }

    /**
     * 初始化数据
     * @return
     */
    public static List<DotInfo> initData() {
        List<DotInfo> dotInfoList = new ArrayList<>();

        DotInfo dotInfo1 = new DotInfo();
        dotInfo1.setDotLat(37.88486);
        dotInfo1.setDotLon(112.503593);
        dotInfo1.setDotId("dotInfo1");
        dotInfoList.add(dotInfo1);

        DotInfo dotInfo2 = new DotInfo();
        dotInfo2.setDotLat(37.806898);
        dotInfo2.setDotLon(112.607078);
        dotInfo2.setDotId("dotInfo2");
        dotInfoList.add(dotInfo2);

        DotInfo dotInfo3 = new DotInfo();
        dotInfo3.setDotLat(37.808229);
        dotInfo3.setDotLon(112.551761);
        dotInfo3.setDotId("dotInfo3");
        dotInfoList.add(dotInfo3);

        DotInfo dotInfo4 = new DotInfo();
        dotInfo4.setDotLat(37.807118);
        dotInfo4.setDotLon(112.550898);
        dotInfo4.setDotId("dotInfo4");
        dotInfoList.add(dotInfo4);

        DotInfo dotInfo5 = new DotInfo();
        dotInfo5.setDotLat(37.807792);
        dotInfo5.setDotLon(112.549567);
        dotInfo5.setDotId("dotInfo5");
        dotInfoList.add(dotInfo5);

        DotInfo dotInfo6 = new DotInfo();
        dotInfo6.setDotLat(37.806766);
        dotInfo6.setDotLon(112.553741);
        dotInfo6.setDotId("dotInfo6");
        dotInfoList.add(dotInfo6);

        return dotInfoList;
    }
}
