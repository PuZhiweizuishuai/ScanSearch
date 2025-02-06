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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults.buttonColors
import com.buguagaoshu.scan.search.data.ConfigData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomePage(commonViewModel: CommonViewModel ,openFloatingWindow: () -> Unit) {
    val apiKeyText by commonViewModel.apiKeyText.collectAsState()

    val baseUrl by commonViewModel.aiServerBaseUrl.collectAsState()

    val modelName by commonViewModel.modelName.collectAsState()

    val prompt by commonViewModel.promptText.collectAsState()

    val apiName by commonViewModel.apiName.collectAsState()

    val configMap = commonViewModel.apiMap

    // 记录当前选中的 ConfigData
    var selectedConfigData by remember { mutableStateOf<ConfigData?>(null) }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
            FlowRow(modifier = Modifier.padding(8.dp)) {
                configMap.forEach { (key, value) ->
                    ConfigFilterChip(
                        configData = value,
                        commonViewModel = commonViewModel,
                        isSelected = value == selectedConfigData,
                        onSelect = { newSelected ->
                            // 如果点击的是已经选中的 ConfigFilterChip，则取消选中
                            if (newSelected == selectedConfigData) {
                                selectedConfigData = null
                                newSelected.cheek = false
                            } else {
                                // 取消之前选中的 ConfigFilterChip
                                selectedConfigData?.cheek = false
                                // 选中新的 ConfigFilterChip
                                selectedConfigData = newSelected
                                newSelected.cheek = true
                                commonViewModel.showUserSelectConfig(newSelected)
                            }
                            commonViewModel.updateConfig(context)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = apiName,
                onValueChange = { commonViewModel.updateApiName(it) },
                label = { Text("配置文件名称") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )


                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = baseUrl,
                    onValueChange = { commonViewModel.updateBaseUrl(it) },
                    label = { Text("请输入服务器地址") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )



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
                    println(commonViewModel.apiKeyText.value)
                    println(commonViewModel.aiServerBaseUrl.value)
                    var config = ConfigData(
                        Uuid.random().toString(),
                        commonViewModel.apiName.value,
                        commonViewModel.aiServerBaseUrl.value,
                        commonViewModel.modelName.value,
                        commonViewModel.apiKeyText.value,
                        commonViewModel.promptText.value
                    )
                    if (selectedConfigData != null) {
                        config.id = selectedConfigData!!.id
                    }

                    commonViewModel.addToApiMap(config.id, config)
                    selectedConfigData = null
                    commonViewModel.updateConfig(context)
                }
            ) {
                Text("保存配置")
            }

            Button(
                colors = buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (selectedConfigData == null) {
                        // 提示用户需要选中配置后才能删除
                        android.widget.Toast.makeText(context, "需要选中配置后才能删除", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        // 弹出确认删除的对话框
                        showDeleteConfirmationDialog = true
                    }
                }
            ) {
                Text("删除配置")
            }

            if (showDeleteConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmationDialog = false },
                    title = { Text("确认删除") },
                    text = { Text("是否确认删除当前选中配置？") },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedConfigData?.let { config ->
                                    commonViewModel.removeFromApiMap(config.id)
                                }
                                showDeleteConfirmationDialog = false
                            }
                        ) {
                            Text("确认")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDeleteConfirmationDialog = false }
                        ) {
                            Text("取消")
                        }
                    }
                )
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


@Composable
fun ConfigFilterChip(
    configData: ConfigData,
    commonViewModel: CommonViewModel,
    isSelected: Boolean,
    onSelect: (ConfigData) -> Unit
) {
    FilterChip(
        onClick = {
            onSelect(configData)
        },
        label = {
            Text(configData.name)
        },
        selected = isSelected,
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}