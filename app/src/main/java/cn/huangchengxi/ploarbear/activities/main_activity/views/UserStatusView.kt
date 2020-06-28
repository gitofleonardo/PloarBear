package cn.huangchengxi.ploarbear.activities.main_activity.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import cn.huangchengxi.ploarbear.R
import com.bumptech.glide.Glide

class UserStatusView(context:Context,attrs:AttributeSet?,defStyle:Int):FrameLayout(context, attrs,defStyle) {
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    private var currentStatus=STATUS.ONLINE
    private var currentPortraitHref=""
    private val portrait by lazy { findViewById<ImageView>(R.id.portrait) }
    private val status by lazy { findViewById<ImageView>(R.id.user_status) }

    init {
        init()
    }
    private fun init(){
        val view= View.inflate(context,R.layout.view_user_status,this)
    }
    public fun setStatus(stat:STATUS){
        currentStatus=stat
        when (stat){
            STATUS.ONLINE->{
                Glide.with(context).load(R.drawable.online).into(status)
            }
            STATUS.BUSY->{
                Glide.with(context).load(R.drawable.busy).into(status)
            }
            STATUS.OFFLINE->{
                Glide.with(context).load(R.drawable.offline).into(status)
            }
        }
    }
    public fun setPortrait(url:String){
        currentPortraitHref=url
        Glide.with(context).load(url).into(portrait)
    }
    companion object{
        enum class STATUS{
            ONLINE,
            OFFLINE,
            BUSY
        }
    }
}