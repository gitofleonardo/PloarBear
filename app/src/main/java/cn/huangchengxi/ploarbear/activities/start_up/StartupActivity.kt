package cn.huangchengxi.ploarbear.activities.start_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import cn.huangchengxi.ploarbear.R
import cn.huangchengxi.ploarbear.activities.activity_utils.ToolKits
import cn.huangchengxi.ploarbear.activities.login_activity.LoginActivity
import cn.huangchengxi.ploarbear.activities.main_activity.MainActivity
import cn.huangchengxi.ploarbear.application.PolarApplication
import cn.huangchengxi.ploarbear.database.LocalSetting
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper
import cn.huangchengxi.ploarbear.handler.CommonHandler
import java.lang.Exception

class StartupActivity : AppCompatActivity() , CommonHandler.Executor {
    private val handler=CommonHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ToolKits.makeFullScreen(this)
        setContentView(R.layout.activity_startup)
        //init()
        /**
         * start a thread to wait
         */
        val t=Thread(RunnableImp())
        t.start()
    }
    /**
     * when first enter this application,it will lead users to a sign up page
     * or else try to get local user,then login automatically
     */
    private fun init(){
        val setting=SqliteHelper.getLocalSetting(this)
        val user=SqliteHelper.getLocalCurrentUser(this)
        if (setting == null|| setting.firstInit==1 || user==null){
            /**first init*/
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }else{
            /**not first enter and has an account login
             * setup application info
             * */
            setupApplication(user,setting)
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
    /**
     * setup application context
     */
    private fun setupApplication(localUser:LocalUser,localSetting: LocalSetting){
        val app=(application as PolarApplication)
        app.localUser=localUser
        app.localSetting=localSetting
    }

    override fun handleMessage(msg: Message) {
        init()
    }
    inner class RunnableImp:Runnable{
        override fun run() {
            try {
                Thread.sleep(1000)
                handler.sendEmptyMessage(0)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}