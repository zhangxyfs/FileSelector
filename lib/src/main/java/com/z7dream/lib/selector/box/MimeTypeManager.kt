package com.z7dream.lib.selector.box

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.collection.ArraySet
import com.z7dream.lib.selector.box.utils.PathUtils
import com.z7dream.lib.selector.utils.FileType
import java.util.*

object MimeTypeManager {
    fun ofAll(): EnumSet<MimeType> = EnumSet.allOf(MimeType::class.java)
    fun of(first: MimeType, others: Array<MimeType>): EnumSet<MimeType> =
            EnumSet.of(first, *others)

    /**
     * 文件类型
     */
    fun ofFile(): EnumSet<MimeType> = EnumSet.of(
            MimeType.JPEG, MimeType.JPE, MimeType.GIF, MimeType.BMP, MimeType.PNG, MimeType.WEBP,
            MimeType.AAC, MimeType.MID, MimeType.MID1, MimeType.WAV, MimeType.FLAC, MimeType.AMR,
            MimeType.M4A, MimeType.XMF, MimeType.OGG, MimeType.APE, MimeType._3GP, MimeType.MP4,
            MimeType.TS, MimeType.RMVB, MimeType.AVI, MimeType.MPEG1, MimeType.OGG1, MimeType.TXT,
            MimeType.XLX, MimeType.XLSX, MimeType.XLTX, MimeType.DPS, MimeType.DPT, MimeType.PPT,
            MimeType.PPTX, MimeType.WPS, MimeType.DOC, MimeType.RTF, MimeType.DOCX, MimeType.PDF,
            MimeType.RAR, MimeType.ZIP, MimeType._7Z, MimeType.Z, MimeType.ISO, MimeType.GZ,
            MimeType.TAR, MimeType.APK, MimeType.JSON
    )

    /**
     * 图片类型
     */
    fun ofPic(): EnumSet<MimeType> = EnumSet.of(
            MimeType.JPEG, MimeType.JPE, MimeType.GIF, MimeType.BMP, MimeType.PNG, MimeType.WEBP
    )

    /**
     * 音频类型
     */
    fun ofAudio(): EnumSet<MimeType> = EnumSet.of(
            MimeType.AAC, MimeType.MID, MimeType.MID1, MimeType.WAV, MimeType.FLAC, MimeType.AMR,
            MimeType.M4A, MimeType.XMF, MimeType.OGG, MimeType.APE
    )

    /**
     * 视频类型
     */
    fun ofVideo(): EnumSet<MimeType> = EnumSet.of(
            MimeType._3GP, MimeType.MP4, MimeType.TS, MimeType.RMVB, MimeType.AVI, MimeType.MPEG1,
            MimeType.OGG1
    )

    /**
     * 文本类型
     */
    fun ofTxt(): EnumSet<MimeType> = EnumSet.of(
            MimeType.TXT, MimeType.JSON
    )

    /**
     * 表单类型
     */
    fun ofExcel(): EnumSet<MimeType> = EnumSet.of(
            MimeType.XLX, MimeType.XLSX, MimeType.XLTX
    )

    /**
     * ppt类型
     */
    fun ofPpt(): EnumSet<MimeType> = EnumSet.of(
            MimeType.DPS, MimeType.DPT, MimeType.PPT, MimeType.PPTX
    )

    /**
     * 文档类型
     */
    fun ofWord(): EnumSet<MimeType> = EnumSet.of(
            MimeType.WPS, MimeType.DOC, MimeType.RTF, MimeType.DOCX
    )

    /**
     * pdf类型
     */
    fun ofPdf(): EnumSet<MimeType> = EnumSet.of(
            MimeType.PDF
    )

    /**
     * 压缩包类型
     */
    fun ofZip(): EnumSet<MimeType> = EnumSet.of(
            MimeType.RAR, MimeType.ZIP, MimeType._7Z, MimeType.Z, MimeType.ISO, MimeType.GZ, MimeType.TAR,
            MimeType.APK
    )

    /**
     * 文件夹类型
     */
    fun ofFolder(): EnumSet<MimeType> = EnumSet.of(
            MimeType.FOLDER
    )

    fun picType(): Array<String> {
        val array = Array(6) { "" }
        array[0] = MimeType.JPEG.getKey()
        array[1] = MimeType.JPE.getKey()
        array[2] = MimeType.GIF.getKey()
        array[3] = MimeType.BMP.getKey()
        array[4] = MimeType.PNG.getKey()
        array[5] = MimeType.WEBP.getKey()
        return array
    }

    fun audioType(): Array<String> {
        val array = Array(11) { "" }
        array[0] = MimeType.AAC.getKey()
        array[1] = MimeType.MP3.getKey()
        array[2] = MimeType.MID.getKey()
        array[3] = MimeType.MID1.getKey()
        array[4] = MimeType.WAV.getKey()
        array[5] = MimeType.FLAC.getKey()
        array[6] = MimeType.AMR.getKey()
        array[7] = MimeType.M4A.getKey()
        array[8] = MimeType.XMF.getKey()
        array[9] = MimeType.OGG.getKey()
        array[10] = MimeType.APE.getKey()
        return array
    }

    fun videoType(): Array<String> {
        val array = Array(8) { "" }
        array[0] = MimeType._3GP.getKey()
        array[1] = MimeType.MP4.getKey()
        array[2] = MimeType.MKV.getKey()
        array[3] = MimeType.TS.getKey()
        array[4] = MimeType.RMVB.getKey()
        array[5] = MimeType.AVI.getKey()
        array[6] = MimeType.MPEG1.getKey()
        array[7] = MimeType.OGG1.getKey()
        return array
    }

