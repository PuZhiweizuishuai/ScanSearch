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
import com.buguagaoshu.scan.search.data.ConfigData
import com.buguagaoshu.scan.search.ui.CommonViewModel
import com.buguagaoshu.scan.search.ui.HomePage
import com.buguagaoshu.scan.search.ui.theme.SearchTheme
import com.buguagaoshu.scan.search.utils.PreferencesDataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


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
            // 初始化配置
            launch(Dispatchers.IO) { collectApiMap(commonViewModel) }
        }
    }

    private suspend fun collectApiMap(commonViewModel: CommonViewModel) {
        println("开始读取设置选项")
        try {
            PreferencesDataStoreUtils.readString(
                application,
                StaticVariableConfig.API_MAP
            ).collect { value ->
                println(value)
                var configMap : MutableMap<String, ConfigData> = mutableMapOf()
                if (value == null || value.isBlank()) {
                    return@collect
                }

                try {
                    configMap = Json.decodeFromString<MutableMap<String, ConfigData>>(value)
                    commonViewModel.addAllApiMap(configMap)
                } catch (e: Exception) {
                    return@collect
                }
            }
        } catch (e: Exception) {
            println("读取设置选项失败: ${e.message}")
        }
    }
}
