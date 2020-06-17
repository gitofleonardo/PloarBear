package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import android.os.Message
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper
import cn.huangchengxi.ploarbear.handler.CommonHandler

class Model:CommonHandler.Executor {
    private val handler=CommonHandler(this)

    fun obtainLocalUser(ctx:Context):List<LocalUser>{
        return SqliteHelper.getLocalUsers(ctx)
    }
    fun updateLocalUser(ctx: Context,user: LocalUser){
        SqliteHelper.updateUser(ctx,user)
    }

    override fun handleMessage(msg: Message) {

    }
}