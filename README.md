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

## 支持的功能一览

DevKit 通过 **Mixin 注入** 捕获主模组关键流程，再通过 **HookRegistry** 分发给 Addon。

当前已支持（持续扩展中）：

- **杀招（ShaZhao）**
  - 按键触发（Z/X/C/V）、绑定确认、UI Tick：`net.guzhenren.devkit.shazhao.ShaZhaoHooks`
  - **HUD 图标注入**：`ShaZhaoHooks.HUD_ICON`（允许按 `boundId/boundItem` 返回自定义 `ResourceLocation` 图标）
- **地藏花（DiZangHua）**
  - Tag 扩展掉落池：`guzhenren:z1~z4`（稀有度）+ `guzhenren:<流派id>`（流派池）
  - **掉落替换 Hook**：`DiZangHuaHooks.DROP`（当前仅覆盖“流派地藏花”）
- **拍卖行（PaiMaiHang）**
  - 全局拍卖池 Tag：`guzhenren:paimaihang`
  - **拍品选择 Hook**：`PaiMaiHooks.SELECT_ITEM`（可替换本轮上架物品）
- **商店 / 收购（ShangDian / ShouGou）**
  - **收购卖出拦截 Hook**：`ShopHooks.SHOU_GOU_SELL`（允许/拒绝本次回收）
  - **商店库存注入 Hook**：`ShopHooks.SHANG_DIAN_STOCK`（注入/替换商店商品槽位）
  - 限制：目前覆盖 `ShangDian` 与 `ShouGouGui`；`Shangdian4/5/6/8/9` 等变体尚未接入；购买扣费/价格逻辑仍走主模组
- **炼蛊 / 任务 / 体质 / 道伤害**
  - 外部炼蛊配方：`net.guzhenren.devkit.liangu.*`
  - 任务按钮 Hook：`RenWuHooks`（见 `RenWuGuiButtonHookMixin`）
  - 体质显示名：`TizhiNames`
  - 道伤害分类：`data/guzhenren/tags/damage_type/<xxx>_dao.json`
- **工具**
  - TagKey/常用 tag：`TagKeyUtil` / `GuzhenrenTags`
  - ItemStack CustomData 读写：`CustomDataAccess`

示例代码入口：`sourceCode/guzhenren-devkit-example/src/main/java/net/guzhenren/devkit/example/`（`ExampleShaZhaoHudIcon` / `ExampleDiZangHua` / `ExamplePaiMai` / `ExampleShop` 等）。

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

### 0) 示例入口（建议先看）

- `sourceCode/guzhenren-devkit-example/src/main/java/net/guzhenren/devkit/example/`
  - `ExampleShaZhaoHudIcon`：杀招 HUD 图标注入
  - `ExampleDiZangHua`：地藏花掉落替换
  - `ExamplePaiMai`：拍卖行拍品选择替换
  - `ExampleShop`：收购拦截 + 商店库存注入

### 1) 体质显示名扩展（TizhiNames）

用途：让 Addon 自定义体质 ID 的显示名字（UI 里显示 `体质:<name>`）。

入口：`net.guzhenren.devkit.tizhi.TizhiNames`

```java
import net.guzhenren.devkit.tizhi.TizhiNames;

TizhiNames.register(100001, "示例体质");
```

### 2) “道”伤害分类：DamageType tags（*_dao）

> 其他新增 API（杀招 HUD / 地藏花 / 拍卖行 / 商店）已在上方「支持的功能一览」列出，后续会把这些 API 章节化补齐。


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

DevKit / Example 的关键依赖通过 `mcreator.gradle` 以本地 jar 方式引入（例如 `files('libs/guzhenren-12.9.jar')`）。

重要：这些 jar（蛊真人的模组和相关前置）**默认不随 git 提交**（体积/版权/分发限制），所以你克隆仓库后需要自己准备并放入对应目录。

本仓库会保留 `libs/` 目录占位（通过 `.gitkeep`），但目录里不会有 jar。

放置位置（两处都要有）：
- `sourceCode/guzhenren-devkit/libs/`
- `sourceCode/guzhenren-devkit-example/libs/`

需要的 jar 名称以各自的 `mcreator.gradle` 为准。当前版本示例（两边一致）：
- `guzhenren-12.9.jar`（主模组 jar）
- `geckolib-neoforge-1.21.1-4.7.7.jar`
- `Pehkui-3.8.3+1.21-neoforge.jar`
- `player-animation-lib-forge-2.0.1+1.21.1.jar`
- `attributefix-neoforge-1.21.1-21.1.2.jar`
- `bookshelf-neoforge-1.21.1-21.1.68.jar`
- `prickle-neoforge-1.21.1-21.1.10.jar`

校验方式：在对应模块目录执行 `./gradlew classes`，能编译通过就说明依赖齐全。

注意：
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
- 确认 tag 文件路径正确（例如道伤害：`data/guzhenren/tags/damage_type/<xxx>_dao.json`；拍卖池：`data/guzhenren/tags/items/paimaihang.json`）。
- 确认 `"replace": false`。
- 确认 values 里写的是 `"<modid>:<id>"`。
