package com.z7dream.lib.selector.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import java.io.File;

/**
 * 缓存目录控制
 * Created by xiaoyu.zhang on 2016/11/10 14:57
 *  
 */
public class CacheManager {
    public static final int DEFAULT = 0x0000;
    public static final int APK = 0x00100;
    public static final int VOICE = 0x00200;
    public static final int VIDEO = 0x00300;
    public static final int PIC = 0x00400;
    public static final int DB = 0x00500;
    public static final int CONFIG = 0x00600;
    public static final int CACHE = 0x00700;
    public static final int USED = 0x00800;
    public static final int GIFT = 0x00900;
    public static final int GLIDE = 0x01000;
    public static final int CRASH = 0x01100;
    public static final int SMILEY = 0x01200;
    public static final int FILE = 0x01300;
    public static final int RES = 0x01400;
    public static final int OSS = 0x01500;
    public static final int IM = 0x01600;
    public static final int ES = 0x01700;//下属文件夹命名-公司id-文件类型-
    public static final int EB_PHOTO = 0x01800;
    public static final int COLLECT = 0x01900;
    public static final int PIC_TEMP = 0x02000;
    public static final int OFF_LINE_CACHE = 0x02001;
    public static final int HTTP_CACHE = 0x02100;
    public static final int OFFLINE_ASS = 0x02200;
    public static final int OFFLINE_DICT = 0x02300;

    public static final int TXT = 0x01710;
    public static final int EXCEL = 0x01720;
    public static final int PPT = 0x01730;
    public static final int WORD = 0x01740;
    public static final int PDF = 0x01750;
    public static final int OTHER = 0x01760;
    public static final int ES_ALL = 0x01799;


    private static final String STR_APK = "apk";
    private static final String STR_VOICE = "voice";
    private static final String STR_VIDEO = "video";
    private static final String STR_PIC = "picture";
    private static final String STR_DB = "database";
    private static final String STR_CFG = "config";
    private static final String STR_RX = "rxCache";
    private static final String STR_USED = "used";
    private static final String STR_GIFT = "gift";
    private static final String STR_GLIDE = "glide";
    private static final String STR_CRASH = "crash";
    private static final String STR_SMILEY = "smiley";
    private static final String STR_FILE = "file";
    private static final String STR_RES = "res";
    private static final String STR_OSS = "oss_record";
    private static final String STR_IM = "im";
    private static final String STR_ES = "es";
    private static final String STR_TXT = "txt";
    private static final String STR_EXCEL = "excel";
    private static final String STR_PPT = "ppt";
    private static final String STR_WORD = "word";
    private static final String STR_PDF = "pdf";
    private static final String STR_OTHER = "other";
    private static final String STR_EB_PHOTO = "EB_photo";
    private static final String STR_COLLECT = "collect";
    private static final String STR_PIC_TEMP = "picTemp";
    private static final String STR_OFF_LINE_CACHE = "offlinecache";
    private static final String STR_OFFLINE_ASS = "offlineass";
    private static final String STR_OFFLINE_DICT = "offlinedict";
    private static final String STR_HTTP_CACHE = "httpCache";

    public static final String NOMEDIA = ".nomedia";


