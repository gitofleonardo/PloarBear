package cn.huangchengxi.ploarbear.network

import android.content.Context
import cn.huangchengxi.ploarbear.application.PolarApplication
import cn.huangchengxi.ploarbear.server.ServerConfig
import com.alibaba.fastjson.JSONObject
import okhttp3.*
import java.lang.Exception

class SessionInterceptor(val ctx:Context) :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest=chain.request()
        val oldResponse=chain.proceed(oldRequest)
        if (!checkAvailability(oldResponse)){
            val username=(ctx.applicationContext as PolarApplication).username
            val password=(ctx.applicationContext as PolarApplication).password
            val form=FormBody.Builder()
                .add("username",username!!)
                .add("password",password!!)
                .build()
            val loginRequest=Request.Builder()
                .url(ServerConfig.HOST)
                .post(form)
                .build()
            val response=chain.proceed(loginRequest)
            if (response.isSuccessful){
                if (checkAvailability(response)){
                    val session=response.headers["set-cookie"]
                    (ctx.applicationContext as PolarApplication).sessionId=session
                    val newRequest=Request.Builder()
                        .headers(oldRequest.headers)
                        .addHeader("cookie",session.toString())
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }
        return oldResponse
    }
    private fun checkAvailability(response: Response):Boolean{
        try {
            val obj=JSONObject.parseObject(response.body.toString())
            val state=obj.getString("state")
            if (state == "offline"){
                return false
            }
            return true
        }catch (e:Exception){
            return false
        }
    }
}