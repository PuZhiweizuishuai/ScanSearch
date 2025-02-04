package com.buguagaoshu.scan.search.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.buguagaoshu.scan.search.config.StaticVariableConfig
import com.buguagaoshu.scan.search.utils.PreferencesDataStoreUtils
import kotlinx.coroutines.launch
import androidx.browser.customtabs.CustomTabsIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(commonViewModel: CommonViewModel ,openFloatingWindow: () -> Unit) {
    val apiKeyText by commonViewModel.apiKeyText.collectAsState()

    val options = listOf("DeepSeek", "其它")
    var expanded by remember { mutableStateOf(false) }

    var selectedOptionText by remember { mutableStateOf(options[commonViewModel.optionIndex]) }
    val baseUrl by commonViewModel.aiServerBaseUrl.collectAsState()

    val modelName by commonViewModel.modelName.collectAsState()

    val prompt by commonViewModel.promptText.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("只能问一下")
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },

                ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { commonViewModel.updateBaseUrl(it) },
                    label = { Text("请选择 AI 服务商") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption

                                if (selectedOptionText != "其它") {
                                    commonViewModel.updateBaseUrl(selectedOptionText)
                                } else {
                                    commonViewModel.setBaseUrl(baseUrl)
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (selectedOptionText == "其它") {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = baseUrl,
                    onValueChange = { commonViewModel.updateBaseUrl(it) },
                    label = { Text("请输入服务器地址") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = modelName,
                onValueChange = { commonViewModel.updateModelName(it) },
                label = { Text("请输入模型名称") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = apiKeyText,
                onValueChange = { commonViewModel.updateApiKeyText(it) },
                label = { Text("请输入API Key") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = prompt,
                maxLines = 5,
                onValueChange = { commonViewModel.updatePrompt(it) },
                label = { Text("自定义 prompt") },
            )



            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    println(selectedOptionText)
                    println(commonViewModel.apiKeyText.value)
                    println(commonViewModel.aiServerBaseUrl.value)
                    scope.launch {
                        // 调用工具类保存数据
                        PreferencesDataStoreUtils.saveString(context, StaticVariableConfig.OPTIONS_NAME, selectedOptionText)
                        PreferencesDataStoreUtils.saveString(context, StaticVariableConfig.API_KEY, commonViewModel.apiKeyText.value)
                        PreferencesDataStoreUtils.saveString(context, StaticVariableConfig.BASE_URL, commonViewModel.aiServerBaseUrl.value)
                        PreferencesDataStoreUtils.saveString(context, StaticVariableConfig.MODEL_NAME, commonViewModel.modelName.value)
                    }


                }
            ) {
                Text("保存配置")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    openFloatingWindow()
                }
            ) {
                Text("打开浮窗")
            }

            TextButton (
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val url = "https://gitee.com/puzhiweizuishuai/ScanSearch"
                    val customTabsIntent = CustomTabsIntent.Builder().build()
                    try {
                        customTabsIntent.launchUrl(context, Uri.parse(url))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ) {
                Text("使用指南")
            }
        }
    }
}