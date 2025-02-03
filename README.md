# 就问一下

## 简介

这是一个利用 Android 无障碍功能 + 悬浮窗 + 大模型的搜题应用

算是 [截图OCR识别后搜索题目获取答案](https://github.com/PuZhiweizuishuai/OCR-CopyText-And-Search) 这个项目的升级版吧

原理就是利用无障碍读取屏幕内容，然后通过悬浮窗来显示答案

因为还没有做聊天记录的功能，所以就只能问一下

## 功能

- [x] 自定义第三方 API 功能，兼容 OpenAI HTTP调用的模型都可以使用
- [x] 悬浮窗显示答案
- [x] 移动悬浮窗
- [ ] 悬浮窗最小化功能，这样就可以实现利用 Android 手机自带读取屏幕功能搜索，例如小米的超级小爱
- [ ] 利用 WebView 显示搜索引擎或其它大模型的结果
- [ ] 聊天历史记录
- [ ] 自建题库
- [ ] UI 优化