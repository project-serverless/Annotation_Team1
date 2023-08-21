package com.dongminpark.threedays.Retrofit


import com.dongminpark.threedays.Utils.API
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {
    // Sign Up
    @POST(API.SIGNUP)
    fun signup(@Body UserInfo: JsonObject) : Call<JsonElement>

    @POST(API.CONFIRM)
    fun confirm(@Body UserInfo: JsonObject) : Call<JsonElement>

    @POST(API.LOGIN)
    fun login(@Body UserInfo: JsonObject) : Call<JsonElement>

//    @DELETE(API.BOOKMARKS_DELETE_POSTNUM)
//    fun bookmarksDeletePostnum(@Path("postNum") postNum: Int) : Call<JsonElement>
//
//    // comment-controller
//    @POST(API.COMMENTS)
//    fun comments(@Body CommentRequestDto: JsonObject) : Call<JsonElement>

//    @GET(API.COMMENT_LIST_POSTNUM)
//    fun commentsListPostNum(@Path("postNum") postNum: Int, @Query("start")start: Int, @Query("display") display: Int) : Call<JsonElement>

}