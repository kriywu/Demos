package com.wurengao.surfaceviewtestdemo

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
import com.wurengao.surfaceviewtestdemo.audio.AudioFileTransformActivity
import com.wurengao.surfaceviewtestdemo.broadcast.BroadcastActivity
import com.wurengao.surfaceviewtestdemo.camera.CameraActivity
import com.wurengao.surfaceviewtestdemo.reentrantlock.ReentrantLockTestActivity
import com.wurengao.surfaceviewtestdemo.surface.SurfaceADumpActivity


data class ActivityData(var clz: Class<out Activity>, var desc: String)

class MainActivity : AppCompatActivity() {
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }

    private val list = listOf(
        ActivityData(BroadcastActivity::class.java, "广播"),
        ActivityData(SurfaceADumpActivity::class.java, "渲染"),
        ActivityData(ReentrantLockTestActivity::class.java, "重入锁") ,
        ActivityData(CameraActivity::class.java, "相机"),
        ActivityData(AudioFileTransformActivity::class.java, "音频")
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