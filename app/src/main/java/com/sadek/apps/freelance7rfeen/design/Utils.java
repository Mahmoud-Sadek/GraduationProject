package com.sadek.apps.freelance7rfeen.design;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static final String ATTRIBUTE_TTF_KEY = "ttf_name";

    public static final String ATTRIBUTE_SCHEMA = "http://schemas.android.com/apk/lib/com.sadek.apps.graduationproject.design";

    private static Map<String, Typeface> TYPEFACE = new HashMap<String, Typeface>();

    public static Typeface getFonts(Context context, String fontName) {
        Typeface typeface = TYPEFACE.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "font/"
                    + fontName);
            TYPEFACE.put(fontName, typeface);
        }
        return typeface;
    }

}
