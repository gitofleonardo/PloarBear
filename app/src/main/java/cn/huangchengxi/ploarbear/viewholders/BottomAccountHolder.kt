package cn.huangchengxi.ploarbear.viewholders

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.ploarbear.R

class BottomAccountHolder(view:View):RecyclerView.ViewHolder(view){
    val portrait by lazy { view.findViewById<ImageView>(R.id.bottom_portrait) }
    val nickname by lazy { view.findViewById<TextView>(R.id.bottom_nickname) }
    val account by lazy { view.findViewById<TextView>(R.id.bottom_account) }
    val deleteBtn by lazy { view.findViewById<FrameLayout>(R.id.delete_btn) }
}