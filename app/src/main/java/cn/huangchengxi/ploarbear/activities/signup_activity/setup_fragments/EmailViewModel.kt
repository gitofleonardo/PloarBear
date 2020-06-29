package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Message
import cn.huangchengxi.ploarbear.handler.CommonHandler
import java.lang.ref.WeakReference

class EmailViewModel(view:View):CommonHandler.Executor{
    private val handler=CommonHandler(this)
    private val v=WeakReference<View>(view)

    public fun sendEmailValidationCode(email:String){

    }
    public fun validateEmailCode(mail:String,code:String){

    }
    interface View{
        fun onSendingEmail()
        fun onErrorSending()
        fun onSuccessSending()
        fun onErrorValidation()
        fun onSuccessValidation()
    }
    override fun handleMessage(msg: Message) {

    }
}