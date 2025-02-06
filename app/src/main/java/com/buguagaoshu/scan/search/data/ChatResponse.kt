package com.buguagaoshu.scan.search.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class ChatResponse(
    val usage: Usage = Usage(0),
    val id: String?,
    val choices: List<Choice>
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Usage(
    val completion_tokens: Int?
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class PromptTokensDetails(
    val cached_tokens: Int?
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Choice(
    val finish_reason: String? = null,
    val index: Int?,
    val message: Message? = null,
    val logprobs: String? = null,
    val delta: DeltaInfo? = null
)

//    "content": "叫通义千",
//    "function_call": null,
//    "refusal": null,
//    "role": null,
//    "tool_calls": null@OptIn(ExperimentalSerializationApi::class)
//@JsonIgnoreUnknownKeys
@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
data class DeltaInfo(
    val content : String = "",
    val function_call: String? = null,
    val refusal: String? = null,
    val role: String? = null,
    val tool_calls: String? = null
)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
data class Message(
    val role: String?,
    val content: String?
)