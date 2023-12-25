package com.rajbagri.chatgptclone


import com.rajbagri.dall_eclone.ImageRequest
import com.rajbagri.dall_eclone.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Body request: RequestBody,
        @Header("Content-Type") contentType: String,
        @Header("Authorization") apiKey: String

    ): ChatResponse

    @POST("images/generations")
    suspend fun imageGenerateInterface(
        @Body requestBody: RequestBody,
        @Header("Content-Type") contentType: String,
        @Header("Authorization") apiKey: String

    ): ImageResponse

    @POST("audio/translations")
    suspend fun audioGenerateInterface(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
        @Header("Authorization") apiKey: String

    ): AudioResponse
}