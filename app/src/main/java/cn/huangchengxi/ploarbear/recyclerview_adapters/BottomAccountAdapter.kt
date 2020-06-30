package cn.huangchengxi.ploarbear.recyclerview_adapters

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.viewholders.BottomAccountHolder

class BottomAccountAdapter(private val users:List<LocalUser>):RecyclerView.Adapter<BottomAccountHolder>() {
    private var delListener:((position:Int)->Unit)?=null
    private var containerListener:((position:Int)->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomAccountHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_bottom_account_recyclerview,parent,false)
        return BottomAccountHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: BottomAccountHolder, position: Int) {
        val item=users[position]
        holder.account.text=SpannableStringBuilder(item.account)
        holder.deleteBtn.setOnClickListener {
            delListener?.invoke(position)
        }
        holder.container.setOnClickListener {
            containerListener?.invoke(position)
        }
    }
    fun setDelListener(delListener:((position:Int)->Unit)){
        this.delListener=delListener
    }
    fun setContainerListener(containerListener:((position:Int)->Unit)){
        this.containerListener=containerListener
    }
}