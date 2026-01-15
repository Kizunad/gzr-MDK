# gzr-MDK / Guzhenren DevKit

本仓库是《蛊真人（guzhenren）》的 **附属模组（Addon）开发套件**（DevKit MDK）。

核心目标：
- **不改主模组源码**（`sourceCode/guzhenren/`）的前提下，通过 DevKit 的 **Mixin 注入 + Hook 分发** 扩展功能。
- 提供一个可直接运行的 **示例 Addon**，方便开发者复制改造。

## 你应该从哪里开始

- 想写附属模组：从 `sourceCode/guzhenren-devkit-example/` 开始。
- 想新增 DevKit 扩展点：看 `sourceCode/guzhenren-devkit/`。
- `sourceCode/guzhenren/` 是主模组工程（MCreator 导出），默认只用于本地验证与参考。

## 仓库结构

```text
./
└── sourceCode/
    ├── guzhenren-devkit/          # DevKit：Mixin 注入 + Hook 分发
    ├── guzhenren-devkit-example/  # Example Addon：includeBuild 引入 DevKit
    └── guzhenren/                 # 主模组（MCreator 导出，谨慎修改）
```

## 环境要求

- Java：**21**（项目使用 toolchain）
- Minecraft：**1.21.1**
- NeoForge：**21.1.65**

## 快速开始（推荐路径：Example Addon）

Example 模块通过 Gradle 复合构建直接引用 DevKit：
- `sourceCode/guzhenren-devkit-example/settings.gradle`：`includeBuild('../guzhenren-devkit')`
- `sourceCode/guzhenren-devkit-example/mcreator.gradle`：`implementation 'net.guzhenren.devkit:guzhenren-devkit:0.1.0'`

在 Example 目录运行：

```bash
cd sourceCode/guzhenren-devkit-example
./gradlew runClient
```

## 常用命令

### DevKit

```bash
cd sourceCode/guzhenren-devkit
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow   # server + --nogui（无窗口加载验证）
./gradlew runHeadless         # 尽量减少弹窗/无 GPU 环境暴露错误（仍可能触发窗口）
./gradlew build
```

### Example Addon

```bash
cd sourceCode/guzhenren-devkit-example
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow
./gradlew build
```

### 主模组（仅当你需要验证/参考）

```bash
cd sourceCode/guzhenren
./gradlew runClient
./gradlew runServer
./gradlew build
```

## 开发 Addon：复制 Example 的最短路径

最简单的做法：
1. 复制 `sourceCode/guzhenren-devkit-example/` 为你的新模块目录。
2. 修改 `src/main/resources/META-INF/neoforge.mods.toml` 的 `modId/displayName/description`。
3. 修改 Java 入口类（参考 `sourceCode/guzhenren-devkit-example/src/main/java/net/guzhenren/devkit/example/GuzhenrenDevKitExampleMod.java`）。
4. 保留 `settings.gradle` 的 `includeBuild('../guzhenren-devkit')`，确保依赖坐标能被复合构建替换为本地 DevKit。

## DevKit 提供的 API

### 1) 体质显示名扩展（TizhiNames）

用途：让 Addon 自定义体质 ID 的显示名字（UI 里显示 `体质:<name>`）。

入口：`net.guzhenren.devkit.tizhi.TizhiNames`

```java
import net.guzhenren.devkit.tizhi.TizhiNames;

TizhiNames.register(100001, "示例体质");
```

### 2) “道”伤害分类：DamageType tags（*_dao）

你们的约定：**道痕识别依赖 tag**，tag ID 形如 `guzhenren:*_dao`。

规则：
- 文件路径固定：`data/guzhenren/tags/damage_type/<xxx>_dao.json`
- 必须 `"replace": false`（允许多个模组共同向同一个 tag 追加）

DevKit helper：`net.guzhenren.devkit.damage.GuzhenrenDamageTypeTags`

```java
import net.guzhenren.devkit.damage.GuzhenrenDamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

TagKey<DamageType> xingDao = GuzhenrenDamageTypeTags.XING_DAO;
TagKey<DamageType> hunDao = GuzhenrenDamageTypeTags.dao("hun_dao");
```

示例数据（Example 已包含）：
- 定义一个新 DamageType：
  - `sourceCode/guzhenren-devkit-example/src/main/resources/data/guzhenren_devkit_example/damage_type/devkit_example_xing_dao.json`
- 把它追加进 `guzhenren:xing_dao`：
  - `sourceCode/guzhenren-devkit-example/src/main/resources/data/guzhenren/tags/damage_type/xing_dao.json`

## 重要约束 / 不要踩坑

### 1) 主模组是 MCreator 导出

`sourceCode/guzhenren/` 大量文件会在构建时重生。
- 看到 `REGENERATED` 提示的文件：只在 `user code block` 区间内修改。
- 长期维护逻辑优先放到 DevKit / Addon。

### 2) 依赖来自本地 libs/

DevKit/Example 的依赖在 `mcreator.gradle` 使用 `flatDir { dirs 'libs' }` + `files('libs/xxx.jar')`。
- 不要随意删除或改名 `libs/` 下 jar。
- 不要擅自把依赖改成远程坐标（除非你确定要迁移依赖管理）。

### 3) Mixin 配置必须同步

新增 DevKit mixin：
- 需要添加到 `sourceCode/guzhenren-devkit/src/main/resources/guzhenren_devkit.mixins.json`
- 并确保 `sourceCode/guzhenren-devkit/src/main/resources/META-INF/neoforge.mods.toml` 里声明了 `[[mixins]]`

## FAQ

### Q1：为什么我在 Example 里改了 DevKit 代码却没生效？
- 确认 Example 的 `settings.gradle` 里有 `includeBuild('../guzhenren-devkit')`。
- 确认 Example 依赖坐标仍为 `net.guzhenren.devkit:guzhenren-devkit:0.1.0`（否则复合构建替换会失效）。

### Q2：启动时报 `ClassNotFound` / 找不到 guzhenren 相关类？
- DevKit 与 Example 都依赖 `libs/guzhenren-12.9.jar`（见各自 `mcreator.gradle`）。
- 确认 `libs/` 目录存在且 jar 文件名匹配。

### Q3：我的 tag 追加没生效？
- 确认 tag 文件路径是 `data/guzhenren/tags/damage_type/<xxx>_dao.json`。
- 确认 `"replace": false`。
- 确认 values 里写的是 `"<modid>:<damage_type_id>"`。
