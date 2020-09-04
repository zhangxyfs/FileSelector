package com.z7dream.lib.selector.box.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.box.Box
import com.z7dream.lib.selector.box.MimeType
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.widget.SudokuLayout
import kotlinx.android.synthetic.main.activity_sudoku.*

/**
 * 九宫格页面(用于公司的其他文件的二级页面)
 */
class SudokuActivity : BoxBaseActivity(), SudokuLayout.ItemClickListener {
    private lateinit var mSpec: SelectionSpec

    override fun onCreate(savedInstanceState: Bundle?) {
        mSpec = SelectionSpec.getInstance()
        setTheme(mSpec.themeId)
        super.onCreate(savedInstanceState)
        if (!mSpec.hasInited) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        setContentView(R.layout.activity_sudoku)
        if (mSpec.needOrientationRestriction()) {
            requestedOrientation = mSpec.orientation
        }
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mSpec.titleName
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        sudoku.setOnItemClickListener(this)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    override fun onItemClickListener(mimeTypes: Set<MimeType>) {
        val companyId = SelectionSpec.getInstance().companyId
        val userId = SelectionSpec.getInstance().userId
        val onlyFileSizeMax = SelectionSpec.getInstance().simpleFileSizeMax
        val isToForward = SelectionSpec.getInstance().isNeedOpenForward
        val picMaxSize = SelectionSpec.getInstance().maxImageSelectable
        val fileMaxSize = SelectionSpec.getInstance().maxFileSelectable
        val allMaxSize = SelectionSpec.getInstance().maxSelectable
        val requestCode = SelectionSpec.getInstance().requestCode
        val orientation = SelectionSpec.getInstance().orientation
        val countable = SelectionSpec.getInstance().countable

        Box.from(this)
                .choose(mimeTypes)
                .setId(userId, companyId)
                .simpleFileSizeMax(onlyFileSizeMax)
                .isNeedForward(isToForward)
                .maxSelectablePerMediaType(picMaxSize, fileMaxSize)
                .maxSelectable(allMaxSize)
                .countable(countable)
                .restrictOrientation(orientation)
                .forResult(requestCode)

    }
}