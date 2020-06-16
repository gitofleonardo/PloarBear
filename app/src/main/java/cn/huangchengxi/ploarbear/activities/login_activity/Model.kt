package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper

class Model {
    fun obtainLocalUser(ctx:Context):List<LocalUser>{
        return SqliteHelper.getLocalUsers(ctx)
    }
    fun updateLocalUser(ctx: Context,user: LocalUser){
        SqliteHelper.updateUser(ctx,user)
    }
}