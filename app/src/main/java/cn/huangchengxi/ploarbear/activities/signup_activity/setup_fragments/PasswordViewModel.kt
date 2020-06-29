package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import cn.huangchengxi.ploarbear.server.ServerConfig
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

class PasswordViewModel(view:View) {
    private val v=WeakReference<View>(view)

    private fun validationImp(): SignUp {
        val okHttpClient= OkHttpClient.Builder()
            .build();

        val retrofit= Retrofit.Builder()
            .baseUrl(ServerConfig.HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SignUp::class.java)
    }

    fun signUp(email: String,password: String,validation:String){
        v.get()?.onChecking()
        val signUp=validationImp()
        signUp.signUp(email,password,validation).enqueue(object : Callback<Result>{
            override fun onFailure(call: Call<Result>, t: Throwable) {
                v.get()?.onFailure()
            }
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.body()?.state == "success"){
                    v.get()?.onSuccess()
                }else{
                    v.get()?.onFailure()
                }
            }
        })
    }
    interface View{
        fun onChecking()
        fun onSuccess()
        fun onFailure()
    }
    companion object{
        private data class Result(val state:String,val message:String)
    }
    private interface SignUp{
        @POST("/sign-up")
        @FormUrlEncoded
        fun signUp(@Field("email")email:String,@Field("password")password:String,@Field("validation")code:String):Call<Result>
    }
}