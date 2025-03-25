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
import androidx.compose.ui.res.stringResource
import com.buguagaoshu.scan.search.data.ConfigData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import com.buguagaoshu.scan.search.R.string as TextValue

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
    val tipsText = stringResource(TextValue.delete_config_tips)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = TextValue.app_title))
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
                label = { Text(stringResource(TextValue.config_file_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = baseUrl,
                onValueChange = { commonViewModel.updateBaseUrl(it) },
                label = { Text(stringResource(TextValue.please_enter_server_address)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )



            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = modelName,
                onValueChange = { commonViewModel.updateModelName(it) },
                label = { Text(stringResource(TextValue.please_enter_model_name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = apiKeyText,
                onValueChange = { commonViewModel.updateApiKeyText(it) },
                label = { Text(stringResource(TextValue.please_enter_API_Key)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = prompt,
                maxLines = 5,
                onValueChange = { commonViewModel.updatePrompt(it) },
                label = { Text(stringResource(TextValue.customize_the_prompt)) },
            )

            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        println(commonViewModel.apiKeyText.value)
                        println(commonViewModel.aiServerBaseUrl.value)
                        val config = ConfigData(
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
                    Text(stringResource(TextValue.save_config))
                }

                Button(
                    colors = buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (selectedConfigData == null) {
                            // 提示用户需要选中配置后才能删除

                            android.widget.Toast.makeText(context, tipsText, android.widget.Toast.LENGTH_SHORT).show()
                        } else {
                            // 弹出确认删除的对话框
                            showDeleteConfirmationDialog = true
                        }
                    }
                ) {
                    Text(stringResource(TextValue.delete_config))
                }

                if (showDeleteConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteConfirmationDialog = false },
                        title = { Text(stringResource(TextValue.confirm_deletion)) },
                        text = { Text(stringResource(TextValue.config_delete_tips)) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    selectedConfigData?.let { config ->
                                        commonViewModel.removeFromApiMap(config.id)
                                    }
                                    showDeleteConfirmationDialog = false
                                }
                            ) {
                                Text(stringResource(TextValue.confirm))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDeleteConfirmationDialog = false }
                            ) {
                                Text(stringResource(TextValue.cancel))
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
                    Text(stringResource(TextValue.open_float))
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
                    Text(stringResource(TextValue.usage_guide))
                }
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