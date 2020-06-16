package cn.huangchengxi.ploarbear.activities.activity_utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Window
import android.view.WindowManager
import java.lang.Exception

class ToolKits {
    companion object{
        fun makeFullScreen(ctx:Context){
            try {
                (ctx as Activity).window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
                (ctx as Activity).requestWindowFeature(Window.FEATURE_NO_TITLE)
            }catch (e:Exception){
                Log.e("Cast Exception","You must pass an activity context")
            }
        }
    }
}