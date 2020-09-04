package com.z7dream.lib.selector.box.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.box.MimeType
import com.z7dream.lib.selector.box.MimeTypeManager
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.utils.CacheManager
import com.z7dream.lib.selector.utils.Utils
import kotlinx.android.synthetic.main.sudoku_content.view.*
import java.io.File

class SudokuLayout(context: Context, attrs: AttributeSet?) : GridLayout(context, attrs),
    View.OnClickListener {
    private val layoutList = ArrayList<LinearLayout>()
    private var mItemClickListener: ItemClickListener? = null

    init {
        init(context)
        data()
    }

    constructor(context: Context) : this(context, null)

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.sudoku_content, this, true)
        layoutList.also {
            it.add(picLayout)
            it.add(audioLayout)
            it.add(videoLayout)
            it.add(txtLayout)
            it.add(excelLayout)
            it.add(pptLayout)
            it.add(wordLayout)
            it.add(pdfLayout)
            it.add(zipLayout)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun data() {
        for (view in layoutList) {
            setLayoutParamsSize(view)
            view.setOnClickListener(this)
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.PIC,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            picNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.VOICE,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            audioNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.VIDEO,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            videoNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.TXT,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            txtNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.EXCEL,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            excelNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.PPT,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            pptNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.WORD,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            wordNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.PDF,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            pdfNum.text = "(${it.list().size})"
        }
        File(
            CacheManager.getEsCompanyPath(context,
                CacheManager.OTHER,
                SelectionSpec.getInstance().companyId
            )
        ).also {
            zipNum.text = "(${it.list().size})"
        }
    }

    private fun setLayoutParamsSize(view: View) {
        view.layoutParams.width = Utils.getScreenWidth(view.context) / 3
        view.layoutParams.height = Utils.getScreenWidth(view.context) / 3
    }

    override fun onClick(p0: View?) {
        p0?.id?.also {
            when (it) {
                R.id.picLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofPic())
                }
                R.id.audioLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofAudio())
                }
                R.id.videoLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofVideo())
                }
                R.id.txtLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofTxt())
                }
                R.id.excelLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofExcel())
                }
                R.id.pptLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofPpt())
                }
                R.id.wordLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofWord())
                }
                R.id.pdfLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofPdf())
                }
                R.id.zipLayout -> {
                    mItemClickListener?.onItemClickListener(MimeTypeManager.ofZip())
                }
            }
        }
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        mItemClickListener = listener
    }

    interface ItemClickListener {
        fun onItemClickListener(mimeTypes: Set<MimeType>)
    }
}