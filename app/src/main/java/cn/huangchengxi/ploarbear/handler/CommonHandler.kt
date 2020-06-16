package cn.huangchengxi.ploarbear.handler

import android.os.Handler
import android.os.Message
import android.util.Log
import java.lang.Exception
import java.lang.ref.WeakReference

class CommonHandler(executor: Executor): Handler() {
    private val executor:WeakReference<Executor> = WeakReference<Executor>(executor)

    override fun handleMessage(msg: Message) {
        try {
            executor.get()?.handleMessage(msg)
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("Exception",e.toString())
        }
    }
    public interface Executor{
        fun handleMessage(msg: Message)
    }
}