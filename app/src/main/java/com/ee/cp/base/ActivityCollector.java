package com.ee.cp.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private static List<Activity> activityList = new ArrayList<Activity>();

    static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
