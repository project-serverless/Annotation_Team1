package com.chattymin.threedays.Retrofit

import android.util.Log
import com.chattymin.threedays.App
import com.chattymin.threedays.Utils.API
import com.chattymin.threedays.Utils.RESPONSE_STATE
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response


class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스 가져오기
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun signup(ID: String, PW: String, Email: String, completion: (RESPONSE_STATE) -> Unit) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("ID", ID)
        jsonObject.addProperty("PW", PW)
        jsonObject.addProperty("Email", Email)

        val call = iRetrofit?.signup(jsonObject) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }

    fun confirm(ID: String, PW: String, nickName: String, infoMessage: String, Code: String, completion: (RESPONSE_STATE) -> Unit) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("ID", ID)
        jsonObject.addProperty("PW", PW)
        jsonObject.addProperty("nickName", nickName)
        jsonObject.addProperty("infoMessage", infoMessage)
        jsonObject.addProperty("Code", Code)

        val call = iRetrofit?.confirm(jsonObject) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }

    fun login(ID: String, PW: String, completion: (RESPONSE_STATE) -> Unit) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("ID", ID)
        jsonObject.addProperty("PW", PW)

        val call = iRetrofit?.login(jsonObject) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            val accessToken = it.asJsonObject.get("body").asString
                            App.token_prefs.accessToken = accessToken
                            Log.e("TAG", "onResponse: $accessToken", )

                            completion(RESPONSE_STATE.OKAY)
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }

    fun mainpage(completion: (RESPONSE_STATE) -> Unit) {
        val call = iRetrofit?.mainpage() ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            //val accessToken = it.asJsonObject.get("body").asString
                            //App.token_prefs.accessToken = accessToken

                            completion(RESPONSE_STATE.OKAY)
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }

    fun setgoal(Goal: String, completion: (RESPONSE_STATE) -> Unit) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("Goal", Goal)

        val call = iRetrofit?.setGoal(jsonObject) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }

    fun updateUserInfo(nickName: String, comment: String, completion: (RESPONSE_STATE) -> Unit) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("PW", "NONE")
        jsonObject.addProperty("nickName", nickName)
        jsonObject.addProperty("Comment", comment)

        val call = iRetrofit?.updateUserInfo(jsonObject) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL)
                    }
                }
            }
        })
    }
}