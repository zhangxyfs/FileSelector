package com.z7dream.lib.selector.box.widget

import android.content.ClipData
import android.content.Context
import android.text.Html
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.utils.Utils
import com.z7dream.lib.selector.utils.FileType
import kotlinx.android.synthetic.main.media_content.view.*
import java.util.*
import java.util.regex.Pattern

class MediaLayout(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs), View.OnClickListener {
    private var mListener: OnMediaItemClickListener? = null
    private var mPreBindInfo: PreBindInfo? = null
    private var mMedia: ClipData.Item? = null

    init {
        init(context)
    }

    constructor(context: Context) : this(context, null)

    companion object {
        private val MATCH_STR = "(?i)"
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.media_content, this, true)
        fileLayout?.setOnClickListener(this)
        file_check_view?.setOnClickListener(this)

        picLayout?.setOnClickListener(this)
        pic_check_view?.setOnClickListener(this)
    }

    private fun bindPicMedia(info: PreBindInfo) {
        picLayout.visibility = View.VISIBLE
        fileLayout.visibility = View.GONE
        last_line.visibility = View.GONE

        picLayout.layoutParams.width = Utils.getScreenWidth(context) / 3
        picLayout.layoutParams.height = Utils.getScreenWidth(context) / 3


        pic_check_view.setCountable(info.mCheckViewCountable)
        if (info.openCheck) {
            pic_check_view?.visibility = View.VISIBLE
        } else {
            pic_check_view?.visibility = View.GONE
        }
        SelectionSpec.getInstance().imageEngine?.loadThumbnail(pic_thumbnail, mMedia?.filePath,
                picLayout.layoutParams.width, picLayout.layoutParams.width, 0)
        if (info.isCollection) {
            pic_collection?.visibility = View.VISIBLE
        } else {
            pic_collection?.visibility = View.GONE
        }
    }

    private fun bindFileMedia(info: PreBindInfo, item: ClipData.Item) {
        picLayout.visibility = View.GONE
        fileLayout.visibility = View.VISIBLE
        last_line.visibility = View.VISIBLE

        file_check_view.setCountable(info.mCheckViewCountable)
        if (info.openCheck) {
            file_check_view?.visibility = View.VISIBLE
            file_size?.visibility = View.GONE
        } else {
            file_check_view?.visibility = View.GONE
            file_size?.visibility = View.VISIBLE
        }
        if (info.mFileType == FileType.FOLDER) {
            file_check_view?.visibility = View.GONE
            file_size?.visibility = View.GONE
        }
        last_line?.visibility =
                if (info.isLastPos) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

        file_thumbnail.setImageResource(FileType.createIconResId(info.mFileType))
        if (info.isCollection) {
            type_collection?.visibility = View.VISIBLE
        } else {
            type_collection?.visibility = View.GONE
        }
        var titleName = ""
        if (info.mSearchKey.isEmpty()) {
            titleName = item.displayName
        } else {
            val sb = StringBuffer()
            val matcher = Pattern.compile(MATCH_STR + info.mSearchKey).matcher(item.displayName)
            while (matcher.find()) {
                matcher.appendReplacement(sb, "<font color='red'>" + matcher.group() + "</font>")//这样保证了原文的大小写没有发生变化
            }
            matcher.appendTail(sb)
            titleName = sb.toString()
        }
        file_title?.text = Html.fromHtml(titleName)


        date_time?.text = DateUtils.formatDate(item.dataTime * 1000, "yyyy-MM-dd HH:mm:ss")
        file_size?.text = SizeFormat.formatShortFileSize(getContext(), item.size).toUpperCase(Locale.getDefault())
    }

    override fun onClick(v: View) {
        val isFolder = mPreBindInfo?.mFileType == FileType.FOLDER
        if (mPreBindInfo?.openCheck == false) {
            mListener?.onThumbnailClicked(file_thumbnail, mMedia, mPreBindInfo?.mViewHolder, isFolder)
            return
        }
        val view: CheckView
        if (SelectionSpec.getInstance().onlyShowPic()) {
            view = pic_check_view
        } else {
            view = file_check_view
        }
        if ((v == picLayout || v == pic_check_view || v == fileLayout || v == file_check_view) && !isFolder) {
            mListener?.onCheckViewClicked(view, mMedia, mPreBindInfo?.mViewHolder)
        }
    }

    fun bindMedia(info: PreBindInfo, item: ClipData.Item) {
        mPreBindInfo = info
        mMedia = item
        if (SelectionSpec.getInstance().onlyShowPic()) {
            bindPicMedia(info)
        } else {
            bindFileMedia(info, item)
        }
    }


    fun setCheckedNum(checkedNum: Int) {
        if (SelectionSpec.getInstance().onlyShowPic()) {
            pic_check_view?.setCheckedNum(checkedNum)
        } else {
            file_check_view?.setCheckedNum(checkedNum)
        }
        isSelected = checkedNum > 0
    }

    fun setChecked(checked: Boolean) {
        if (SelectionSpec.getInstance().onlyShowPic()) {
            pic_check_view?.setChecked(checked)
        } else {
            file_check_view?.setChecked(checked)
        }
    }


    fun setOnMediaGridClickListener(listener: OnMediaItemClickListener) {
        mListener = listener
    }

    fun removeOnMediaGridClickListener() {
        mListener = null
    }


    interface OnMediaItemClickListener {
        fun onThumbnailClicked(thumbnail: ImageView?, item: ClipData.Item?, holder: RecyclerView.ViewHolder?, isFolder: Boolean)
        fun onCheckViewClicked(checkView: CheckView?, item: ClipData.Item?, holder: RecyclerView.ViewHolder?)
    }

    class PreBindInfo(var mFileType: Int, var isCollection: Boolean, var mCheckViewCountable: Boolean,
                      var openCheck: Boolean, var mViewHolder: RecyclerView.ViewHolder, var mSearchKey: String,
                      var isLastPos: Boolean)
}