package com.z7dream.lib.selector.box.model

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.entity.IncapableCause
import com.z7dream.lib.selector.box.entity.Item
import com.z7dream.lib.selector.box.utils.BoxUtils
import com.z7dream.lib.selector.box.utils.PathUtils
import com.z7dream.lib.selector.box.widget.CheckView

class SelectedItemCollection(private var context: Context) {
    private val STATE_SELECTION = "state_selection"
    private val STATE_COLLECTION_TYPE = "state_collection_type"
    private var mOnSelectionListener: OnSelectionListener? = null
    private lateinit var items: LinkedHashSet<Item>
    private var picItems: LinkedHashSet<Item>? = null
    private var audioItems: LinkedHashSet<Item>? = null
    private var videoItems: LinkedHashSet<Item>? = null
    private var txtItems: LinkedHashSet<Item>? = null
    private var excelItems: LinkedHashSet<Item>? = null
    private var pptItems: LinkedHashSet<Item>? = null
    private var wordItems: LinkedHashSet<Item>? = null
    private var pdfItems: LinkedHashSet<Item>? = null
    private var zipItems: LinkedHashSet<Item>? = null

    private var collectionType = COLLECTION_UNDEFINED
    private val spec: SelectionSpec = SelectionSpec.getInstance()

    /**
     * 根据混合选择模式，初始化图片与文件集合
     */
    private fun initImageOrFileItems() {
        if (spec.isTypeExclusive()) return
        items.forEach {
            addFileItem(it)
        }
    }

    private fun resetType() {
        if (items.size == 0) {
            collectionType = COLLECTION_UNDEFINED
        } else {
            if (collectionType == COLLECTION_MIXED) refineCollectionType()
        }
    }

    private fun currentMaxSelectableTips(item: Item?): Int {
        if (!spec.isTypeExclusive()) {
            if (item?.isPic() == true) {
                return R.string.error_over_image_count
            } else if (item?.isFile() == true) {
                return R.string.error_over_count
            }
        }

        return R.string.error_over_count
    }

    // depends
    private fun currentMaxSelectable(item: Item?): Int {
        if (item?.isFile() == true) {
            return spec.maxFileSelectable
        } else if (item?.isPic() == true) {
            return spec.maxImageSelectable
        }
//        if (!spec.isTypeExclusive()) {
//            if (item?.isFile() == true) {
//                return spec.maxFileSelectable
//            }
//        }
        return spec.maxSelectable
    }

    /**
     * 根据item集合数据设置collectionType
     */
    private fun refineCollectionType() {
        val hasPic = picItems != null && picItems?.size ?: 0 > 0
        val hasVoice = audioItems != null && audioItems?.size ?: 0 > 0
        val hasVideo = videoItems != null && videoItems?.size ?: 0 > 0
        val hasTxt = txtItems != null && txtItems?.size ?: 0 > 0
        val hasExcel = excelItems != null && excelItems?.size ?: 0 > 0
        val hasPpt = pptItems != null && pptItems?.size ?: 0 > 0
        val hasWord = wordItems != null && wordItems?.size ?: 0 > 0
        val hasPdf = pdfItems != null && pdfItems?.size ?: 0 > 0
        val hasZip = zipItems != null && zipItems?.size ?: 0 > 0
        val hasFile = hasVoice && hasVideo && hasTxt && hasExcel && hasPpt && hasWord && hasPdf
                && hasZip

        collectionType = if (hasFile) {
            COLLECTION_MIXED
        } else if (hasPic) {
            COLLECTION_PIC
        } else if (hasVoice) {
            COLLECTION_AUDIO
        } else if (hasVideo) {
            COLLECTION_VIDEO
        } else if (hasTxt) {
            COLLECTION_TXT
        } else if (hasExcel) {
            COLLECTION_EXCEL
        } else if (hasPpt) {
            COLLECTION_PPT
        } else if (hasWord) {
            COLLECTION_WORD
        } else if (hasPdf) {
            COLLECTION_PDF
        } else if (hasZip) {
            COLLECTION_ZIP
        } else {
            COLLECTION_UNDEFINED
        }
    }

