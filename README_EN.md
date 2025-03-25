# Ask a question

## README Language

[中文](/README.md)

## Introduction

This is a question - searching application that utilizes Android's accessibility features, floating windows, and large language models. 

It can be regarded as an upgraded version of the project [Search for Answers after Screenshot OCR Recognition](https://github.com/PuZhiweizuishuai/OCR-CopyText-And-Search). 

The principle is to use the accessibility function to read the screen content and then display the answers through a floating window. 


Since the chat history function has not been implemented yet, it can only be used for simple inquiries.


## Change Log

[Change Log](/CHANGELOG.md)

## Running Demonstration

https://www.bilibili.com/video/BV1PrNweHEDX

## Multilingual Support

Currently supported languages: Chinese, English, Russian, French, German, Japanese, Korean


## Download Links

GitHub：https://github.com/PuZhiweizuishuai/ScanSearch/releases

Gitee：https://gitee.com/puzhiweizuishuai/ScanSearch/releases

CloudFlare R2：https://img.buguagaoshu.com/data/%E9%97%AE%E4%B8%80%E4%B8%8B/%E9%97%AE%E4%B8%80%E4%B8%8B_1.3.0.apk

For downloading from CloudFlare R2, please copy the link to your browser. Direct access to the link may be blocked.


> *Note: The accessibility permission is a sensitive one. Please confirm the software source before installation or compile and install it yourself to avoid potential losses.*

## Background Information

As we all know, I'm a study noob, so I've achieved quite a bit in the aspect of searching for answers.


About four years ago, I wrote such a script.

GitHub：[Search for Answers after Screenshot OCR Recognition](https://github.com/PuZhiweizuishuai/OCR-CopyText-And-Search)

Gitee：[Search for Answers after Screenshot OCR Recognition](https://gitee.com/puzhiweizuishuai/OCR-CopyText-And-Search)

This script takes a screenshot of the screen using ADB, then conducts OCR recognition. After that, it searches for the recognized results in search engines and local question banks to quickly obtain answers.

A few days ago, when looking at LiTiaotiao and DeepSeek on my phone, I suddenly realized that I could use the accessibility feature to read the screen data, send the read questions to large language models like DeepSeek for answers, and display the answers through an Android floating window.

I took action as soon as I thought of it. Since I felt there wasn't particularly much code to write, this project came into being.


Because there's no history record function and you can only ask one question each time, this APP is named "Wen Yixia" (Ask a Question).

## Technical Implementation

I originally intended to write about it, but then I found there's really not much to write, because most of it was written by AI 😂😂😂


## User Guide

### I. Configure Accessibility and Floating Window Permissions

The first time you use the app, an accessibility permission configuration menu will pop up.

In the downloaded apps, click on "Wen Yixia" (Ask a Question).

Grant "Wen Yixia" accessibility permission.

![Configure Accessibility](/doc/img/03.jpg)

### II. Configure API Service Provider

First, you need to configure your AI service provider. Currently, you can also choose not to configure it and directly open a web page within the floating window for searching. 


However, as I'm not very proficient in using the Android Webview control, there are some issues with the display effect.

API Application Address: https://platform.deepseek.com/usage


1.After logging in, click "API keys" in the sidebar to generate an API_KEY.


![申请api-key](/doc/img/02.png)


2.Then, in the APP, fill in the name of the large language model you need to call, the API address, and the API - KEY, and you can start using it.


![填写配置](/doc/img/01.jpg)

#### Other Free - to - Use API Service Addresses

1.ByteDance Huoshan (Volcengine): https://www.volcengine.com/product/doubao

Currently, it gives away 500,000 TOKEs for free and supports the full - capacity DeepSeek R1 model.

2.Alibaba Tongyi: https://www.aliyun.com/minisite/goods?userCode=4i6gwidx


It offers 1,000,000 tokens for free.


### III. Start Using

1.After opening the floating window, enter the app where you want to search.

- Since only the sliding event is monitored, after entering the app, please slide on the screen twice first, and then click to load the data. 

- At this time, the content displayed on the screen should be readable.

![进入应用](/doc/img/04.jpg)


2.Select the question you want to search.

3.Click "OK". The question will then automatically appear in the search box.


![搜索框](/doc/img/05.jpg)

> ⚠️ Note: If you need to edit the question, please click to open the ⌨️ keyboard first to get the focus; otherwise, you can't input. After modification, please click to close the keyboard. Reading the screen data may not be able to obtain the current screen information.
To avoid conflicts with the sliding event, if you need to move the window position, please click the lock in the upper - right corner first. After moving, click the lock again to be able to slide and display the content again.


4.Finally, click "Ask" to get the answer.

![答案](/doc/img/06.jpg)


### IV. Other Functions

#### 1. Web Search

Clicking "Open Web Page" can call the Mita AI Search. However, due to the small display interface, there are some display problems.

![秘塔AI搜索](/doc/img/07.jpg)

Clicking the minimize button can shrink the floating window. At this time, you can use the AI function built into the mobile phone system to recognize the screen and obtain question information.

#### 2. Floating Window Transparency Adjustment

Sliding the slider below the floating window title can adjust the window transparency.


## Features

- [x] Custom third - party API function. Models compatible with OpenAI's HTTP calls can be used.
- [x] Display answers in a floating window.
- [x] Move the floating window.
- [x] Floating window minimization function, enabling the use of the Android phone's built - in screen - reading function for search, such as Xiaomi's Super Xiaoai.
- [x] Use WebView to display the results of search engines or other large language models, partially completed.
- [ ] Chat history record.
- [ ] Self - built question bank.
- [X] UI optimization.