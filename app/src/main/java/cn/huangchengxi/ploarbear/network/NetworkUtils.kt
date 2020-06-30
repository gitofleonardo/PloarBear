package cn.huangchengxi.ploarbear.network

import cn.huangchengxi.ploarbear.server.ServerConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkUtils {
    companion object{
        fun <T> buildGsonRetrofitApi(clazz:Class<T>):Class<T>{
            val okHttpClient= OkHttpClient.Builder()
                .build()

            val retrofit= Retrofit.Builder()
                .baseUrl(ServerConfig.HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(clazz.javaClass)
        }
    }
}