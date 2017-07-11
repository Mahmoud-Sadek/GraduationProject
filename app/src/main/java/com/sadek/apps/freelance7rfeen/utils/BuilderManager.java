package com.sadek.apps.freelance7rfeen.utils;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.Util;
import com.sadek.apps.freelance7rfeen.R;

/**
 * Created by Weiping Huang at 23:44 on 16/11/21
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */
public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.ic_action_favorite,
            R.drawable.ic_action_articles,
            android.R.drawable.ic_menu_call,
            android.R.drawable.ic_dialog_map
    };
    private static int[] textResources = new int[]{
            R.string.favorite,
            R.string.order,
            R.string.call,
            R.string.map
    };

    private static int imageResourceIndex = 0;
    private static int txtResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    static int gettxtResource() {
        if (txtResourceIndex >= textResources.length) txtResourceIndex = 0;
        return textResources[txtResourceIndex++];
    }

    public static TextInsideCircleButton.Builder getSquareTextInsideCircleButtonBuilder() {
        return new TextInsideCircleButton.Builder()
                .isRound(false)
                .shadowCornerRadius(Util.dp2px(10))
                .buttonCornerRadius(Util.dp2px(10))
                .normalImageRes(getImageResource())
                .normalTextRes(gettxtResource());
    }


    private static BuilderManager ourInstance = new BuilderManager();

    public static BuilderManager getInstance() {
        return ourInstance;
    }

    private BuilderManager() {
    }
}
