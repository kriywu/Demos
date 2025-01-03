package com.wurengao.demos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wurengao.demos.apm.HotFixActivity
import com.wurengao.demos.concurrent.ReadWriteLockActivity
import com.wurengao.demos.core.UIDesignActivity
import com.wurengao.demos.core.broadcast.BroadcastActivity
import com.wurengao.demos.core.ipc.BinderClientActivity
import com.wurengao.demos.core.touch.TouchEventDispatchActivity
import com.wurengao.demos.core.view.FlowActivity
import com.wurengao.demos.feature.StoryCreateGuideActivity
import com.wurengao.demos.media.OpenGLFilterActivity
import com.wurengao.demos.media.audio.AudioFileTransformActivity
import com.wurengao.demos.media.camera.CameraActivity
import com.wurengao.demos.media.surface.SurfaceADumpActivity
import com.wurengao.demos.utils.mmap.MMapDemoActivity


data class ItemData(var desc: String, var clz: Class<out Activity>?)

class MainActivity : AppCompatActivity() {
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }

    private val list = listOf(
        ItemData("Java Core", null),
        ItemData("Read Write Lock", ReadWriteLockActivity::class.java),
        ItemData("Hot Fix", HotFixActivity::class.java),

        ItemData("Android Core", null),
        ItemData("Activity", BroadcastActivity::class.java),
        ItemData("Broadcast", BroadcastActivity::class.java),
        ItemData("Service", BroadcastActivity::class.java),
        ItemData("Content Provider", BroadcastActivity::class.java),
        ItemData("Binder", BinderClientActivity::class.java),
        ItemData("ViewGroup", FlowActivity::class.java),
        ItemData("Touch Event", TouchEventDispatchActivity::class.java),
        ItemData("UI Design", UIDesignActivity::class.java),



        ItemData("Media", null),
        ItemData("Capture", CameraActivity::class.java),
        ItemData("Filter", OpenGLFilterActivity::class.java),
        ItemData("Video Render", SurfaceADumpActivity::class.java),
        ItemData("Audio Render", AudioFileTransformActivity::class.java),


        ItemData("Native Core", null),
        ItemData("MMAP", MMapDemoActivity::class.java),


        ItemData("Feature", null),
        ItemData("Story Guide", StoryCreateGuideActivity::class.java),


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = MyAdapter(list)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
    }
}


class MyAdapter(val data: List<ItemData>): RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_CONTENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].clz == null) TYPE_TITLE else TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val container = LinearLayout(parent.context)
        container.layoutParams = MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply { marginStart = 32; marginEnd = 32 }

        if (viewType == TYPE_TITLE) {
            return TitleHolder(container)
        } else {
            return ContentHolder(container)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TitleHolder) {
            holder.textView.text = data.get(position).desc
            return
        }

        if (holder is ContentHolder) {
            holder.button.setOnClickListener {
                val intent = Intent(it.context, data.get(position).clz)
                it.context.startActivity(intent)
            }
            holder.button.text = data[position].desc
            return
        }
    }
}

class TitleHolder(itemView: View): ViewHolder(itemView) {
    val textView = TextView(itemView.context)
    init {
        (itemView as? ViewGroup)?.addView(textView, LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }
}

class ContentHolder(itemView: View): ViewHolder(itemView) {
    val button = Button(itemView.context)
    init {
        (itemView as? ViewGroup)?.addView(button, LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }
}