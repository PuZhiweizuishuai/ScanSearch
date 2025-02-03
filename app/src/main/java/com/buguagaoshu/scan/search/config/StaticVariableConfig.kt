package com.buguagaoshu.scan.search.config

object StaticVariableConfig {
    // 是否开启读取屏幕功能
    var openScan = false
    var isInitialized = false
    // 存储读取到的屏幕数据
    var screenTextList: ArrayList<String> = ArrayList()

    var OPTIONS_NAME = "OPTIONS_NAME"
    var API_KEY = "API_KEY"
    var BASE_URL = "BASE_URL"
    var MODEL_NAME = "MODEL_NAME"
}