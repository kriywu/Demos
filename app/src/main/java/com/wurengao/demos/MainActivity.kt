package com.wurengao.demos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wurengao.demos.audio.AudioFileTransformActivity
import com.wurengao.demos.binder.BinderClientActivity
import com.wurengao.demos.broadcast.BroadcastActivity
import com.wurengao.demos.camera.CameraActivity
import com.wurengao.demos.mmap.MMapDemoActivity
import com.wurengao.demos.reentrantlock.ReentrantLockTestActivity
import com.wurengao.demos.surface.SurfaceADumpActivity
import com.wurengao.demos.view.FlowActivity


data class ActivityData(var clz: Class<out Activity>, var desc: String)

class MainActivity : AppCompatActivity() {
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }

    private val list = listOf(
        ActivityData(BroadcastActivity::class.java, "广播"),
        ActivityData(ReentrantLockTestActivity::class.java, "重入锁") ,
        ActivityData(CameraActivity::class.java, "视频采集"),
        ActivityData(SurfaceADumpActivity::class.java, "视频渲染"),
//        ActivityData(CameraActivity::class.java, "音频采集"),
        ActivityData(AudioFileTransformActivity::class.java, "音频播放"),
        ActivityData(OpenGLFilterActivity::class.java, "滤镜"),
        ActivityData(MMapDemoActivity::class.java, "MMAP"),
        ActivityData(BinderClientActivity::class.java, "Binder"),
        ActivityData(FlowActivity::class.java, "自定义ViewGroup"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = MyAdapter(list)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
    }
}


class MyAdapter(val data: List<ActivityData>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val container = LinearLayout(parent.context)
        container.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        return MyViewHolder(container)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.button.setOnClickListener {
            val intent = Intent(it.context, data.get(position).clz)
            it.context.startActivity(intent)
        }
        holder.button.text = data.get(position).desc
    }

}


class MyViewHolder(itemView: View): ViewHolder(itemView) {
    val button = Button(itemView.context)
    init {
        (itemView as? ViewGroup)?.addView(button, LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }
}