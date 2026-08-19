package com.farmerbb.taskbar.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import java.util.HashMap;
import java.util.Map;

public class FreeformWindowManager {
    private static final Map<String, Rect> savedBoundsMap = new HashMap<>();

    public static void saveWindowBounds(String packageName, Rect bounds) {
        if (packageName != null && bounds != null) {
            savedBoundsMap.put(packageName, bounds);
        }
    }

    public static Rect getSavedBounds(String packageName) {
        return savedBoundsMap.get(packageName);
    }

    public static void launchOrRestoreFreeform(Context context, Intent intent, Rect defaultBounds) {
        String pkg = intent.getComponent() != null ? intent.getComponent().getPackageName() : null;
        Rect bounds = (pkg != null && savedBoundsMap.containsKey(pkg)) ? savedBoundsMap.get(pkg) : defaultBounds;

        ActivityOptions options = ActivityOptions.makeBasic();
        if (bounds != null) {
            options.setLaunchBounds(bounds);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                      | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 
                      | Intent.FLAG_ACTIVITY_RESIZABLE_FOR_ALL_PROFILES);

        try {
            context.startActivity(intent, options.toBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
