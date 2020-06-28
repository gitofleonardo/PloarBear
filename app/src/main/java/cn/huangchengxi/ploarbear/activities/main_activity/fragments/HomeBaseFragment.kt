package cn.huangchengxi.ploarbear.activities.main_activity.fragments

import androidx.fragment.app.Fragment

open class HomeBaseFragment:Fragment() {
    private var onTransactToThis:(()->Unit)?=null
    fun setOnTransaction(onTransact:(()->Unit)){
        onTransactToThis=onTransact
    }
    fun onTransactToFragment(){
        onTransactToThis?.invoke()
    }
}