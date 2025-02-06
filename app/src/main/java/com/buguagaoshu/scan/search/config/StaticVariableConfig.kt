package com.buguagaoshu.scan.search.config

import com.buguagaoshu.scan.search.data.ConfigData
import com.buguagaoshu.scan.search.data.ScanSearchData

object StaticVariableConfig {
    // 是否开启读取屏幕功能
    var openScan = false
    var isInitialized = false
    // 存储读取到的屏幕数据
    var screenTextList: ArrayList<ScanSearchData> = ArrayList()

    // 存储配置好的 API 列表
    var apiConfigMap : MutableMap<String, ConfigData> = mutableMapOf()
    var API_MAP = "API_MAP"

    var OPTIONS_NAME = "OPTIONS_NAME"
    var API_KEY = "API_KEY"
    var BASE_URL = "BASE_URL"
    var MODEL_NAME = "MODEL_NAME"
}