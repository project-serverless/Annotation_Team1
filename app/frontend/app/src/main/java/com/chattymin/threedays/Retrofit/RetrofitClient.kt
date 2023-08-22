package com.chattymin.threedays.Retrofit

import android.util.Log
import com.chattymin.threedays.App
import com.chattymin.threedays.Utils.Constants.TAG
import com.chattymin.threedays.Utils.isJsonArray
import com.chattymin.threedays.Utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// 싱글턴
object RetrofitClient {
    private var retrofitClient: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit?{
        Log.d(TAG, "RetrofitClient - getClient() called")

        // okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()

        // 로그를 찍기 위해 로깅 인터셉터 설정
        val loggingIntercepter = HttpLoggingInterceptor { message ->
            Log.d(TAG, "RetrofitClient - log() called / message: $message")

            when {
                message.isJsonObject() ->
                    Log.d(TAG, JSONObject(message).toString(4))
                message.isJsonArray() ->
                    Log.d(TAG, JSONObject(message).toString(4))
                else -> {
                    try {
                        Log.d(TAG, JSONObject(message).toString(4))
                    } catch (e: Exception) {
                        Log.d(TAG, message)
                    }
                }
            }
        }

        loggingIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(loggingIntercepter)

        // 엑세스 토큰을 헤더에 넣는 방법
        val baseParameterInterceptor: Interceptor = (object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitClient - intercept() called")

                val newRequest = chain.request().newBuilder().addHeader("Authorization", "${App.token_prefs.accessToken}").build()
                val newResponse = chain.proceed(newRequest)

                return newResponse
            }
        })

        // 위에서 설정한 기본파라메터 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(baseParameterInterceptor)

        // 커넥션 타임아웃
        client.connectTimeout(1000, TimeUnit.SECONDS)
        client.readTimeout(1000, TimeUnit.SECONDS)
        client.writeTimeout(1000, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)

        if (retrofitClient == null){
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build()) // 위에서 설정한 클라이언트로 레트로핏 클라이언트를 설정
                .build()
        }

        return retrofitClient
    }
}