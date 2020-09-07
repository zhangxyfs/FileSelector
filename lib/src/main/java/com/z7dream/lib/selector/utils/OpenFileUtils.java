package com.z7dream.lib.selector.utils;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.z7dream.lib.selector.utils.callback.Z7Callback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;


/**
 * Created by ziyouxingdong on 2017/5/17.
 */

public class OpenFileUtils {
    public static void openFile(Context context, String filePath) {
//        Intent intent = getFileIntent(filePath);

        try{
            Intent intent = getWordFileIntent1(context, filePath);
            if (intent != null)
                context.startActivity(intent);
            else Toast.makeText(context, "没有可以打开文件的软件", Toast.LENGTH_SHORT).show();
        }catch (ActivityNotFoundException a){
            Toast.makeText(context, "没有可以打开文件的软件", Toast.LENGTH_SHORT).show();
        }
    }


    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else if (end.equals("pptx") || end.equals("ppt")) {
            type = "application/vnd.ms-powerpoint";
        } else if (end.equals("docx") || end.equals("doc")) {
            type = "application/vnd.ms-word";
        } else if (end.equals("xlsx") || end.equals("xls")) {
            type = "application/vnd.ms-excel";
        } else if (end.equals("txt")) {
            type = "text/plain";
        } else if (end.equals("html") || end.equals("htm")) {
            type = "text/html";
        } else {
            //如果无法直接打开，就跳出软件列表给用户选择
            type = "*/*";
        }
        return type;
    }

    private static Intent getWordFileIntent1(Context context, String filePath) {
        if(TextUtils.isEmpty(filePath))
            return null;

        File file = new File(filePath);
        if (!file.exists())
            return null;
        String end = FileUtils.getExtensionName(filePath).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (FileType.isAudio(end)) {
            return getAudioFileIntent(filePath);
        } else if (FileType.isVideo(end)) {
            return getVideoFileIntent(filePath);
        } else if (FileType.isPic(end) || end.equals("gif")) {
            return getImageFileIntent(context, filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(context, filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(context, filePath);
        } else if (FileType.isExcel(end) || FileType.isPdf(end) || FileType.isPpt(end) || FileType.isTxt(end)
                || FileType.isWord(end)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            String type = getMIMEType(file);
            if (type.contains("pdf") || type.contains("vnd.ms-powerpoint") ||
                    type.contains("vnd.ms-word") || type.contains("vnd.ms-excel") || type.contains("text/plain") || type.contains("text/html")) {
                if (WPSUtils.isHasWPS(context)) {
                    intent.setClassName("cn.wps.moffice_eng",
                            "cn.wps.moffice.documentmanager.PreStartActivity2");
                    intent.setData(uri);
                } else {
                    return getAllIntent(context, filePath);
                }
            } else {
                return getAllIntent(context, filePath);
            }
            return intent;
        }
        return getAllIntent(context, filePath);
    }


    /**
     * @param filePath
     * @return
     */
    private static Intent getFileIntent(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        // XXX Sigi edited
        /* 取得扩展名 */
        String end = FileUtils.getExtensionName(filePath).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (FileType.isAudio(end)) {
            return getAudioFileIntent(filePath);
        } else if (FileType.isVideo(end)) {
            return getVideoFileIntent(filePath);
        } else if (FileType.isPic(end) || end.equals("gif")) {
            return getImageFileIntent(context, filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(context, filePath);
        } else if (end.equals("ppt") || (end.equals("pptx") || (end.endsWith(".zip")) || (end.endsWith(".rar")))) {
            if (WPSUtils.isHasWPS(context)) {
                return getWPSFileIntent(context, filePath);
            } else {
                return getAllIntent(context, filePath);
            }
        } else if (end.equals("xls") || (end.equals("xlsx"))) {
            if (WPSUtils.isHasWPS(context)) {
                return getWPSFileIntent(context, filePath);
            } else {
                return getAllIntent(context, filePath);
            }
        } else if (end.equals("doc") || (end.equals("docx"))) {
            if (WPSUtils.isHasWPS(context)) {
                return getWPSFileIntent(context, filePath);
            } else {
                return getAllIntent(context, filePath);
            }
        } else if (end.equals("pdf")) {
            if (WPSUtils.isHasWPS(context)) {
                return getWPSFileIntent(context, filePath);
            } else {
                return getAllIntent(context, filePath);
            }
        } else if (end.equals("chm")) {
            return getChmFileIntent(context, filePath);
        } else if (end.equals("txt")) {
            if (WPSUtils.isHasWPS(context)) {
                return getWPSFileIntent(context, filePath);
            }
            return getTextFileIntent(context, filePath, false);
        } else {
            return getAllIntent(context, filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        //        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个WPS的intent
    public static Intent getWPSFileIntent(Context context, String param) {
        IntentFilter localIntentFilter1 = new IntentFilter("cn.wps.moffice.file.close");
        localIntentFilter1.addAction("cn.wps.moffice.file.save");
        context.registerReceiver(mCloseReceiver, localIntentFilter1);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "ReadOnly");
        bundle.putBoolean("SendCloseBroad", true);
        bundle.putBoolean("SendSaveBroad", true);
        bundle.putString("SavePath", getWpsDir());
        bundle.putString("ThirdPackage", context.getPackageName());
        bundle.putBoolean("ClearBuffer", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setData(uri);
        intent.putExtras(bundle);
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }


    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(Context context, String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (paramBoolean) {
            uri = Uri.parse(param);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider7.getUriForFile(context, new File(param));
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Uri uri = FileProvider7.getUriForFile(context, new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static BroadcastReceiver mCloseReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            try {
                Bundle localBundle = paramIntent.getExtras();
                String closefile = localBundle.getString("CloseFile");

                String newFilePath = getWpsDir() + closefile.substring(closefile.lastIndexOf("/") + 1, closefile.length());
                copyFile(new File(closefile), new File(newFilePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static String getWpsDir() {
        return CacheManager.getCachePath(null, CacheManager.WORD);
    }

    public static void copyFile(File sourceFile, File targetFile) {
        try {
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);

            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception e) {
        }
    }

    public static void openFile(Context context, String filePath, Z7Callback.Callback<String> callback) {
        String finishExc = FileUtils.getExtensionName(filePath);
        int type = FileType.createFileType(finishExc);
        switch (type) {
            case FileType.TXT:
            case FileType.EXCEL:
            case FileType.PPT:
            case FileType.WORD:
            case FileType.PDF:
                WPSUtils.openWpsFile(context, filePath);
                break;
            case FileType.AUDIO:
                callback.event(filePath);
                break;
            default:
                openFile(context, filePath);
                break;
        }
    }
}
