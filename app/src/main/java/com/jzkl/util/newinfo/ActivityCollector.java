package com.jzkl.util.newinfo;

import android.app.Activity;

import com.jzkl.app.ApplicationDB;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by WangJinRui on 2018/3/13.
 */

public class ActivityCollector {
    private static final Set<Activity> activities = new HashSet<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void exitApp() {
        ApplicationDB.getInstance().unRegistReceiver();
        synchronized (activities) {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public static int getSize() {
        return activities.size();
    }

    public static void removeOtherAllActivity(Activity self) {
        synchronized (activities) {
            for (Activity activity : activities) {
                if (!activity.isFinishing() && !activity.equals(self)) {
                    activity.finish();
                }
            }
        }
    }
}