    /**
     * Determine whether there will be conflict media types. A user can only select images and videos at the same time
     * while [SelectionSpec.mediaTypeExclusive] is set to false.
     */
    private fun typeConflict(item: Item?) =
        spec.isTypeExclusive()
                && ((item?.isAudio() == true && (isNeedCollectionType(COLLECTION_AUDIO) || collectionType == COLLECTION_MIXED))
                || (item?.isVideo() == true && (isNeedCollectionType(COLLECTION_VIDEO) || collectionType == COLLECTION_MIXED))
                || (item?.isTxt() == true && (isNeedCollectionType(COLLECTION_TXT) || collectionType == COLLECTION_MIXED))
                || (item?.isExcel() == true && (isNeedCollectionType(COLLECTION_EXCEL) || collectionType == COLLECTION_MIXED))
                || (item?.isPpt() == true && (isNeedCollectionType(COLLECTION_PPT) || collectionType == COLLECTION_MIXED))
                || (item?.isWord() == true && (isNeedCollectionType(COLLECTION_WORD) || collectionType == COLLECTION_MIXED))
                || (item?.isPdf() == true && (isNeedCollectionType(COLLECTION_PDF) || collectionType == COLLECTION_MIXED))
                || (item?.isZip() == true && (isNeedCollectionType(COLLECTION_ZIP) || collectionType == COLLECTION_MIXED))
                || (item?.isPic() == true && (isNeedCollectionType(COLLECTION_PIC) || collectionType == COLLECTION_MIXED)))

    private fun isNeedCollectionType(type: Int): Boolean = collectionType == type

    private fun addFileItem(item: Item) {
        if (item.isPic()) {
            if (picItems == null) {
                picItems = linkedSetOf()
            }
            picItems?.add(item)
        } else if (item.isAudio()) {
            if (audioItems == null) {
                audioItems = linkedSetOf()
            }
            audioItems?.add(item)
        } else if (item.isVideo()) {
            if (videoItems == null) {
                videoItems = linkedSetOf()
            }
            videoItems?.add(item)
        } else if (item.isTxt()) {
            if (txtItems == null) {
                txtItems = linkedSetOf()
            }
            txtItems?.add(item)
        } else if (item.isExcel()) {
            if (excelItems == null) {
                excelItems = linkedSetOf()
            }
            excelItems?.add(item)
        } else if (item.isPpt()) {
            if (pptItems == null) {
                pptItems = linkedSetOf()
            }
            pptItems?.add(item)
        } else if (item.isWord()) {
            if (wordItems == null) {
                wordItems = linkedSetOf()
            }
            wordItems?.add(item)
        } else if (item.isPdf()) {
            if (pdfItems == null) {
                pdfItems = linkedSetOf()
            }
            pdfItems?.add(item)
        } else if (item.isZip()) {
            if (zipItems == null) {
                zipItems = linkedSetOf()
            }
            zipItems?.add(item)
        }
    }

    private fun removeFileItem(item: Item) {
        if (item.isAudio()) {
            audioItems?.remove(item)
        } else if (item.isVideo()) {
            videoItems?.remove(item)
        } else if (item.isTxt()) {
            txtItems?.remove(item)
        } else if (item.isExcel()) {
            excelItems?.remove(item)
        } else if (item.isPpt()) {
            pptItems?.remove(item)
        } else if (item.isWord()) {
            wordItems?.remove(item)
        } else if (item.isPdf()) {
            pdfItems?.remove(item)
        } else if (item.isZip()) {
            zipItems?.remove(item)
        } else if (item.isPic()) {
            picItems?.remove(item)
        }
    }


    fun onCreate(bundle: Bundle?) {
        if (bundle == null) {
            items = linkedSetOf()
        } else {
            val saved = bundle.getParcelableArrayList<Item>(STATE_SELECTION)
            items = LinkedHashSet(saved!!)
            initImageOrFileItems()
            collectionType = bundle.getInt(STATE_COLLECTION_TYPE, COLLECTION_UNDEFINED)
        }
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(STATE_SELECTION, ArrayList(items))
        outState?.putInt(STATE_COLLECTION_TYPE, collectionType)
    }

    fun getDataWithBundle() = Bundle().run {
        putParcelableArrayList(STATE_SELECTION, ArrayList(items))
        putInt(STATE_COLLECTION_TYPE, collectionType)
        this
    }

