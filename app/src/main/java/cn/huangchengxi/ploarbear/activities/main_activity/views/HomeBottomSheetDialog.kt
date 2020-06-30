package cn.huangchengxi.ploarbear.activities.main_activity.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import androidx.annotation.StyleRes
import cn.huangchengxi.ploarbear.R

class HomeBottomSheetDialog(context: Context,@StyleRes res:Int):Dialog(context,res) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.view_home_bottom_sheet)

        window!!.setGravity(Gravity.BOTTOM)
        val window=window
        val param=window!!.attributes
        val metrics= DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics)
        Log.e("wid hei","""${metrics.widthPixels}  ${metrics.heightPixels}""")
        val height=(metrics.heightPixels*0.9).toInt()
        param.height=height
        param.width=metrics.widthPixels
        window.attributes=param
    }
}