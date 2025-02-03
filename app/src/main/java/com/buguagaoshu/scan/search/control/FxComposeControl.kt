package com.buguagaoshu.scan.search.control

import android.app.Application
import androidx.compose.ui.platform.ComposeView
import com.buguagaoshu.scan.search.config.StaticVariableConfig
import com.buguagaoshu.scan.search.ui.CommonViewModel
import com.buguagaoshu.scan.search.ui.FloatingWindowContent
import com.petterp.floatingx.FloatingX
import com.petterp.floatingx.assist.FxDisplayMode
import com.petterp.floatingx.assist.FxGravity
import com.petterp.floatingx.assist.FxScopeType
import com.petterp.floatingx.compose.enableComposeSupport

object FxComposeControl {
    private val _TAG = "ScanSearch"

    private var hasMove = false

    fun updateDisplayMode() {
        if (hasMove) {
            hasMove = false
            FloatingX.configControl(_TAG).setDisplayMode(FxDisplayMode.ClickOnly)
        } else {
            hasMove = true
            FloatingX.configControl(_TAG).setDisplayMode(FxDisplayMode.Normal)

        }
    }

    fun cancel() {
        // 关闭无障碍服务
        StaticVariableConfig.openScan = false
        FloatingX.controlOrNull(_TAG)?.cancel()
    }

    fun install(context: Application, commonViewModel: CommonViewModel) {
        if (!FloatingX.isInstalled(_TAG)) {
            // 开启无障碍服务，读取数据
            StaticVariableConfig.openScan = true

            FloatingX.install {
                setContext(context)
                setTag(_TAG)
                // system浮窗必须调用此方法，才可以启用Compose支持
                enableComposeSupport()
                setScopeType(FxScopeType.SYSTEM)
                setGravity(FxGravity.LEFT_OR_TOP)
                setDisplayMode(FxDisplayMode.ClickOnly)



                setLayoutView(
                    ComposeView(context).apply {
                        setContent {
                            FloatingWindowContent(commonViewModel)
                        }
                    }
                )
                setEnableLog(false)
                setEdgeOffset(20f)
                setEnableEdgeAdsorption(true)
            }.show()
        } else {
            // TODO 增加消息框
        }
    }
}