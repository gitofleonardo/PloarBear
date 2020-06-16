package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import cn.huangchengxi.ploarbear.database.LocalUser
import java.lang.ref.WeakReference

class Presenter(private val exe: Executor) {
    private val executor=WeakReference<Executor>(exe)
    private val model=Model()

    fun login(ctx: Context,account:String,password:String){
        model.updateLocalUser(ctx, LocalUser(account,password))
        //
        executor.get()?.onLogin()
    }
    fun obtainLocalUsers(ctx:Context){
        val users=model.obtainLocalUser(ctx)
        executor.get()?.onLoadLocalUsers(users)
    }
    interface Executor{
        fun onLoginSuccess()
        fun onLoginFailed()
        fun onLoadLocalUsers(users:List<LocalUser>)
        fun onLogin();
    }
}