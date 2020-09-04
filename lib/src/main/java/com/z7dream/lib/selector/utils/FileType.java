package com.z7dream.lib.selector.utils;

import android.text.TextUtils;

import com.z7dream.lib.selector.R;


/**
 * Created by Z7Dream on 2017/2/13 11:42.
 * Email:zhangxyfs@126.com
 */

public class FileType {
    public static final int PIC = 1;
    public static final int AUDIO = 2;
    public static final int VIDEO = 3;
    public static final int TXT = 4;
    public static final int EXCEL = 5;
    public static final int PPT = 6;
    public static final int WORD = 7;
    public static final int PDF = 8;
    public static final int OTHER = 9;
    public static final int ES_ALL = 10;
    public static final int ALL = 11;
    public static final int FOLDER = 12;
    public static final int NONE = 500;

    public static final int SDCARD = 500;
    public static final int ES = 501;

    public static String[] getExc(int type) {
        String[] value = {};
        switch (type) {
            case PIC:
                value = Extension.INSTANCE.getPIC();
                break;
            case AUDIO:
                value = Extension.INSTANCE.getAUDIO();
                break;
            case VIDEO:
                value = Extension.INSTANCE.getVIDEO();
                break;
            case TXT:
                value = Extension.INSTANCE.getTXT();
                break;
            case EXCEL:
                value = Extension.INSTANCE.getEXCEL();
                break;
            case PPT:
                value = Extension.INSTANCE.getPPT();
                break;
            case WORD:
                value = Extension.INSTANCE.getWORD();
                break;
            case PDF:
                value = Extension.INSTANCE.getPDF();
                break;
            default:
                break;
        }
        return value;
    }

    public static boolean isPicFromPath(String path) {
        String exc = FileUtils.getExtensionName(path);
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getPIC().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPIC()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPic(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getPIC().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPIC()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAudio(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getAUDIO().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getAUDIO()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVoice(String exc){
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        if(TextUtils.equals("amr", exc)){
            return true;
        }
        return false;
    }

    public static boolean isVideo(String exc) {
        for (int i = 0; i < Extension.INSTANCE.getVIDEO().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getVIDEO()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTxt(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getTXT().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getTXT()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExcel(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getEXCEL().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getEXCEL()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPpt(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getPPT().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPPT()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWord(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getWORD().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getWORD()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPdf(String exc) {
        if(TextUtils.isEmpty(exc)){
            return false;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getPDF().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPDF()[i], exc)) {
                return true;
            }
        }
        return false;
    }

    public static int createFileType(String exc) {
        if(TextUtils.isEmpty(exc)){
            return FileType.FOLDER;
        }else {
            exc = exc.toLowerCase();
        }
        for (int i = 0; i < Extension.INSTANCE.getPIC().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPIC()[i], exc)) {
                return FileType.PIC;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getTXT().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getTXT()[i], exc)) {
                return FileType.TXT;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getEXCEL().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getEXCEL()[i], exc)) {
                return FileType.EXCEL;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getPPT().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPPT()[i], exc)) {
                return FileType.PPT;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getWORD().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getWORD()[i], exc)) {
                return FileType.WORD;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getPDF().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getPDF()[i], exc)) {
                return FileType.PDF;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getAUDIO().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getAUDIO()[i], exc)) {
                return FileType.AUDIO;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getVIDEO().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getVIDEO()[i], exc)) {
                return FileType.VIDEO;
            }
        }
        for (int i = 0; i < Extension.INSTANCE.getZIP().length; i++) {
            if (TextUtils.equals(Extension.INSTANCE.getZIP()[i], exc)) {
                return FileType.OTHER;
            }
        }
        return FileType.OTHER;
    }

    public static int createIconResId(int fileType) {
        int resId = R.drawable.ic_file_other;
        switch (fileType) {
            case FileType.PIC:
                resId = R.drawable.ic_file_pic;
                break;
            case FileType.AUDIO:
                resId = R.drawable.ic_file_audio;
                break;
            case FileType.VIDEO:
                resId = R.drawable.ic_file_video;
                break;
            case FileType.TXT:
                resId = R.drawable.ic_file_txt;
                break;
            case FileType.EXCEL:
                resId = R.drawable.ic_file_excel;
                break;
            case FileType.PPT:
                resId = R.drawable.ic_file_ppt;
                break;
            case FileType.WORD:
                resId = R.drawable.ic_file_word;
                break;
            case FileType.PDF:
                resId = R.drawable.ic_file_pdf;
                break;
            case FileType.OTHER:
                resId = R.drawable.ic_file_other;
                break;
            case FileType.FOLDER:
                resId = R.drawable.ic_file_folder;
                break;
            default:

                break;
        }
        return resId;
    }

    public static int getTypeFromResId(int resId) {
        int fileType = FileType.OTHER;
        if (resId == R.drawable.ic_file_pic) {
            fileType = FileType.PIC;
        } else if (resId == R.drawable.ic_file_audio) {
            fileType = FileType.AUDIO;
        } else if (resId == R.drawable.ic_file_video) {
            fileType = FileType.VIDEO;
        } else if (resId == R.drawable.ic_file_txt) {
            fileType = FileType.TXT;
        } else if (resId == R.drawable.ic_file_excel) {
            fileType = FileType.EXCEL;
        } else if (resId == R.drawable.ic_file_ppt) {
            fileType = FileType.PPT;
        } else if (resId == R.drawable.ic_file_word) {
            fileType = FileType.WORD;
        } else if (resId == R.drawable.ic_file_pdf) {
            fileType = FileType.PDF;
        } else if (resId == R.drawable.ic_file_other) {
            fileType = FileType.OTHER;
        } else if (resId == R.drawable.ic_file_folder) {
            fileType = FileType.FOLDER;
        }
        return fileType;
    }
}
