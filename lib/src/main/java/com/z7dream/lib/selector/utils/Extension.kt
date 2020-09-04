package com.z7dream.lib.selector.utils

import android.text.TextUtils
import com.z7dream.lib.selector.R

/**
 * Created by Z7Dream on 2017/2/13 17:23.
 * Email:zhangxyfs@126.com
 */

object Extension {
    val PIC = arrayOf("jpg", "jpeg", "jpe", "bmp", "png")
    val TXT = arrayOf("txt")
    val EXCEL = arrayOf("xls", "xlt", "xlm", "xlsx")
    val PPT = arrayOf("dps", "dpt", "ppt", "pot", "pps", "pptx")
    val WORD = arrayOf("wps", "wpt", "doc", "dot", "rtf", "docx", "dotx")
    val PDF = arrayOf("pdf")

    val AUDIO = arrayOf("aac", "mp3", "mid", "wav", "flac", "amr", "m4a", "xmf", "ogg", "ape")
    val VIDEO = arrayOf("3gp", "mp4", "mkv", "ts", "rmvb", "avi")

    val ZIP = arrayOf("rar", "zip", "7z", "z", "iso", "gz", "tar", "cab", "ace", "apk")


    fun addLike(which: String, sb: StringBuilder, vararg strs: String) {
        for (i in strs.indices) {
            sb.append(which)
            sb.append(" LIKE ")
            sb.append("'%.").append(strs[i]).append("'")
            sb.append(" OR ")
        }
        sb.delete(sb.length - 4, sb.length)
    }

    fun addNotLike(which: String, sb: StringBuilder, vararg strs: String) {
        for (i in strs.indices) {
            sb.append(which)
            sb.append(" NOT LIKE ")
            sb.append("'%.").append(strs[i]).append("'")
            sb.append(" AND ")
        }
        sb.delete(sb.length - 4, sb.length)
    }

    fun getExcRes(exc: String): Int {
        val tmp = exc.toLowerCase()
        for (i in Extension.PIC.indices) {
            if (TextUtils.equals(tmp, Extension.PIC[i])) {
                return R.drawable.ic_file_pic
            }
        }
        for (i in Extension.TXT.indices) {
            if (TextUtils.equals(tmp, Extension.TXT[i])) {
                return R.drawable.ic_file_txt
            }
        }
        for (i in Extension.EXCEL.indices) {
            if (TextUtils.equals(tmp, Extension.EXCEL[i])) {
                return R.drawable.ic_file_excel
            }
        }
        for (i in Extension.PPT.indices) {
            if (TextUtils.equals(tmp, Extension.PPT[i])) {
                return R.drawable.ic_file_ppt
            }
        }
        for (i in Extension.WORD.indices) {
            if (TextUtils.equals(tmp, Extension.WORD[i])) {
                return R.drawable.ic_file_word
            }
        }
        for (i in Extension.PDF.indices) {
            if (TextUtils.equals(tmp, Extension.PDF[i])) {
                return R.drawable.ic_file_pdf
            }
        }
        for (i in Extension.AUDIO.indices) {
            if (TextUtils.equals(tmp, Extension.AUDIO[i])) {
                return R.drawable.ic_file_audio
            }
        }
        for (i in Extension.VIDEO.indices) {
            if (TextUtils.equals(tmp, Extension.VIDEO[i])) {
                return R.drawable.ic_file_video
            }
        }

        return R.drawable.ic_file_other
    }
}
