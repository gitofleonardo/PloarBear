package cn.huangchengxi.ploarbear.activities.signup_activity.setup_fragments

import android.os.Message
import android.util.Log
import cn.huangchengxi.ploarbear.handler.CommonHandler
import cn.huangchengxi.ploarbear.server.ServerConfig
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.ref.WeakReference

class EmailViewModel(view:View):CommonHandler.Executor{
    private val handler=CommonHandler(this)
    private val v=WeakReference<View>(view)

    private fun validationImp():AccountCreationCode{
        val okHttpClient=OkHttpClient.Builder()
            .build();

        val retrofit=Retrofit.Builder()
            .baseUrl(ServerConfig.HOST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(AccountCreationCode::class.java)
    }
    fun sendEmailValidationCode(email:String){
        v.get()?.onSendingEmail()
        val accountCreationCode=validationImp()

        accountCreationCode.getCodeResult(email).enqueue(object:Callback<Result>{
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("Exception",t.message)
                v.get()?.onErrorSending()
            }
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.e("Response","""${response.body()?.state}  ${response.body()?.message}""")
                if (response.body()?.state == "success"){
                    v.get()?.onSuccessSending()
                }else{
                    v.get()?.onErrorSending()
                }
            }
        })
    }
    fun validateEmailCode(mail:String,code:String){
        v.get()?.onValidating()
        val accountCreationCode=validationImp()
        accountCreationCode.getSignUpResult(mail,code).enqueue(object : Callback<Result>{
            override fun onFailure(call: Call<Result>, t: Throwable) {
                v.get()?.onErrorValidation()
            }
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.body()?.state!! == "success"){
                    v.get()?.onSuccessValidation()
                }else{
                    v.get()?.onErrorValidation()
                }
            }
        })
    }
    interface View{
        fun onSendingEmail()
        fun onErrorSending()
        fun onSuccessSending()
        fun onErrorValidation()
        fun onSuccessValidation()
        fun onValidating()
    }
    override fun handleMessage(msg: Message) {

    }
    companion object{
        data class Result(val state:String,val message:String)
    }
    private interface AccountCreationCode{
        @POST("/validation-code")
        @FormUrlEncoded
        fun getCodeResult(@Field("email") email:String):Call<Result>

        @POST("/check-code")
        @FormUrlEncoded
        fun getSignUpResult(@Field("email")email: String,@Field("code")code:String):Call<Result>
    }
}