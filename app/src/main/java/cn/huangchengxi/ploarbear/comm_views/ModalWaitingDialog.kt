package cn.huangchengxi.ploarbear.comm_views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import cn.huangchengxi.ploarbear.R
import com.bumptech.glide.Glide

class ModalWaitingDialog(context: Context):Dialog(context,R.style.Theme_AppCompat_Dialog) {
    private val icon by lazy { findViewById<ImageView>(R.id.waiting_icon) }
    private val name by lazy { findViewById<TextView>(R.id.waiting_text) }

    private var nameText:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_modal_waiting)
        init()
    }
    private fun init() {
        Glide.with(context).asGif().load(R.drawable.loading1).into(icon)
        if (nameText==null)
            nameText=context.resources.getText(R.string.loading).toString()
        name.text=SpannableStringBuilder(nameText)
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setGravity(Gravity.TOP)
        val metrics=DisplayMetrics()
        window?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val param=window?.attributes
        param?.dimAmount=0.5f
        param?.width= (metrics.widthPixels* 0.9).toInt()
        param?.verticalMargin=0.02f
        window?.attributes=param
    }
    fun setLoadingText(text:String){
        nameText=text
        //name.text=SpannableStringBuilder(nameText)
    }
}