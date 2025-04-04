# 问一下

## 语言 README Language

[English](/README_EN.md)

## 简介

这是一个利用 Android 无障碍功能 + 悬浮窗 + 大模型的搜题应用

算是 [截图OCR识别后搜索题目获取答案](https://github.com/PuZhiweizuishuai/OCR-CopyText-And-Search) 这个项目的升级版吧

原理就是利用无障碍读取屏幕内容，然后通过悬浮窗来显示答案

因为还没有做聊天记录的功能，所以就只能问一下

## 更新日志

[更新日志](/CHANGELOG.md)

## 运行展示

https://www.bilibili.com/video/BV1PrNweHEDX

## 多语言支持

目前支持：中文、英语、俄语、法语、德语、日语、韩语


## 下载链接

GitHub：https://github.com/PuZhiweizuishuai/ScanSearch/releases

码云：https://gitee.com/puzhiweizuishuai/ScanSearch/releases

CloudFlare R2：https://img.buguagaoshu.com/data/%E9%97%AE%E4%B8%80%E4%B8%8B/%E9%97%AE%E4%B8%80%E4%B8%8B_1.3.0.apk

使用 CloudFlare R2 下载请复制链接到浏览器下载，直接访问链接会被拦截


> *注意：无障碍权限属于敏感权限，请确认软件来源后再安装或者自己编译安装，避免造成损失*

## 前情提要

众所周知我是一个学渣，所以在搜答案方面颇有成就

大概是在 4 年前，我写了这样一个脚本 

GitHub：[截图OCR识别后搜索题目获取答案](https://github.com/PuZhiweizuishuai/OCR-CopyText-And-Search)

码云：[截图OCR识别后搜索题目获取答案](https://gitee.com/puzhiweizuishuai/OCR-CopyText-And-Search)

利用 ADB 对屏幕截图后进行 OCR 识别，然后将识别到的结果用搜索引擎和本地题库进行搜索，然后快速获取答案

前几天，看着我手机里面的李跳跳和 DeepSeek，我突然发现，我可以利用无障碍读取屏幕数据，将读取到的题目发送给 DeepSeek 等大模型进行解答，利用  Android 悬浮窗 来显示答案

说干就干，感觉代码不是特别多，于是就有了这个项目

因为没有做历史记录，提问每次只能问一下，所以这个 APP 就叫问一下了

## 技术实现

本来是想写的，但是我发现实在没啥可写的，因为大部分都是 AI 写的 😂😂😂


## 使用指南

### 一、配置无障碍权限与悬浮窗权限

第一次使用会弹出无障碍权限配置菜单

在已下载应用内点击问一下

给予问一下无障碍权限

![无障碍权限](/doc/img/03.jpg)

### 二、配置 API 服务商

首先你需要配置好你的 AI 服务商，当前你也可以不用配置这个，直接使用悬浮窗内打开网页进行搜索，不过由于 Android Webview 控件我不太会用，显示的效果有问题

由于 DeepSeek 的服务目前用不了，暂时用阿里通义的 API 替代

API申请地址：https://www.aliyun.com/minisite/goods?userCode=4i6gwidx


登陆后进入控制台点右上角头像选 API-KEY 生成一个 API_KEY

![申请api-key](/doc/img/02.png)

然后到 APP 内填写你需要调用的大模型名称、 API 地址、和 API-KEY 即可使用


![填写配置](/doc/img/01.jpg)

#### 其它可以白嫖的API服务地址

字节火山：https://www.volcengine.com/product/doubao

目前免费送 50 万 TOKE，支持满血 DeepSeek R1 模型


### 三、开始使用

打开悬浮窗后，进入你要搜索的应用

由于只监听了滑动事件，所以进入应用后请先在屏幕上划两下，然后再点击加载数据

这是应该就可以读取到屏幕上显示内容了

![进入应用](/doc/img/04.jpg)


将你要搜索的题目进行勾选

点击确定

这样题目就会自动出现再搜索框


![搜索框](/doc/img/05.jpg)

> ⚠️注意：如果需要对题目进行编辑，请先点击打开 ⌨️ 键盘获取焦点，不然无法输入，修改完成后请点击关闭键盘，读取屏幕数据会无法读取到当前屏幕信息

为避免滑动事件冲突，如果需要挪动窗口位置，请先点击右上角的锁，挪动完成后，再点击一下锁就可以再次滑动显示内容



最后点击提问即可获取答案

![答案](/doc/img/06.jpg)


### 四、其它功能

#### 1、web搜索

点击打开网页可以调用 秘塔AI搜索，不过由于显示界面太小，所以显示会有一些问题

![秘塔AI搜索](/doc/img/07.jpg)

点击最小化按钮可以缩小悬浮窗，这时你可以利用手机系统自带的 AI 功能对屏幕进行识别，获取问题信息

#### 2、悬浮窗透明度调节

悬浮窗标题下方的滑块滑动可以调节窗口透明度


## 功能

- [x] 自定义第三方 API 功能，兼容 OpenAI HTTP调用的模型都可以使用
- [x] 悬浮窗显示答案
- [x] 移动悬浮窗
- [x] 悬浮窗最小化功能，这样就可以实现利用 Android 手机自带读取屏幕功能搜索，例如小米的超级小爱
- [x] 利用 WebView 显示搜索引擎或其它大模型的结果，部分完成
- [ ] 聊天历史记录
- [ ] 自建题库
- [X] UI 优化