package com.buguagaoshu.scan.search.data

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData (
    var id: String,
    val name: String,
    val api: String,
    val modelName: String,
    val apiKey: String,
    val prompt: String,
    var cheek: Boolean = false
)