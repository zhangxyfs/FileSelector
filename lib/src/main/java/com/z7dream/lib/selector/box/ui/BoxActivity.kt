package com.z7dream.lib.selector.box.ui

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.z7dream.lib.selector.box.ui.FileDatabaseSelectionFragment.Companion.MEDIA_FRAGMENT_LIST
import com.z7dream.lib.selector.box.ui.FileDatabaseSelectionFragment.Companion.MEDIA_FRAGMENT_SEARCH
import com.eblog.base.widget.box.ui.FolderSelectionFragment
import com.eblog.base.widget.box.ui.FolderSelectionFragment.Companion.FOLDER_FRAGMENT_LIST
import com.eblog.base.widget.box.ui.FolderSelectionFragment.Companion.FOLDER_FRAGMENT_SEARCH
import com.z7dream.lib.selector.box.ui.StarSelectionFrament.Companion.STAR_FRAGMENT_LIST
import com.z7dream.lib.selector.box.ui.StarSelectionFrament.Companion.STAR_FRAGMENT_SEARCH
import com.eblog.base.widget.box.ui.adapter.FileAlbumAdapter
import com.eblog.base.widget.box.ui.view.FragmentImpl
import com.z7dream.lib.selector.box.ui.view.SelectionProvider
import com.z7dream.lib.selector.R
import com.z7dream.lib.selector.box.SelectionSpec
import com.z7dream.lib.selector.box.model.SelectedItemCollection
import com.z7dream.lib.selector.utils.CacheManager
import com.z7dream.lib.selector.utils.OpenFileUtils
import java.io.File

/**
 * 文件管理器
 */
