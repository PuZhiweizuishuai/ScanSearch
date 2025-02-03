package com.buguagaoshu.scan.search.data

import kotlinx.serialization.Serializable

@Serializable
data class SendData(
    val stream: Boolean,
    val model: String,
    val messages: List<SendMessage>
)

@Serializable
data class SendMessage (
    val role: String,
    val content: String
)