    fun setDefaultSelection(uris: List<Item>) {
        items.addAll(uris)
    }

    fun overwrite(items: ArrayList<Item>, collectionType: Int) {
        this.collectionType = if (items.size == 0) COLLECTION_UNDEFINED else collectionType

        this.items.clear()
        this.items.addAll(items)
    }

    fun asList() = ArrayList(items)

    fun asListOfUri(): List<Uri> {
        val uris = arrayListOf<Uri>()
        for (item in items) {
            uris.add(item.getContentUri())
        }
        return uris
    }

    fun asListOfString(): List<String> {
        val paths = ArrayList<String>()
        items.forEach {
            val path = PathUtils.getPath(context, it.getContentUri())
            if (path != null) paths.add(path)
        }

        return paths
    }

    fun isAcceptable(item: Item?): IncapableCause? {
        if (maxSelectableReached(item)) {
            val maxSelectable = currentMaxSelectable(item)
            val maxSelectableTips = currentMaxSelectableTips(item)

            val cause = try {
                context.getString(maxSelectableTips, maxSelectable)
            } catch (e: Resources.NotFoundException) {
                context.getString(maxSelectableTips, maxSelectable)
            } catch (e: NoClassDefFoundError) {
                context.getString(maxSelectableTips, maxSelectable)
            }
            return IncapableCause(cause)
        } else if (typeConflict(item)) {
            return IncapableCause(context.getString(R.string.error_type_allType))
        }

        return BoxUtils.isAcceptable(context, item)
    }

    fun maxSelectableReached(item: Item?): Boolean {
        if (spec.onlyShowPic()) {
            return spec.maxImageSelectable == picItems?.size
        } else if (spec.onlyShowFile()) {
            var nowFileSize = 0
            audioItems?.size?.apply {
                nowFileSize += this
            }
            videoItems?.size?.apply {
                nowFileSize += this
            }
            txtItems?.size?.apply {
                nowFileSize += this
            }
            excelItems?.size?.apply {
                nowFileSize += this
            }
            pptItems?.size?.apply {
                nowFileSize += this
            }
            wordItems?.size?.apply {
                nowFileSize += this
            }
            pdfItems?.size?.apply {
                nowFileSize += this
            }
            zipItems?.size?.apply {
                nowFileSize += this
            }
            return spec.maxFileSelectable == nowFileSize
        } else if (spec.withShowFolder()) {
            var nowPicNum = 0
            var nowFileNum = 0
            for (it in items) {
                if (it.isPic()) {
                    nowPicNum += 1
                } else {
                    nowFileNum += 1
                }
            }
            if (nowFileNum + nowPicNum == spec.maxSelectable) {
                //所有已选择的文件为最大值
                return true
            }
            if (nowPicNum >= spec.maxImageSelectable && nowFileNum >= spec.maxFileSelectable) {
                //已选的图片达到最大值并且已选的文件达到最大值
                return true
            }
            if (nowPicNum >= spec.maxImageSelectable && item?.isPic() == true) {
                //已选的图片达到最大值
                return true
            }
            if (nowFileNum >= spec.maxFileSelectable && item?.isFile() == true) {
                //已选的文件达到最大值
                return true
            }
            return false
        }

        return spec.maxSelectable == items.size
    }

    fun getCollectionType() = collectionType

    fun isEmpty() = items.isEmpty()

    fun isSelected(item: Item?) = items.contains(item)

    fun count() = items.size

    fun items() = items.toList()

    /**
     * 注：
     * 此处取的是item在选中集合中的序号，
     * 所以不需区分混合选择或单独选择
     */
    fun checkedNumOf(item: Item?): Int {
        val index = ArrayList(items).indexOf(item)
        return if (index == -1) CheckView.UNCHECKED else index + 1
    }

