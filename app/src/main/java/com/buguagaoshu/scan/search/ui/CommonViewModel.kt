package com.buguagaoshu.scan.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {
    // ai 服务商列表
    private val _aiServerBaseUrl = MutableStateFlow("https://api.deepseek.com")
    val aiServerBaseUrl = _aiServerBaseUrl.asStateFlow();

    // 存储 ai 服务商的 api key
    private val _apiKeyText = MutableStateFlow("")
    val apiKeyText = _apiKeyText.asStateFlow()

    // 模型名称
    private val  _modelName = MutableStateFlow("deepseek-reasoner")
    val modelName = _modelName.asStateFlow();

    // 存储需要提问的问题
    private val _questionText = MutableStateFlow("")
    val questionText = _questionText.asStateFlow()

    var optionIndex = 0


    fun updateModelName(newName: String) {
        viewModelScope.launch {
            _modelName.value = newName
        }
    }

    fun updateQuestionText(newText: String) {
        viewModelScope.launch {
            _questionText.value = newText
        }
    }


    fun updateApiKeyText(newText: String) {
        viewModelScope.launch {
            _apiKeyText.value = newText
        }
    }

    fun setBaseUrl(newUrl: String) {
        viewModelScope.launch {
            _aiServerBaseUrl.value = newUrl
        }
    }

    fun updateBaseUrl(newText: String) {
        val url: String

        if (newText == "DeepSeek") {
            url = "https://api.deepseek.com"
        } else {
            url = newText
        }
        viewModelScope.launch {
            _aiServerBaseUrl.value = url
        }
    }
}