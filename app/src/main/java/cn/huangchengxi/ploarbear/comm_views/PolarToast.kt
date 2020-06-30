package cn.huangchengxi.ploarbear.comm_views

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.huangchengxi.ploarbear.R

class PolarToast(val context: Context) :Toast(context){
    private val icon by lazy { view.findViewById<ImageView>(R.id.toast_icon) }
    private val message by lazy { view.findViewById<TextView>(R.id.toast_message) }

    init {
        setGravity(Gravity.TOP,0,0)
        val view=View.inflate(context,R.layout.toast_polar_common,null)
        setView(view)
        duration=LENGTH_SHORT
    }
    fun setIcon(i:Int){
        icon.setImageResource(i)
    }
    fun setMessage(m:String){
        message.text=SpannableStringBuilder(m)
    }
    fun setMessage(m:Int){
        message.text=SpannableStringBuilder(context.resources.getText(m))
    }
    companion object{
        fun show(context: Context,icon:Int,message:Int,duration:Int){
            show(context, icon, context.resources.getText(message).toString(), duration)
        }
        fun show(context: Context,icon: Int,message: String,duration: Int){
            val pt=PolarToast(context)
            pt.setIcon(icon)
            pt.setMessage(message)
            pt.duration=duration
            pt.show()
        }
    }
}