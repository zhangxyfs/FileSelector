package com.eblog.base.widget.box.utils

import com.eblog.base.room.entity.session.EMessageEntity
import com.eblog.base.room.manager.SessionManager
import com.eblog.base.utils.ImageUtil
import com.eblog.base.utils.cache.CacheManager
import com.eblog.base.utils.constant.RxConstant
import com.eblog.base.utils.download.DownloadListener
import com.eblog.base.utils.im.IMExt
import com.eblog.base.utils.listener.Callback
import com.eblog.base.utils.rx.RxBus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.model.Progress
import com.eblog.base.widget.preview.impl.PreviewImpl
import java.io.File

class PicDownloadListener(var uuMsgId: String?,var sessionId: String?, var impl: PreviewImpl) : DownloadListener()  {
    private var message: EMessageEntity? = null
    init {
        if (uuMsgId != null) {
            message = SessionManager.instance.getEMessage(sessionId, uuMsgId)
        }
    }

    override fun onStart(progress: Progress) {
        super.onStart(progress)
        if (message != null) {
            val ext = Gson().fromJson<Map<String, Any>>(message!!.extJson, object : TypeToken<Map<String, Any>>() {
            }.type)

            val user = IMExt.getIMUser(ext)
            val file = IMExt.getIMFile(ext)
            if (user != null && file != null)
                SessionManager.runInIObservable(Callback { param ->
                    SessionManager.instance.sessionFileAdd(sessionId,
                            uuMsgId!!, user.realMsgId, user.name, user.face, user.id,
                            file.url, file.name, message!!.messageTime, java.lang.Long.parseLong(file.size))
                })
        }
    }

    override fun onSuccess(file: File, progress: Progress) {
        if (message != null)
            SessionManager.runInIObservable(Callback { param ->
                SessionManager.instance.sessionFileUpdate(message!!.sessionId,
                        uuMsgId, progress.filePath)
            })

        val newPath = ImageUtil.copyTo(progress.filePath, CacheManager.EB_PHOTO)
        val newFile = File(newPath)
        ImageUtil.insertToSystemDB(newFile)
        impl.downloadSucc(CacheManager.getRelativePath(CacheManager.EB_PHOTO), newFile.name)

        RxBus.post(RxConstant.IM_CHAT_ROOM_REF_ITEM_BY_FILEPATH_OBSERVABLE, arrayOf<String>(progress.filePath, uuMsgId
                ?: ""))
    }

}