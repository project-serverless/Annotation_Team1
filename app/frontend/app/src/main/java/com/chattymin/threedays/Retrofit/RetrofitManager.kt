package com.chattymin.threedays.Retrofit

import android.util.Log
import com.chattymin.threedays.App
import com.chattymin.threedays.Model.MainPageInfo
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

    fun confirm(
        ID: String,
        PW: String,
        nickName: String,
        infoMessage: String,
        Code: String,
        completion: (RESPONSE_STATE) -> Unit
    ) {
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
                            Log.e("TAG", "onResponse: $accessToken")

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

    fun mainpage(completion: (RESPONSE_STATE, mainPageInfo: MainPageInfo?) -> Unit) {
        val call = iRetrofit?.mainpage() ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)
            }

            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            Log.e("TAG", "onResponse: ${body}", )
                            val data = body.get("body").asJsonObject
                            val goalArr = data.get("GoalArr").asJsonArray
                            val friendGoalArr = data.get("FriendGoalArr").asJsonArray

                            val info = MainPageInfo(
                                Name = data.get("Name").asString,
                                SuccessGoal = data.get("SuccessGoal").asInt,
                                ContinueGoal = data.get("ContinueGoal").asInt,
                                SuccessPercent = data.get("SuccessPercent").asInt,
                                FriendCnt = data.get("FriendCnt").asInt,
                                Goal = data.get("Goal").asString,
                                TodaySuccess = data.get("TodaySuccess").asBoolean,
                                GoalArr = mutableListOf(
                                    goalArr[0].asBoolean,
                                    goalArr[1].asBoolean,
                                    goalArr[2].asBoolean
                                ),
                                FriendName = data.get("FriendName").asString,
                                FriendGoal = data.get("FriendGoal").asString,
                                FriendGoalArr = mutableListOf(
                                    friendGoalArr[0].asBoolean,
                                    friendGoalArr[1].asBoolean,
                                    friendGoalArr[2].asBoolean
                                ),
                                //userId = "do",
                                //friendId = "ng"
                                userId = data.get("userId").asString,
                                FriendId = data.get("FriendId").asString
                            )

                            completion(RESPONSE_STATE.OKAY, info)
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATE.FAIL, null)
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

    fun successTodayGoal(completion: (RESPONSE_STATE) -> Unit) {
        val call = iRetrofit?.successTodayGoal() ?: return

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

    fun firendList(completion: (RESPONSE_STATE) -> Unit) {
        val call = iRetrofit?.friendList() ?: return

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

    fun friendDetails(friendName: String, completion: (RESPONSE_STATE) -> Unit){
        val jsonObject = JsonObject()
        jsonObject.addProperty("userId", friendName)

        val call = iRetrofit?.friendDetails(jsonObject) ?: return

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