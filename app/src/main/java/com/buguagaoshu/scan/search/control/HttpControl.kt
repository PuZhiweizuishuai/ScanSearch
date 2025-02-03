package com.buguagaoshu.scan.search.control

import com.buguagaoshu.scan.search.data.SendData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit



object HttpControl {
    private var client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.SECONDS) // 无限等待流数据
        .retryOnConnectionFailure(true)
        .build()
    private var contentType: MediaType = "application/json".toMediaType()

    fun send(
        sendData: SendData,
        url: String,
        key: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val body = Json.encodeToString(sendData).toRequestBody(contentType)
                // Authorization: Bearer $DASHSCOPE_API_KEY
                val req = Request
                    .Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Authorization", "Bearer $key")
                    .build()
                val call = client.newCall(req)
                val data = call.execute()
                if (data.isSuccessful) {
                    val responseBody = data.body.string()
                    responseBody.let {
                        CoroutineScope(Dispatchers.Main).launch {
                            onSuccess(it)
                        }
                    }
                } else {
                    onError("请求错误，请稍后重试！")
                }
            } catch (e: Exception) {
                e.message?.let { onError(it) }
            }
        }

    }


    fun sendStream(
        sendData: SendData,
        url: String,
        key: String,
        onChunkReceived: (String) -> Unit,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val body = Json.encodeToString(sendData).toRequestBody(contentType)
            // Authorization: Bearer $DASHSCOPE_API_KEY
            val req = Request
                .Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer $key")
                .build()
            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.message?.let { onError(it) }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val reader = response.body.charStream()
                        reader.forEachLine { line ->
                            if (line.isNotBlank()) {
                                onChunkReceived(line)
                            }
                        }
                        onComplete()
                    }
                }
            })
        }
    }


}