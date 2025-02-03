package com.buguagaoshu.scan.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buguagaoshu.scan.search.config.StaticVariableConfig
import com.buguagaoshu.scan.search.control.FxComposeControl
import com.buguagaoshu.scan.search.control.HttpControl
import com.buguagaoshu.scan.search.data.ChatResponse
import com.buguagaoshu.scan.search.data.SendData
import com.buguagaoshu.scan.search.data.SendMessage

import com.petterp.floatingx.util.FxInputHelper
import kotlinx.serialization.json.Json


@Preview
@Composable
fun FloatingWindowContentPreview() {
    val configModel = remember { CommonViewModel() }
    FloatingWindowContent(configModel)
}

@Composable
fun FloatingWindowContent(commonViewModel: CommonViewModel) {
    // 使用 mutableStateListOf 来管理可变列表，自动跟踪列表变化

    val textList = remember { mutableStateListOf<String>() }
    val textMap = remember { mutableStateMapOf<String, Boolean>() }

    // 创建并记住滚动状态
    val scrollState = rememberScrollState()

    // 控制数据列表的显示与不显示
    val isListVisible = remember { mutableStateOf(true) }
    val aiResultText = remember { mutableStateOf("") } // 新增AI结果状态

    val questionText by commonViewModel.questionText.collectAsState()

    val view = LocalView.current


    val alphabet = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K")


    val baseUrl by commonViewModel.aiServerBaseUrl.collectAsState()

    val modelName by commonViewModel.modelName.collectAsState()

    val apiKeyText by commonViewModel.apiKeyText.collectAsState()

    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp), // 增加阴影高度
        shape = RoundedCornerShape(16.dp), // 添加圆角
        modifier = Modifier
            .size(width = 240.dp, height = 530.dp) // 调整高度以显示更多内容
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(scrollState)
                .padding(12.dp), // 增加内边距
            verticalArrangement = Arrangement.spacedBy(12.dp) // 增加元素间距
        ) {
            // 顶部标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "答题浮窗",
                    style = MaterialTheme.typography.titleMedium, // 使用预设样式
                    modifier = Modifier.weight(1f), // 占据剩余空间
                )

                // 图标按钮组
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp) // 按钮间添加间距
                ) {
                    IconButton(
                        onClick = {
                            FxComposeControl.updateDisplayMode()
                        },
                        modifier = Modifier.size(32.dp) // 固定按钮大小
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "最小化",
                            //tint = MaterialTheme.colorScheme.primary // 使用主题色
                        )
                    }
                    IconButton(
                        onClick = {
                            FxComposeControl.updateDisplayMode()
                        },
                        modifier = Modifier.size(32.dp) // 固定按钮大小
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "锁定",
                            tint = MaterialTheme.colorScheme.primary // 使用主题色
                        )
                    }
                    IconButton(
                        onClick = { FxComposeControl.cancel() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "关闭",
                            tint = MaterialTheme.colorScheme.error // 使用错误色
                        )
                    }
                }
            }

            // 分割线
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )


            AnimatedVisibility(
                visible = isListVisible.value,
                modifier = Modifier.weight(1f)
            ) {
                // 内容列表
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(textList) { item ->
                        Surface(
                            shape = RoundedCornerShape(8.dp), // 添加圆角
                            color = MaterialTheme.colorScheme.surfaceVariant, // 使用表面变体色
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp), // 添加右边距
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Checkbox(
                                    checked = textMap[item] ?: false,
                                    onCheckedChange = { textMap[item] = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary // 使用主题色
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // AI结果框
            AnimatedVisibility(
                visible = !isListVisible.value,
            ) {
                val scrollState2 = rememberScrollState()
                OutlinedTextField(
                    value = aiResultText.value,
                    onValueChange = { /* 只读不需要修改 */ },
                    modifier = Modifier
                        .height(225.dp)
                        .verticalScroll(scrollState2),
                    label = { Text("AI分析结果") },
                    readOnly = true,
                    //shape = RoundedCornerShape(8.dp),
                    maxLines = Int.MAX_VALUE
                )
            }


            // 输入框
            OutlinedTextField(
                value = questionText,
                onValueChange = { commonViewModel.updateQuestionText(it) },
                label = { Text("需要提问搜索的数据") },
                maxLines = 2,
                shape = RoundedCornerShape(8.dp), // 添加圆角
                modifier = Modifier.fillMaxWidth()
            )

            // 操作按钮组
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 键盘操作行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(
                        text = "显示键盘",
                        onClick = { FxInputHelper.showKeyBoard("ScanSearch", view) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "关闭键盘",
                        onClick = { FxInputHelper.hideKeyBoard("ScanSearch", view) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionButton(
                        text = "加载数据",
                        onClick = {
                            println("点击读取屏幕按钮")
                            // 清除历史数据
                            textList.clear()
                            textMap.clear()

                            var i = 0;

                            while (i < StaticVariableConfig.screenTextList.size) {
                                if (alphabet.contains(StaticVariableConfig.screenTextList[i])) {
                                    if (i + 1 >= StaticVariableConfig.screenTextList.size) {
                                        textList.add(StaticVariableConfig.screenTextList[i])
                                    } else {
                                        // 处理只有的情况ABCD
                                        textList.add("${StaticVariableConfig.screenTextList[i]}.${StaticVariableConfig.screenTextList[i + 1]}")
                                        i++
                                    }
                                } else {
                                    textList.add(StaticVariableConfig.screenTextList[i])
                                }

                                i++
                            }

                            textList.forEach { key ->
                                textMap[key] = false
                            }
                            // 显示列表
                            isListVisible.value = true
                        },
                        modifier = Modifier.weight(1f),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer // 使用次要容器色
                    )
                }

                // 确认操作行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ActionButton(
                        text = "确认",
                        onClick = {
                            val stringQuestion = StringBuilder()
                            for (item in textList) {
                                if (textMap[item] == true) {
                                    stringQuestion.append("${item}\n")
                                }
                            }
                            println(stringQuestion.toString())
                            commonViewModel.updateQuestionText(stringQuestion.toString())
                            // 新增隐藏列表逻辑
                            isListVisible.value = false
                        },
                        modifier = Modifier.weight(1f)
                    )
                    FilledTonalButton(
                        onClick = {
                            // 新增隐藏列表逻辑
                            isListVisible.value = false
                            aiResultText.value = ""
                            if (questionText.isBlank()) {
                                return@FilledTonalButton
                            }
                            val sendData = SendData(
                                stream = true,
                                modelName,
                                listOf(
                                    SendMessage(
                                        "system",
                                        """
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
                                          """.trimIndent()
                                    ),
                                    SendMessage(
                                        "user",
                                        questionText
                                    )
                                )
                            )

                            HttpControl.sendStream(
                                sendData,
                                baseUrl,
                                apiKeyText,
                                onChunkReceived = { chunk ->
                                    val jsonLine = if (chunk.startsWith("data: ")) {
                                        chunk.substring("data: ".length)
                                    } else {
                                        chunk
                                    }
                                    try {
                                        val str = Json.decodeFromString<ChatResponse>(jsonLine)
                                        if (str.choices.isNotEmpty()) {
                                            aiResultText.value += str.choices[0].delta?.content
                                        }
                                    } catch (e: Exception) {
                                        println(e.message)
                                    }
                                },
                                onComplete = {
                                    println("Streaming completed.")
                                },
                                onError = { error ->
                                    println("Error: $error")
                                }
                            )

                        },
                        modifier = Modifier.weight(2f), // 占据更多空间
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary // 使用主题色
                        )
                    ) {
                        Text("提问", style = TextStyle(fontSize = 10.sp))
                    }
                }
            }
        }
    }
}

// 自定义操作按钮组件
@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = RoundedCornerShape(8.dp) // 统一圆角
    ) {
        Text(text, style = TextStyle(fontSize = 10.sp))
    }
}