    fun txtType(): Array<String> {
        val array = Array(2) { "" }
        array[0] = MimeType.TXT.getKey()
        array[1] = MimeType.JSON.getKey()
        return array
    }

    fun excelType(): Array<String> {
        val array = Array(3) { "" }
        array[0] = MimeType.XLX.getKey()
        array[1] = MimeType.XLSX.getKey()
        array[2] = MimeType.XLTX.getKey()
        return array
    }

    fun pptType(): Array<String> {
        val array = Array(4) { "" }
        array[0] = MimeType.DPS.getKey()
        array[1] = MimeType.DPT.getKey()
        array[2] = MimeType.PPT.getKey()
        array[3] = MimeType.PPTX.getKey()
        return array
    }

    fun wordType(): Array<String> {
        val array = Array(4) { "" }
        array[0] = MimeType.WPS.getKey()
        array[1] = MimeType.DOC.getKey()
        array[2] = MimeType.RTF.getKey()
        array[3] = MimeType.DOCX.getKey()
        return array
    }

    fun pdfType() = Array(1) { MimeType.PDF.getKey() }

    fun zipType(): Array<String> {
        val array = Array(8) { "" }
        array[0] = MimeType.RAR.getKey()
        array[1] = MimeType.ZIP.getKey()
        array[2] = MimeType._7Z.getKey()
        array[3] = MimeType.Z.getKey()
        array[4] = MimeType.ISO.getKey()
        array[5] = MimeType.GZ.getKey()
        array[6] = MimeType.TAR.getKey()
        array[7] = MimeType.APK.getKey()
        return array
    }

    fun folderType(): Array<String> = Array(1) { MimeType.FOLDER.getKey() }

    /**
     * 是否图片
     */
    fun isPic(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.JPEG.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.JPE.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.GIF.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.BMP.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.PNG.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.WEBP.getKey())

    /**
     * 是否音频
     */
    fun isAudio(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.AAC.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MP3.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MID.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MID1.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.WAV.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.FLAC.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.AMR.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.M4A.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.XMF.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.OGG.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.APE.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.AUDIO.getKey())

    /**
     * 是否视频
     */
    fun isVideo(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType._3GP.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MP4.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MKV.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.TS.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.RMVB.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.AVI.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.MPEG1.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.OGG1.getKey())

    /**
     * 是否文本
     */
    fun isTxt(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.TXT.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.JSON.getKey())

    /**
     * 是否表单
     */
    fun isExcel(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.XLX.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.XLSX.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.XLTX.getKey())

    /**
     * 是否ppt
     */
    fun isPpt(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.DPS.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.DPT.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.PPT.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.PPTX.getKey())

    /**
     * 是否文档
     */
    fun isWord(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.WPS.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.DOC.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.RTF.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.DOCX.getKey())

    /**
     * 是否pdf
     */
    fun isPdf(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.PDF.getKey())

    /**
     * 是否压缩包
     */
    fun isZip(mimeType: String?) =
            lowerCaseMimeType(mimeType).contains(MimeType.RAR.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.ZIP.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType._7Z.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.Z.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.ISO.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.GZ.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.TAR.getKey())
                    || lowerCaseMimeType(mimeType).contains(MimeType.APK.getKey())

    /**
     * 是否文件夹
     */
    fun isFolder(mimeType: String?) = MimeType.FOLDER.getKey().contentEquals(lowerCaseMimeType(mimeType))

    /**
     * 类型转换
     */
    fun fromFileType(fileType: Int): Set<MimeType> {
        return when (fileType) {
            FileType.PIC -> ofPic()
            FileType.AUDIO -> ofAudio()
            FileType.VIDEO -> ofVideo()
            FileType.TXT -> ofTxt()
            FileType.EXCEL -> ofExcel()
            FileType.PPT -> ofPpt()
            FileType.WORD -> ofWord()
            FileType.PDF -> ofPdf()
            FileType.OTHER -> ofZip()
            else -> ofFolder()
        }
    }

    /**
     * 根据扩展名转mimetype
     */
    fun findTypeByFileExt(ext: String): MimeType {
        val picType = ofFile()
        for (type in picType) {
            for (it in type.getValue()){
                if(ext == it){
                    return type
                }
            }
        }
        return MimeType.ZIP
    }


    fun arraySetOf(vararg suffixes: String) = ArraySet(mutableListOf(*suffixes))

    /**
     * 验证类型
     */
    fun checkType(resolver: ContentResolver, uri: Uri?, mExtensions: Set<String>): Boolean {
        val map = MimeTypeMap.getSingleton()
        if (uri == null) return false

        val type = map.getExtensionFromMimeType(resolver.getType(uri))
        var path: String? = null
        // lazy load the path and prevent resolve for multiple times
        var pathParsed = false
        mExtensions.forEach {
            if (it == type) return true

            if (!pathParsed) {
                // we only resolve the path for one time
                path = PathUtils.getPath(resolver, uri)
                if (!path.isNullOrEmpty()) path = path?.toLowerCase(Locale.US)
                pathParsed = true
            }
            if (path != null && path?.endsWith(it) == true) return true
        }
        return false
    }

    private fun lowerCaseMimeType(mimeType: String?) = mimeType?.toLowerCase(Locale.getDefault())
            ?: ""
}