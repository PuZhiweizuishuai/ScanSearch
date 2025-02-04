package com.buguagaoshu.scan.search.service
import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.buguagaoshu.scan.search.config.StaticVariableConfig
import com.buguagaoshu.scan.search.data.ScanSearchData
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class ScreenReaderService : AccessibilityService() {

    // 指定按钮的包名和类名，需要根据实际情况修改
    private val targetButtonPackageName = "com.buguagaoshu.scan.search"
    private val targetButtonClassName = "androidx.compose.material3.Button"
    // 指定按钮的文本内容，需要根据实际情况修改
    private val targetButtonText = "读取屏幕"

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!StaticVariableConfig.openScan) {
            return
        }

        if (event.packageName == targetButtonPackageName) {
            return
        }

        // 监听滑动事件
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED || event.eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
            // 判断事件类型是否为点击事件
            val source = event.source
            // 检查点击的节点是否符合目标按钮的条件
            if (source != null) {
                // TODO 增加指定包名过滤配置
                println(source.packageName)
            }

            // 获取根节点信息
            val rootNode = rootInActiveWindow
            if (rootNode != null) {
                // 清除之前的数据
                StaticVariableConfig.screenTextList.clear();
                // 遍历节点并读取内容
                traverseNodeLoop(rootNode)
            }
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    private fun traverseNodeLoop(node: AccessibilityNodeInfo) {
        val stack = mutableListOf<AccessibilityNodeInfo>()
        stack.add(node)

        while (stack.isNotEmpty()) {
            val currentNode = stack.removeAt(stack.size - 1)

            // 读取节点的文本内容
            val text = currentNode.text
            if (text != null && text.isNotEmpty()) {
                // 存储数据
                StaticVariableConfig.screenTextList.add(
                    ScanSearchData(Uuid.random().toString(), text.toString())
                )
            }

            // 遍历子节点并将它们添加到栈中
            for (i in currentNode.childCount - 1 downTo 0) {
                val child = currentNode.getChild(i)
                if (child != null) {
                    stack.add(child)
                }
            }
        }
    }

    private fun traverseNode(node: AccessibilityNodeInfo) {
        // 读取节点的文本内容
        val text = node.text
        if (text != null && text.isNotEmpty()) {
            println(text)
        }

        // 遍历子节点
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                traverseNode(child)
            }
        }
    }

    override fun onInterrupt() {
        // 服务中断时的处理
    }
}