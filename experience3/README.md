
# Compose 实验项目

这是一个基于 Kotlin 和 Jetpack Compose 的 Android 应用开发实验项目，包含三个主要任务。

## 项目结构

```
ComposeDemo/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/composedemo/
│   │       │   ├── MainActivity.kt          # 主界面，包含所有任务
│   │       │   └── ui/theme/
│   │       │       ├── Color.kt             # 颜色定义
│   │       │       ├── Theme.kt             # 主题配置
│   │       │       └── Type.kt              # 字体配置
│   │       └── res/
│   │           ├── values/
│   │           │   ├── strings.xml
│   │           │   └── themes.xml
│   │           └── xml/
│   │               ├── backup_rules.xml
│   │               └── data_extraction_rules.xml
│   ├── build.gradle.kts                     # 应用级 Gradle 配置
│   └── proguard-rules.pro
├── build.gradle.kts                         # 项目级 Gradle 配置
├── settings.gradle.kts
└── gradle.properties
```

## 实验目的

1. 掌握使用 Kotlin 语言开发 Android 的基本流程
2. 掌握 Android Compose 布局的基本用法
3. 进一步熟悉 Kotlin 语言的特性

## 实验内容

### 任务一：创建首个 Kotlin 应用

- 选择创建 Empty Activity
- 使用 Kotlin 语言
- 应用名为 ComposeDemo
- 最小支持 API Level 21
- 显示 "Hello Android!" 问候语

### 任务二：实践 Compose 布局

- 使用 Compose 组件进行界面布局
- 展示 Row、Column、Box 等布局的使用
- 包含按钮和卡片组件的示例

### 任务三：面向 AI 应用的 Compose 布局

界面功能：
- **顶部栏**：显示应用标题
- **预览区**：相机预览占位，后续可替换为 CameraX
- **结果区**：显示模型名称、识别结果、置信度、推理时间
- **按钮区**：
  - 拍照识别
  - 相册导入
  - 切换模型（MobileNet / ResNet）
  - 清空结果

## 技术栈

- **语言**：Kotlin
- **UI 框架**：Jetpack Compose
- **最低 SDK**：API 21 (Android 5.0)
- **目标 SDK**：API 34
- **Gradle**：Kotlin DSL

## 运行项目

### 前置要求

1. Android Studio Jellyfish (2024.1.1) 或更高版本（建议最新版）
2. Android SDK API 36
3. JDK 17 或更高版本

### 构建步骤

1. 克隆项目到本地
2. 使用 Android Studio 打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击运行按钮或使用命令：

```bash
./gradlew installDebug
```

## 主要功能组件说明

### MainScreen

主界面，包含顶部导航栏和三个任务标签页。

### TaskOneScreen

展示首个 Kotlin 应用的简单界面，包含卡片和问候语。

### TaskTwoScreen

Compose 布局实践，展示各种布局组件的使用：
- Column 垂直布局
- Row 水平布局
- Box 容器布局
- Card 卡片组件
- Button 按钮组件

### TaskThreeScreen

AI 应用界面，包含：
- 预览区占位
- 结果显示区域
- 功能按钮区

## Git 仓库

项目已准备好上传到 GitHub。使用以下命令初始化：

```bash
git init
git add .
git commit -m "Initial commit: Compose 实验项目"
git remote add origin &lt;your-repo-url&gt;
git push -u origin main
```

## 参考资源

- [Kotlin 官方文档](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose 官方教程](https://developer.android.com/jetpack/compose/tutorial)
- [Jetpack Compose 基础知识](https://developer.android.com/jetpack/compose/layouts/basics)
- [Compose 布局指南](https://developer.android.com/jetpack/compose/layouts)

## 许可证

本项目仅用于学习目的。

