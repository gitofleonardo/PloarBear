package cn.huangchengxi.ploarbear.activities.main_activity.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.huangchengxi.ploarbear.R

class HomeBottomItem(context: Context,attrs:AttributeSet?,defStyle:Int):FrameLayout(context, attrs,defStyle){
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)
    init {
        val view=View.inflate(context, R.layout.view_item_home_bottom,this)
        if (attrs!=null) {
            val arr = context.obtainStyledAttributes(attrs, R.styleable.HomeBottomItem)
            view.findViewById<ImageView>(R.id.option_icon).setImageResource(
                arr.getResourceId(
                    R.styleable.HomeBottomItem_icon,
                    R.drawable.option
                )
            )
            view.findViewById<TextView>(R.id.option_text).text =
                arr.getText(R.styleable.HomeBottomItem_name)
            arr.recycle()
        }
    }
}