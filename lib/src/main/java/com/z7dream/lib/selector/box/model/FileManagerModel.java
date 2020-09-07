package com.z7dream.lib.selector.box.model;

import com.z7dream.lib.selector.utils.FileType;
import com.z7dream.lib.selector.utils.FileUtils;

import java.io.Serializable;

/**
 * Created by Z7Dream on 2017/3/29 16:35.
 * Email:zhangxyfs@126.com
 */

public class FileManagerModel implements Serializable {
    public String fileName;
    public String filePath;
    public int resId;

    ///////////////////////////
    //上传文件时的标记
    public String tag;
    //上传后的文件路径
    public String url;

    public String bucketName;
    public String toUploadAllPath;

    public FileManagerModel createModel(String filePath, int fileType) {
        fileName = FileUtils.getFolderName(filePath);
        this.filePath = filePath;
        resId = FileType.createIconResId(fileType);
        return this;
    }
}