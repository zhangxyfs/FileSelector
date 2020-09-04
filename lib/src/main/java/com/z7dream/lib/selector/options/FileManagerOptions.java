package com.z7dream.lib.selector.options;


import com.z7dream.lib.selector.FileManager;
import com.z7dream.lib.selector.utils.FileType;

import java.io.Serializable;

//文件管理属性
public class FileManagerOptions implements Serializable {
    //公司id
    public Long companyId = 0L;
    //起始位置
    public String startPath;
    //标题
    public String titleName;
    //文件类型
    public int fileType = FileType.ES_ALL;
    //最大图片数量
    public int picMaxNum = 9;
    //最大文件数量
    public int fileMaxNum = 9;
    //最大所有数量
    public int allMaxNum = 18;
    //最大文件大小
    public Long fileSizeMax = -1L;
    //类型
    public int fuction = 0;

    public boolean isDisplayPicIco;
    //是否开启转发
    public boolean isNeedOpenForward = false;
    //是否需要压缩
    public boolean isNeedZip = true;
    //是否需要裁剪
    public boolean isNeedCrop = false;
    //是否可以选择图片
    public boolean isCouldSelectPic = true;
    //是否有水印
    public boolean isHasWaterFilter = false;

    public String fileRxKey;

    public String picRxKey;

    public int requestCode = FileManager.FILE_MANAGER_REQUEST_CODE;
}
