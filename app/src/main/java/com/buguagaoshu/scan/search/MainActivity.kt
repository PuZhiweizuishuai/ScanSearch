package com.buguagaoshu.scan.search

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.buguagaoshu.scan.search.config.StaticVariableConfig
import com.buguagaoshu.scan.search.control.FxComposeControl
import com.buguagaoshu.scan.search.ui.CommonViewModel
import com.buguagaoshu.scan.search.ui.HomePage
import com.buguagaoshu.scan.search.ui.theme.SearchTheme
import com.buguagaoshu.scan.search.utils.PreferencesDataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val activityScope = lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val commonViewModel : CommonViewModel by viewModels()

        enableEdgeToEdge()
        setContent {

            SearchTheme {
                HomePage(commonViewModel, openFloatingWindow = {
                    FxComposeControl.install(application, commonViewModel)
                })
            }
        }
        if (!StaticVariableConfig.isInitialized) {
            StaticVariableConfig.isInitialized = true
            initData(commonViewModel)
        }

        // 检查无障碍服务是否已开启
        if (!isAccessibilityServiceEnabled()) {
            // 引导用户开启无障碍服务
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

    }

    // 检查无障碍权限
    private fun isAccessibilityServiceEnabled(): Boolean {
        val settingValue = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        val packageName = packageName
        val serviceName = "$packageName.service.ScreenReaderService"
        return settingValue?.contains(serviceName) == true
    }

    private fun initData(commonViewModel: CommonViewModel) {
        println("初始化数据")
        // 使用lifecycleScope替代临时CoroutineScope
        activityScope.launch {
            // 并行收集多个数据流
            launch(Dispatchers.IO) { collectApiKey(commonViewModel) }
            launch(Dispatchers.IO) { collectBaseUrl(commonViewModel) }
            launch(Dispatchers.IO) { collectModelName(commonViewModel) }
            launch(Dispatchers.IO) { collectOptions(commonViewModel) }
        }
    }

    // 分离各个数据流收集逻辑
    private suspend fun collectOptions(commonViewModel: CommonViewModel) {
        println("开始读取设置选项")
        try {
            PreferencesDataStoreUtils.readString(
                application,
                StaticVariableConfig.OPTIONS_NAME
            ).collect { value ->
                println("收到设置选项值: $value")
                when (value) {
                    "DeepSeek" -> commonViewModel.optionIndex = 0
                    else -> commonViewModel.optionIndex = 1
                }
            }
        } catch (e: Exception) {
            println("读取设置选项失败: ${e.message}")
        }
    }

    private suspend fun collectApiKey(commonViewModel: CommonViewModel) {
        println("开始读取API KEY")
        try {
            PreferencesDataStoreUtils.readString(
                application,
                StaticVariableConfig.API_KEY
            ).collect { value ->
                println("收到API KEY值: ${value?.take(3)}...") // 安全日志
                value?.let { commonViewModel.updateApiKeyText(it) }
            }
        } catch (e: Exception) {
            println("读取API KEY失败: ${e.message}")
        }
    }

    private suspend fun collectBaseUrl(commonViewModel: CommonViewModel) {
        println("开始读取Base URL")
        try {
            PreferencesDataStoreUtils.readString(
                application,
                StaticVariableConfig.BASE_URL
            ).collect { value ->
                println("收到Base URL值: $value")
                value?.let { commonViewModel.setBaseUrl(it) }
            }
        } catch (e: Exception) {
            println("读取Base URL失败: ${e.message}")
        }
    }

    private suspend fun collectModelName(commonViewModel: CommonViewModel) {
        println("开始读取模型名称")
        try {
            PreferencesDataStoreUtils.readString(
                application,
                StaticVariableConfig.MODEL_NAME
            ).collect { value ->
                println("收到模型名称值: $value")
                value?.let { commonViewModel.updateModelName(it) }
            }
        } catch (e: Exception) {
            println("读取模型名称失败: ${e.message}")
        }
    }
}
