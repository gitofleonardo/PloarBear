package cn.huangchengxi.ploarbear.activities.login_activity

import android.content.Context
import android.os.Message
import android.util.Log
import cn.huangchengxi.ploarbear.database.LocalUser
import cn.huangchengxi.ploarbear.database.SqliteHelper
import cn.huangchengxi.ploarbear.handler.CommonHandler
import cn.huangchengxi.ploarbear.network.NetworkUtils
import cn.huangchengxi.ploarbear.server.ServerConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.ref.WeakReference

class Model(ptser:Presenter):CommonHandler.Executor {
    private val handler=CommonHandler(this)
    private val presenter=WeakReference<Presenter>(ptser)

    fun obtainLocalUser(ctx:Context):List<LocalUser>{
        return SqliteHelper.getLocalUsers(ctx)
    }
    fun updateLocalUser(ctx: Context,user: LocalUser){
        SqliteHelper.updateUser(ctx,user)
    }
    fun login(username:String,password:String){
        val okHttpClient= OkHttpClient.Builder()
            //.addInterceptor(LoginInterceptor())
            .build()

        val retrofit= Retrofit.Builder()
            .baseUrl(ServerConfig.HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ln=retrofit.create(LoginApi::class.java)
        ln.login(username,password).enqueue(object : Callback<LoginResult>{
            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                presenter.get()?.onNetworkError()
            }
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                if (response.body()!=null){
                    if (response.body()!!.state == "success"){
                        val session = response.headers()["set-cookie"]
                        //Log.e("header",response.headers().toMultimap().toString())
                        if (session == null){
                            presenter.get()?.onLoginFailure()
                            return
                        }
                        presenter.get()?.onLoginSuccess(session)
                        return
                    }
                }
                presenter.get()?.onLoginFailure()
            }
        })
    }
    fun removeLocalUser(ctx: Context,username: String){
        SqliteHelper.removeLocalUser(ctx, username)
        presenter.get()?.onRemoveUserSuccess()
    }
    override fun handleMessage(msg: Message) {

    }
    interface Pster{
        fun onLoginSuccess(session:String)
        fun onNetworkError()
        fun onLoginFailure()
        fun onRemoveUserSuccess()
    }
    private interface LoginApi{
        @POST("/login")
        @FormUrlEncoded
        fun login(@Field("username") username: String,@Field("password") password: String):Call<LoginResult>
    }
    companion object{
        data class LoginResult(val state:String,val time:String)
        class LoginInterceptor:Interceptor{
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request=chain.request()
                Log.e("Request",request.headers.toMultimap().toString())
                val response=chain.proceed(request)
                Log.e("Response",response.headers.toMultimap().toString())
                return response
            }

        }
    }
}