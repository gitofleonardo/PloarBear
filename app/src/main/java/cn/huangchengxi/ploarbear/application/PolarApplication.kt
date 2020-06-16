package cn.huangchengxi.ploarbear.application

import android.app.Application
import cn.huangchengxi.ploarbear.database.LocalSetting
import cn.huangchengxi.ploarbear.database.LocalUser

class PolarApplication: Application() {
    var localUser:LocalUser?=null
    var localSetting:LocalSetting?=null

    override fun onCreate() {
        super.onCreate()
    }
}