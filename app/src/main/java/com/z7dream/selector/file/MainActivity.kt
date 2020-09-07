package com.z7dream.selector.file

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.eblog.base.widget.box.engine.ImageEngine
import com.z7dream.lib.selector.Z7Plugin
import com.z7dream.lib.selector.box.Box
import com.z7dream.lib.selector.box.MimeTypeManager
import com.z7dream.lib.selector.box.entity.Item

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Z7Plugin.instance.init(object : Z7Plugin.SelectionListener{
            override fun applicationContext(): Context = this@MainActivity.applicationContext

            override fun sendToIM(list: List<Item>) {
            }

            override fun share(context: Context?, filePath: String?) {
            }

            override fun getImageEngine(): ImageEngine? = null

        })

        findViewById<Button>(R.id.button1).setOnClickListener {
            Box.from(this)
                .choose(MimeTypeManager.ofFolder())
                .setToolbarTitle("文件目录")
                .simpleFileSizeMax(18)
                .isNeedForward(true)
                .maxSelectablePerMediaType(9, 9)
                .maxSelectable(1)
                .countable(true)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .forResult(11111)
        }
    }
}