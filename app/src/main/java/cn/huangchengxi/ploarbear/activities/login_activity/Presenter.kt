package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.database.LocalUser
import java.lang.ref.WeakReference

class Presenter(exe: Executor,val context: Context):Model.Pster {
    private val executor=WeakReference<Executor>(exe)
    private val model=Model(this)

    fun login(ctx: Context,account:String,password:String){
        executor.get()?.onLogin()
        //model.updateLocalUser(ctx, LocalUser(account,password))
        //
        model.login(account,password)
    }
    fun obtainLocalUsers(ctx:Context){
        val users=model.obtainLocalUser(ctx)
        executor.get()?.onLoadLocalUsers(users)
    }
    fun updateLocalCurrentUser(localUser: LocalUser){
        model.updateLocalUser(context,localUser)
    }
    fun removeLocalUser(username:String){
        model.removeLocalUser(context,username)
    }
    interface Executor{
        fun onLoginSuccess(session: String)
        fun onLoginFailed(reason:String)
        fun onLoadLocalUsers(users:List<LocalUser>)
        fun onLogin()
        fun removeLocalUserSuccess()
    }

    override fun onLoginSuccess(session:String) {
        executor.get()?.onLoginSuccess(session)
    }

    override fun onNetworkError() {
        executor.get()?.onLoginFailed(context.resources.getText(R.string.network_error).toString())
    }

    override fun onLoginFailure() {
        executor.get()?.onLoginFailed(context.resources.getText(R.string.account_or_passwd_not_correct).toString())
    }

    override fun onRemoveUserSuccess() {
        executor.get()?.removeLocalUserSuccess()
    }
}