    fun add(item: Item?): Boolean {
        if (typeConflict(item)) {
            throw IllegalArgumentException("Can't select images and videos at the same time.")
        }
        if (item == null) return false

        val added = items.add(item)
        addFileItem(item)
        if (added) {
            when (collectionType) {
                COLLECTION_UNDEFINED -> {
                    if (item.isAudio()) {
                        collectionType = COLLECTION_AUDIO
                    } else if (item.isVideo()) {
                        collectionType = COLLECTION_VIDEO
                    } else if (item.isTxt()) {
                        collectionType = COLLECTION_TXT
                    } else if (item.isExcel()) {
                        collectionType = COLLECTION_EXCEL
                    } else if (item.isPpt()) {
                        collectionType = COLLECTION_PPT
                    } else if (item.isWord()) {
                        collectionType = COLLECTION_WORD
                    } else if (item.isPdf()) {
                        collectionType = COLLECTION_PDF
                    } else if (item.isZip()) {
                        collectionType = COLLECTION_ZIP
                    } else if (item.isPic()) {
                        collectionType = COLLECTION_PIC
                    }
                }

                COLLECTION_AUDIO, COLLECTION_VIDEO, COLLECTION_TXT, COLLECTION_EXCEL, COLLECTION_PPT,
                COLLECTION_WORD, COLLECTION_PDF, COLLECTION_ZIP, COLLECTION_PIC -> {
                    if ((item.isAudio() && isNeedCollectionType(COLLECTION_AUDIO))
                        || item.isVideo() && isNeedCollectionType(COLLECTION_VIDEO)
                        || item.isTxt() && isNeedCollectionType(COLLECTION_TXT)
                        || item.isExcel() && isNeedCollectionType(COLLECTION_EXCEL)
                        || item.isPpt() && isNeedCollectionType(COLLECTION_PPT)
                        || item.isWord() && isNeedCollectionType(COLLECTION_WORD)
                        || item.isPdf() && isNeedCollectionType(COLLECTION_PDF)
                        || item.isZip() && isNeedCollectionType(COLLECTION_ZIP)
                        || item.isPic() && isNeedCollectionType(COLLECTION_PIC)
                    ) {
                        collectionType = COLLECTION_MIXED
                    }
                }
            }
        }
        mOnSelectionListener?.nowSelectCount(items.size)
        return added
    }

    fun remove(item: Item?): Boolean {
        if (item == null) return false
        val removed = items.remove(item)
        removeFileItem(item)
        if (removed) resetType()

        mOnSelectionListener?.nowSelectCount(items.size)
        return removed
    }

    fun removeAll() {
        items.clear()
        audioItems?.clear()
        videoItems?.clear()
        txtItems?.clear()
        excelItems?.clear()
        pptItems?.clear()
        wordItems?.clear()
        pdfItems?.clear()
        zipItems?.clear()
        picItems?.clear()
        resetType()

        mOnSelectionListener?.nowSelectCount(items.size)
    }

    fun setOnSelectionListener(listener: OnSelectionListener) {
        mOnSelectionListener = listener
    }

    interface OnSelectionListener {
        fun nowSelectCount(count: Int)
    }

    companion object {
        /**
         * Empty collection
         */
        val COLLECTION_UNDEFINED = 0x00

        /**
         * Collection only with audios
         */
        val COLLECTION_AUDIO = 0x01

        /**
         * Collection only with videos
         */
        val COLLECTION_VIDEO = 0x01 shl 1

        /**
         * Collection only with txts
         */
        val COLLECTION_TXT = 0x01 shl 2

        /**
         * Collection only with excels
         */
        val COLLECTION_EXCEL = 0X01 shl 3

        /**
         * Collection only with ppts
         */
        val COLLECTION_PPT = 0X01 shl 4

        /**
         * Collection only with words
         */
        val COLLECTION_WORD = 0X01 shl 5

        /**
         * Collection only with pdfs
         */
        val COLLECTION_PDF = 0X01 shl 6


        /**
         * Collection only with zips
         */
        val COLLECTION_ZIP = 0X01 shl 7

        /**
         * Collection only with pics
         */
        val COLLECTION_PIC = 0x01 shl 8

        /**
         * Collection with all
         */
        val COLLECTION_MIXED =
            COLLECTION_AUDIO or COLLECTION_VIDEO or COLLECTION_TXT or COLLECTION_EXCEL or
                    COLLECTION_PPT or COLLECTION_WORD or COLLECTION_PDF or COLLECTION_ZIP or COLLECTION_PIC
    }
}