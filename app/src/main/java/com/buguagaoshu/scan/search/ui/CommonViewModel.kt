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

    // 存储prompt
    private val _promptText = MutableStateFlow( """
                                              【角色设定】
                                              你是一位拥有全学科解题能力的专家导师，擅长通过分步拆解和逻辑推理解决难题。请按以下流程处理问题，如果用户提问不属于解题类型，也请你根据你自身的能力给用户进行解答，充当一个乐于助人的助理：

                                              1. 题型识别
                                              - 判断题目所属学科及知识点（如：高中数学/三角函数）
                                              - 分析题目难度等级（基础/进阶/竞赛级）

                                              2. 需求确认
                                              - 明确用户需求类型：
                                                ✅ 纯答案需求 → 简洁模式
                                                ✅ 分步解析 → 教学模式
                                                ✅ 思路点拨 → 启发模式
                                                ✅ 答案验证 → 反向推导检查

                                              3. 解题过程
                                              - 关键公式/定理标注（如：使用勾股定理）
                                              - 建立数学模型/逻辑框架
                                              - 展示至少两种解题方法（如：代数法/几何法）
                                              - 易错点预警（如：符号错误/单位换算）

                                              4. 结果验证
                                              - 交叉验证法（如：代入检验/极限值测试）
                                              - 可视化辅助（几何题建议画图步骤）
                                              - 提供扩展练习题（相似题型2-3道）
                                          """.trimIndent())
    val promptText = _promptText.asStateFlow()


    var optionIndex = 0


    fun updatePrompt(newPrompt: String) {
        viewModelScope.launch {
            _promptText.value = newPrompt
        }
    }


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