class BoxActivity : BoxBaseActivity(), SelectionProvider,
        FileAlbumAdapter.OnMediaClickListener, FileAlbumAdapter.CheckStateListener, View.OnClickListener,
        Toolbar.OnMenuItemClickListener, FileManagerDialog.OnFileSendListener,
        FileManagerDialog.OnFileDeleteListener, FileManagerDialog.OnFileRenameListener,
        FileManagerDialog.OnFileStarListener, FileManagerDialog.OnShareListener,
        SelectedItemCollection.OnSelectionListener, TextView.OnEditorActionListener,
        TextWatcher {
    private lateinit var mSpec: SelectionSpec
    private lateinit var mFileManagerDialog: FileManagerDialog
    private lateinit var mInputManager: InputMethodManager
    private lateinit var mToolbar: Toolbar
    private val mSelectedCollection = SelectedItemCollection(this)
    private var choiceItem: MenuItem? = null
    private var isOpenCheck: Boolean = false
    private var isClickSelectAll: Boolean = false

    private var mHasDataMap = HashMap<String, Boolean>()
    private var mFragmentImpl: FragmentImpl? = null

    companion object {
        private val ROOT_PATH = File.separator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mSpec = SelectionSpec.getInstance()
        setTheme(mSpec.themeId)
        super.onCreate(savedInstanceState)
        if (!mSpec.hasInited) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        setContentView(R.layout.activity_box)

        if (mSpec.needOrientationRestriction()) {
            requestedOrientation = mSpec.orientation
        }

        initToolbar()
        mSelectedCollection.onCreate(savedInstanceState)
        mSelectedCollection.setOnSelectionListener(this)

        initDialog()
        initView()
        switchToList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mSelectedCollection.onSaveInstanceState(outState)
    }

    override fun nowSelectCount(count: Int) {
        if (!isOpenCheck) {
            return
        }
       if(count == mFragmentImpl?.getCursorCount()
               || count == mSpec.maxImageSelectable && mSpec.onlyShowPic()
               || count == mSpec.maxFileSelectable){
           choiceItem?.setTitle(R.string.clear_select_all_str)
           isClickSelectAll = true
       }else{
           choiceItem?.setTitle(R.string.select_all_str)
           isClickSelectAll = false
       }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (!isOpenCheck) {
            choiceItem?.setTitle(R.string.select_all_str)
            mFragmentImpl?.openCheck(true)
            isOpenCheck = true
            mFileManagerDialog.showPopup()
        } else {
            if (!isClickSelectAll) {
                //当前为全选, 需要改成全不选
                choiceItem?.setTitle(R.string.clear_select_all_str)
                mFragmentImpl?.selectAll()
                isClickSelectAll = true
            } else {
                choiceItem?.setTitle(R.string.select_all_str)
                mFragmentImpl?.clearSelectAll(true)
                isClickSelectAll = false
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.page_menu, menu)
        choiceItem = menu.findItem(R.id.firstBtn)
        val secItem = menu.findItem(R.id.secondBtn)
        choiceItem?.setTitle(R.string.choice_str)
        secItem.isVisible = false
        choiceItem?.isVisible = mHasDataMap[getFirstFragmentTag()] ?: false
        return true
    }

    override fun finish() {
        mFileManagerDialog.destory()
        super.finish()
    }

    override fun provideSelectedItemCollection(): SelectedItemCollection = mSelectedCollection

    override fun loadDataSucc(hasData: Boolean) {
        mHasDataMap[getFirstFragmentTag()] = hasData

        if (hasData) {
            choiceItem?.isVisible = hasData
            swipeRefresh.visibility = View.VISIBLE
            empty_view.visibility = View.GONE
        } else {
            swipeRefresh.visibility = View.GONE
            empty_view.visibility = View.VISIBLE
        }
        swipeRefresh?.isRefreshing = false
        swipeRefresh?.isEnabled = false
    }

    override fun onSelectUpdate() {
        mSelectedCollection.items().also {
            updateDialogRenameStatus(it)
            updateDialogShareStatus(it)
            updateDialogStarStatus(it)
            updateDialogChoiceStatus(it)
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            getSearchEditView()?.apply {
                val searchKey = text.toString().trim()
                if (searchKey.isEmpty()) {
                    return false
                }
                mInputManager.hideSoftInputFromWindow(windowToken, 0)
                if (mSpec.onlyShowFile()) {
                    getMediaDBFragmentByName(MEDIA_FRAGMENT_SEARCH)?.searchByKeyword(searchKey)
                } else if (mSpec.withShowFolder()) {
                    getFolderFragmentByName(FOLDER_FRAGMENT_SEARCH)?.searchByKeyword(searchKey)
                }
            }
        }
        return false
    }

    override fun afterTextChanged(s: Editable?) {
        if (isOpenCheck) {
            closeCheck()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null) return
        findViewById<ImageView>(R.id.search_close)?.visibility =
                if (s.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
    }

    override fun onMediaClick(item: Item?, adapterPosition: Int, isFolder: Boolean) {
        when {
            item?.isPic() == true -> {
                FileManager.instance.nextPicDisplay()
                        .setMaxNum(1)
                        .setDefPos(0)
                        .setOnlyDisplay(true)
                        .setDisplayDel(true)
                        .setDisplayList(item.filePath)
                        .build(this)
            }
            else -> {
                OpenFileUtils.openFile(item?.filePath)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchLayout -> {
                closeCheck()
                openSearchFragment()
            }
            R.id.search_edit -> {
                closeCheck()
            }
        }
    }

    override fun onFileSendClick(view: View?) {
        if (isNotSelectFile() || mFragmentImpl?.resetSelectItems() == true) {
            return
        }
        if (mSpec.isNeedOpenForward) {
            mFragmentImpl?.forwardItemsToChat(this)
        } else {
            mFragmentImpl?.forwardItems(this)
        }
    }

    override fun onFileDeleteClick(view: View?) {
        if (isNotSelectFile()) {
            return
        }
        mFragmentImpl?.removeFiles()
    }

    override fun onFileRenameClick(view: View?) {
        if (isNotSelectFile()) {
            return
        }
        mFragmentImpl?.rename(this)
    }

    override fun onFileStarClick(view: View?, b: Boolean) {
        if (isNotSelectFile()) {
            return
        }
        mFragmentImpl?.starFile(b)
    }

    override fun onShareClick(view: View?) {
        if (isNotSelectFile() || mFragmentImpl?.resetSelectItems() == true) {
            return
        }
        mFragmentImpl?.share()
    }

    override fun onBackPressed() {
        if (isOpenCheck) {
            closeCheck()
        } else if (getSearchBtnView().visibility == View.GONE && !mSpec.onlyShowPic()) {
            closeSearchFragment()
        } else if (mFragmentImpl?.toBackParentFolder() == true) {
            return
        } else {
            setResult(Activity.RESULT_CANCELED)
            super.onBackPressed()
        }
    }

    /**
     * 更新底部导航重命名状态
     */
    private fun updateDialogRenameStatus(it: List<Item>) {
        mFileManagerDialog.setRenameEnable(it.size == 1)
    }

    /**
     * 更新底部导航分享状态
     */
    private fun updateDialogShareStatus(it: List<Item>) {
        mFileManagerDialog.setShareEnable(it.size == 1)
    }

    /**
     * 更新底部导航星标状态
     */
    private fun updateDialogStarStatus(it: List<Item>) {
        if (it.isEmpty()) {
            mFileManagerDialog.setStarState(true)
        } else if (mSpec.isStarType()) {
            mFileManagerDialog.setStarState(false)
        } else {
            var isAllStar = true
            for (item in it) {
                mFragmentImpl?.getStarMap()?.also {
                    if (it[item.filePath] == null) {
                        isAllStar = false
                    }
                }
                if (!isAllStar) {
                    break
                }
            }
            mFileManagerDialog.setStarState(!isAllStar)
        }
    }

    /**
     * 更新上方选择按钮状态
     */
    private fun updateDialogChoiceStatus(it: List<Item>) {
        nowSelectCount(it.size)
    }

    private fun setFocus() {
        getSearchEditView().isFocusable = true;
        getSearchEditView().isFocusableInTouchMode = true;
        getSearchEditView().requestFocus();
    }

    private fun switchInput(boolean: Boolean) {
        if (boolean) {
            mInputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            mInputManager.hideSoftInputFromWindow(getSearchEditView().windowToken, 0)
        }
    }

    /**
     * 打开搜索
     */
    private fun openSearchFragment() {
        switchToSearch()
        getSearchBtnView().visibility = View.GONE
        getSearchLayoutView().visibility = View.VISIBLE
        mToolbar.setBackgroundResource(R.drawable.bg_background_100dp)
        supportActionBar?.title = ""
        choiceItem?.isVisible = false
        setFocus()
        switchInput(true)
    }

    /**
     * 关闭多选
     */
    private fun closeCheck() {
        isOpenCheck = false
        isClickSelectAll = false
        choiceItem?.setTitle(R.string.choice_str)
        mFragmentImpl?.openCheck(isOpenCheck)
        mFragmentImpl?.clearSelectAll(true)
        mFileManagerDialog.dismiss()
    }

    /**
     * 关闭搜索
     */
    private fun closeSearchFragment() {
        switchToList()
        getSearchBtnView().visibility = View.VISIBLE
        getSearchLayoutView().visibility = View.GONE
        supportActionBar?.title = mSpec.titleName
        mToolbar.background = null
        switchInput(false)
    }

    private fun isNotSelectFile(): Boolean {
        if (mSelectedCollection.items().isEmpty()) {
            showToast(R.string.please_choose_afile_str)
            return true
        }
        return false
    }

    private fun getSearchBtnView() = findViewById<LinearLayout>(R.id.searchLayout)

    private fun getSearchLayoutView() = findViewById<RelativeLayout>(R.id.content_title)

    private fun getSearchEditView() = findViewById<EditText>(R.id.search_edit)

    private fun initToolbar() {
        mInputManager = Appli.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mSpec.titleName
        mToolbar.setOnMenuItemClickListener(this)
        mToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        mToolbar.background = null
        val navigationIcon = mToolbar.navigationIcon
        val ta = theme.obtainStyledAttributes(intArrayOf(R.attr.album_element_color))
        val color = ta.getColor(0, 0)
        ta.recycle()
        navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    private fun initDialog() {
        mFileManagerDialog = FileManagerDialog(this, 0)
        mFileManagerDialog.setRenameEnable(false)
        mFileManagerDialog.setShareEnable(false)
        mFileManagerDialog.setOnFileDeleteListener(this)
        mFileManagerDialog.setOnFileRenameListener(this)
        mFileManagerDialog.setOnFileSendListener(this)
        mFileManagerDialog.setOnFileStarListener(this)
        mFileManagerDialog.setOnShareListener(this)
    }

    private fun initView() {
        getSearchLayoutView()?.visibility = View.GONE
        if (mSpec.onlyShowPic()) {
            getSearchBtnView().visibility = View.GONE
        }
        swipeRefresh.visibility = View.VISIBLE
        empty_view.visibility = View.GONE

        swipeRefresh?.setColorSchemeColors(-0xde690d)
        swipeRefresh?.isRefreshing = true

        getSearchBtnView().setOnClickListener(this)
        getSearchEditView()?.setOnEditorActionListener(this)
        getSearchEditView()?.addTextChangedListener(this)
        getSearchEditView()?.setHintTextColor(resources.getColor(R.color.color_white))
        getSearchEditView()?.setOnClickListener(this)
    }

    private fun switchToList() {
        if (mSpec.isStarType()) {
            switchFragment(STAR_FRAGMENT_LIST)
        } else if (mSpec.onlyShowFile()) {
            switchFragment(MEDIA_FRAGMENT_LIST)
        } else if (mSpec.withShowFolder()) {
            switchFragment(FOLDER_FRAGMENT_LIST)
        }
    }

    private fun switchToSearch() {
        if (mSpec.isStarType()) {
            switchFragment(STAR_FRAGMENT_SEARCH)
        } else if (mSpec.onlyShowFile()) {
            switchFragment(MEDIA_FRAGMENT_SEARCH)
        } else if (mSpec.withShowFolder()) {
            switchFragment(FOLDER_FRAGMENT_SEARCH)
        }
    }

    private fun switchFragment(key: String) {
        var fragment: Fragment? = null
        when {
            mSpec.isStarType() -> {
                fragment = getStarFragmentByName(key)
                if (fragment == null) {
                    fragment = StarSelectionFrament.newInstance(key == FOLDER_FRAGMENT_SEARCH)
                }
            }
            mSpec.onlyShowFile() -> {
                fragment = getMediaDBFragmentByName(key)
                if (fragment == null) {
                    fragment =
                        FileDatabaseSelectionFragment.newInstance(key == MEDIA_FRAGMENT_SEARCH)
                }
            }
            mSpec.withShowFolder() -> {
                fragment = getFolderFragmentByName(key)
                val rootPath =
                        if (mSpec.companyId <= 0) {
                            ROOT_PATH
                        } else {
                            CacheManager.getRelativePath(CacheManager.ES, mSpec.companyId.toString())
                        }
                if (fragment == null) {
                    fragment =
                        FolderSelectionFragment.newInstance(rootPath, key == FOLDER_FRAGMENT_SEARCH)
                }
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, key).commit()
            mFragmentImpl = fragment as FragmentImpl
        }
    }

    private fun getMediaDBFragmentByName(fragmentName: String): FileDatabaseSelectionFragment? {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (fragment is FileDatabaseSelectionFragment) {
            return fragment
        }
        return null
    }

    private fun getFolderFragmentByName(fragmentName: String): FolderSelectionFragment? {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (fragment is FolderSelectionFragment) {
            return fragment
        }
        return null
    }

    private fun getStarFragmentByName(fragmentName: String): StarSelectionFrament? {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (fragment is StarSelectionFrament) {
            return fragment
        }
        return null
    }

    private fun getFirstFragmentTag(): String {
        var tag = ""
        val fs = supportFragmentManager.fragments
        if (fs.size > 0 && !fs[0].tag.isNullOrEmpty()) {
            tag = fs[0].tag.toString()
        }
        return tag
    }
}