    /**
     * 生成下载文件保存路径
     *
     * @return
     */
    public static String getSaveFilePath() {
        File file = null;
        String rootPath = "";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            file = Environment.getExternalStorageDirectory();//获取跟目录
            rootPath = file.getPath();
        }
        return rootPath;
    }

    public static String getRelativePath(int which) {
        String savePath = getSaveFilePath() + File.separator + "com.eblog";// + File.separator + "cache";
        String relatviePath = File.separator + "com.eblog";// + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return getSavePath(relatviePath, which);
    }

    public static String getRelativePath(int which, String childFolderName) {
        String path = getRelativePath(which);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String needPath = path + childFolderName + File.separator;
        String nomediaPath = needPath + NOMEDIA;

        File fDir = new File(needPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        if (which != ES) {
            File npDir = new File(nomediaPath);
            if (!npDir.exists()) {
                npDir.mkdir();
            }
        }
        return needPath;
    }


    /**
     * 获取缓存路径
     */
    public static String getCachePath(Context context) {
        String savePath = getSaveFilePath() + File.separator + context.getPackageName();// + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getCachePath() {
        String savePath = getSaveFilePath() + File.separator + "com.eblog";// + File.separator + "cache";
        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return savePath;
    }

    public static String getCachePath(Context context, int which) {
        String savePath = "";
        if (context == null) {
            savePath = getCachePath();
        } else {
            savePath = getCachePath(context);
        }
        savePath = getSavePath(savePath, which);
        String nomediaPath = savePath + NOMEDIA;

        File fDir = new File(savePath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!savePath.contains(STR_EB_PHOTO) && !savePath.contains(STR_ES)) {
            if (!npDir.exists()) {
                npDir.mkdir();
            }
        } else {
            if (npDir.exists()) {
                npDir.delete();
            }
        }
        return savePath;
    }

    private static String getSavePath(String savePath, int which) {
        savePath += File.separator;
        if (which == APK) {
            savePath += STR_APK;
        } else if (which == VOICE) {
            savePath += STR_VOICE;
        } else if (which == VIDEO) {
            savePath += STR_VIDEO;
        } else if (which == PIC) {
            savePath += STR_PIC;
        } else if (which == DB) {
            savePath += STR_DB;
        } else if (which == CONFIG) {
            savePath += STR_CFG;
        } else if (which == CACHE) {
            savePath += STR_RX;
        } else if (which == USED) {
            savePath += STR_USED;
        } else if (which == GIFT) {
            savePath += STR_GIFT;
        } else if (which == GLIDE) {
            savePath += STR_GLIDE;
        } else if (which == CRASH) {
            savePath += STR_CRASH;
        } else if (which == SMILEY) {
            savePath += STR_SMILEY;
        } else if (which == FILE) {
            savePath += STR_FILE;
        } else if (which == RES) {
            savePath += STR_RES;
        } else if (which == OSS) {
            savePath += STR_OSS;
        } else if (which == IM) {
            savePath += STR_IM;
        } else if (which == ES) {
            savePath += STR_ES;
        } else if (which == TXT) {
            savePath += STR_TXT;
        } else if (which == EXCEL) {
            savePath += STR_EXCEL;
        } else if (which == PPT) {
            savePath += STR_PPT;
        } else if (which == WORD) {
            savePath += STR_WORD;
        } else if (which == PDF) {
            savePath += STR_PDF;
        } else if (which == OTHER) {
            savePath += STR_OTHER;
        } else if (which == ES_ALL) {
            savePath = savePath.substring(0, savePath.length() - 1);
        } else if (which == EB_PHOTO) {
            savePath += STR_EB_PHOTO;
        } else if (which == COLLECT) {
            savePath += STR_COLLECT;
        } else if (which == PIC_TEMP) {
            savePath += STR_PIC_TEMP;
        } else if (which == OFF_LINE_CACHE) {
            savePath += STR_OFF_LINE_CACHE;
        } else if (which == HTTP_CACHE) {
            savePath += STR_HTTP_CACHE;
        } else if (which == OFFLINE_ASS) {
            savePath += STR_OFFLINE_ASS;
        } else if (which == OFFLINE_DICT) {
            savePath += STR_OFFLINE_DICT;
        }
        savePath += File.separator;
        return savePath;
    }

    /**
     * 获取CacheManager中的文件类型
     *
     * @param filePath
     * @return
     */
    public static int getFileTypeFromExc(String filePath) {
        int fileType = CacheManager.OTHER;
        if (TextUtils.isEmpty(filePath)) {
            return fileType;
        } else {
            String realFilePath = filePath;
            if (filePath.endsWith("?")) {
                realFilePath = filePath.split("\\?")[0];
            }
            String exc = FileUtils.getExtensionName(realFilePath);
            if (FileType.isPic(exc)) {
                fileType = CacheManager.PIC;
            } else if (FileType.isAudio(exc)) {
                fileType = CacheManager.VOICE;
            } else if (FileType.isVideo(exc)) {
                fileType = CacheManager.VIDEO;
            } else if (FileType.isTxt(exc)) {
                fileType = CacheManager.TXT;
            } else if (FileType.isExcel(exc)) {
                fileType = CacheManager.EXCEL;
            } else if (FileType.isPpt(exc)) {
                fileType = CacheManager.PPT;
            } else if (FileType.isWord(exc)) {
                fileType = CacheManager.WORD;
            } else if (FileType.isPdf(exc)) {
                fileType = CacheManager.PDF;
            }
        }
        return fileType;
    }

    /**
     * 帮你创建个目录
     *
     * @param which           父目录
     * @param childFolderName 子目录名
     * @return
     */
    public static String getPath(Context context, int which, String childFolderName) {
        String path = getCachePath(context, which);
        String needPath = path + childFolderName + File.separator;
        String nomediaPath = needPath + NOMEDIA;

        File fDir = new File(needPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (!npDir.exists()) {
            if (which == ES) {
                npDir.delete();
            } else {
                npDir.mkdir();
            }
        }
        return needPath;
    }

    /**
     * 获得 es 下的目录
     *
     * @param which
     * @param companyId
     * @return
     */
    public static String getEsCompanyPath(Context context, int which, Long companyId) {
        String rootPath = getPath(context, ES, String.valueOf(companyId));
        if (rootPath.endsWith(File.separator)) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        rootPath = getSavePath(rootPath, which);
        String nomediaPath = rootPath + NOMEDIA;

        File fDir = new File(rootPath);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        File npDir = new File(nomediaPath);
        if (npDir.exists()) {
            npDir.delete();
        }

        return rootPath;
    }
}
