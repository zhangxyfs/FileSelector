package com.z7dream.lib.selector.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.z7dream.lib.selector.R;


/**
 * Created by Z7Dream on 2018/9/28 13:58.
 * Email:zhangxyfs@126.com
 */
public class SizeFormat {
    public static String formatFileSize(Context context, long number) {
        return formatFileSize(context, number, false);
    }

    public static String formatShortFileSize(Context context, long number) {
        return formatFileSize(context, number, true);
    }

    @SuppressLint({"DefaultLocale", "StringFormatInvalid"})
    private static String formatFileSize(Context context, long number, boolean shorter) {
        if (context == null) {
            return "";
        }

        float result = number;
        int suffix = R.string.size_byte;
        if (result > 900) {
            suffix = R.string.size_kbyte;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.size_mbyte;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.size_gbyte;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.size_tbype;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = R.string.size_pbypte;
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            if (shorter) {
                value = String.format("%.1f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else if (result < 100) {
            if (shorter) {
                value = String.format("%.0f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else {
            value = String.format("%.0f", result);
        }
        try {
            return context.getResources().
                    getString(R.string.fileSizeSuffix, value, context.getString(suffix));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
