# gzr-MDK / Guzhenren DevKit

本仓库是《蛊真人（guzhenren）》的 **附属模组（Addon）开发套件**（DevKit MDK）。

核心目标：

- **不改主模组源码** 的前提下，通过 DevKit 的 **Mixin 注入 + Hook 分发** 扩展功能。
- 提供一个可直接运行的 **示例 Addon**，方便开发者复制改造。

> **当前适配主模组**：`mod本体_魂道智道更新`（魂道智道更新版本）。
> 开发套件已针对该版本对齐依赖、重写因主模组逻辑变化而失效的 Mixin 注入点，
> 并新增 **逆向炼蛊（NiLian）** Hook。运行环境为 NeoForge **21.1.234** / Minecraft 1.21.1 / Java 21。

## 本次适配变更（相较旧版 DevKit）

主模组升级到「魂道智道更新」后，部分旧的注入点/逻辑因主模组结构变化而**失效或不再适配**，已删除并改为新的实现。迁移要点如下：

### 删除 / 重写（适配新本体的必要改动）

- **拍卖行注入点重写**
  - 旧逻辑：注入主模组的拍卖行 NPC 实体 tick 过程 `PaiMaiHangDianYuanZaiShiTiKeGengXinShiProcedure`。
  - 该类在新本体中**已被移除**（拍卖改由世界 tick 状态机 `PaiMaiHangZhuLiuChengProcedure` 监听 `LevelTickEvent.Post` 驱动，拍品存于 `MapVariables.PaiMaiHang_PaiPin`）。
  - 新逻辑：`PaiMaiSelectItemHookMixin` 改为注入到 `PaiMaiHangJiaGeProcedure.execute` 调用**之前**（拍品已定、价格未算），并新增**定价能力**（`PaiMaiSelectItemContext.setPrice()` 写 `PaiMaiHang_jiage` / `PaiMaiHang_JingJia`），解决“替换自定义拍品后起拍价/竞价为 0”。

- **依赖与环境对齐**
  - NeoForge：`21.1.65` → **`21.1.234`**（满足 bookshelf ≥ 21.1.209，且对齐真实游戏环境）。
  - libs 版本对齐实际文件：geckolib `4.7.3`、bookshelf `21.1.80`、prickle `21.1.11`。
  - 主模组 jar：`guzhenren-12.9.jar`（旧别名）→ **`mod本体_魂道智道更新.jar`**（真实文件名，中文名），同步更新 devkit / example 两个 `mcreator.gradle` 的引用。

### 新增

- **逆向炼蛊（NiXiangLianGu / NiLian）Hook**：DevKit 框架原未实现，本次从零补齐 SPI + 2 个 Mixin 注入点 + example 示例（详见下方专属小节）。

> 其余 18/20 个旧 Mixin 注入点经字节码核验在新本体全部命中、**无需改动**；加上逆向炼蛊新增的 2 个，当前 Mixin 共 **22 个**。

## 你应该从哪里开始

- 想写附属模组：从 `sourceCode/guzhenren-devkit-example/` 开始。
- 想新增 DevKit 扩展点：看 `sourceCode/guzhenren-devkit/`。
- `sourceCode/guzhenren/` 是主模组工程（MCreator 导出），默认只用于本地验证与参考。

## 仓库结构

```
./
└── sourceCode/
    ├── guzhenren-devkit/          # DevKit：Mixin 注入 + Hook 分发
    ├── guzhenren-devkit-example/  # Example Addon：includeBuild 引入 DevKit
    └── guzhenren/                 # 主模组（MCreator 导出，谨慎修改）
```

## 支持的功能一览

DevKit 通过 **Mixin 注入** 捕获主模组关键流程，再通过 **HookRegistry / 注册表** 分发给 Addon。

当前已支持（持续扩展中）：

- **杀招（ShaZhao）**
  - 按键触发（Z/X/C/V）、绑定确认、UI Tick： `net.guzhenren.devkit.shazhao.ShaZhaoHooks`
  - **HUD 图标注入**： `ShaZhaoHooks.HUD_ICON`（允许按 `boundId/boundItem` 返回自定义 `ResourceLocation` 图标）
- **地藏花（DiZangHua）**
  - Tag 扩展掉落池： `guzhenren:z1~z4`（稀有度）\+ `guzhenren:<流派id>`（流派池）
  - **掉落替换 Hook**： `DiZangHuaHooks.DROP`（当前仅覆盖“流派地藏花”）
- **拍卖行（PaiMaiHang）**
  - 全局拍卖池 Tag： `guzhenren:paimaihang`
  - **拍品选择 Hook**： `PaiMaiHooks.SELECT_ITEM`（可替换本轮上架物品 + 通过 `PaiMaiSelectItemContext.setPrice()` 定价）
- **商店 / 收购（ShangDian / ShouGou）**
  - **收购卖出拦截 Hook**： `ShopHooks.SHOU_GOU_SELL`（允许/拒绝本次回收）
  - **商店库存注入 Hook**： `ShopHooks.SHANG_DIAN_STOCK`（注入/替换商店商品槽位）
  - 限制：目前覆盖 `ShangDian` 与 `ShouGouGui`； `Shangdian4/5/6/8/9` 等变体尚未接入；购买扣费/价格逻辑仍走主模组
- **炼蛊 / 逆向炼蛊 / 任务 / 体质 / 道伤害**
  - 外部炼蛊配方： `net.guzhenren.devkit.liangu.*`
  - **外部逆向炼蛊配方（新增）**： `net.guzhenren.devkit.nixiangliangu.*`（见下方专属小节）
  - 任务按钮 Hook： `RenWuHooks`（见 `RenWuGuiButtonHookMixin`）
  - 体质显示名： `TizhiNames`
  - 道伤害分类： `data/guzhenren/tags/damage_type/<xxx>_dao.json`
- **工具**
  - TagKey/常用 tag： `TagKeyUtil` / `GuzhenrenTags`
  - ItemStack CustomData 读写： `CustomDataAccess`

### 逆向炼蛊（NiXiangLianGu / NiLian）【新增】

主模组逆炼系统 `NiLian` 有独立 GUI（`NiLianGuiMenu` / `NiLianGuiScreen`），但**复用普通炼蛊共享变量**
`vars.LianGu` / `vars.GuFang` / `vars.LianGuJinDu`，且 `NiLianGuiMenu` 也实现 `GuzhenrenModMenus.MenuAccessor`
（槽位访问可复用 `LianGuMenuSlots`）。从普通炼蛊界面点按钮（`LianGuguiButtonMessage → NiLianGui1Procedure`）进入逆炼 GUI。

DevKit 将其镜像为一套 SPI（与炼蛊 `ExternalLianGuRecipe` 完全对称）：

- **外部配方接口**： `net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe`
  - `id()` / `matches(Player)` / `onStart(Player)` / `onTick(Player)` / `isActive(Player)`
  - 静态 `isRunning(Player, double guFangId)`：判断当前是否在该蛊方逆炼中（`vars.LianGu && vars.GuFang == guFangId`）
- **注册表**： `net.guzhenren.devkit.nixiangliangu.NiXiangLianGuRecipes`
  - `register(recipe)` / `findMatch(player)`（遍历 `matches`）/ `findActive(player)`（遍历 `isActive`）
- **Mixin 注入点**（已注册进 `guzhenren_devkit.mixins.json`）：
  - `NiXiangLianGuStartHookMixin`：注入 `NiLian1Procedure.execute(Entity)`，拦截“开始逆炼”
  - `NiXiangLianGuTickHookMixin`：注入 `NiLianGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure.execute(LevelAccessor, double, double, double, Entity)`，拦截“每 tick 推进”

示例代码入口： `sourceCode/guzhenren-devkit-example/src/main/java/net/guzhenren/devkit/example/`
（ `ExampleShaZhaoHudIcon` / `ExampleDiZangHua` / `ExamplePaiMai` / `ExampleShop` / `ExampleNiXiangLianGu` 等）。

## 环境要求

- Java： **21**（项目使用 toolchain）
- Minecraft： **1.21.1**
- NeoForge： **21.1.234**（已对齐真实游戏环境，并满足 bookshelf ≥ 21.1.209 的要求）

## 快速开始（推荐路径：Example Addon）

Example 模块通过 Gradle 复合构建直接引用 DevKit：

- `sourceCode/guzhenren-devkit-example/settings.gradle`： `includeBuild('../guzhenren-devkit')`
- `sourceCode/guzhenren-devkit-example/mcreator.gradle`： `implementation 'net.guzhenren.devkit:guzhenren-devkit:0.1.0'`

在 Example 目录运行：

```
cd sourceCode/guzhenren-devkit-example
./gradlew runClient
```

> 运行前请确保本地 `libs/` 已放入主模组 jar `mod本体_魂道智道更新.jar` 及相关前置（见下方「依赖来自本地 libs/」）。

## 常用命令

### DevKit

```
cd sourceCode/guzhenren-devkit
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow   # server + --nogui（无窗口加载验证）
./gradlew runHeadless         # 尽量减少弹窗/无 GPU 环境暴露错误（仍可能触发窗口）
./gradlew build
```

### Example Addon

```
cd sourceCode/guzhenren-devkit-example
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow
./gradlew build
```

### 主模组（仅当你需要验证/参考）

```
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
  - `ExamplePaiMai`：拍卖行拍品选择替换 + 定价
  - `ExampleShop`：收购拦截 \+ 商店库存注入
  - `ExampleNiXiangLianGu`：逆向炼蛊示例（slot0 放钻石 → 开始逆炼 → 约 5 秒在 slot2 产出泥土）

### 1) 体质显示名扩展（TizhiNames）

用途：让 Addon 自定义体质 ID 的显示名字（UI 里显示 `体质:<name>`）。

入口： `net.guzhenren.devkit.tizhi.TizhiNames`

```java
import net.guzhenren.devkit.tizhi.TizhiNames;

TizhiNames.register(100001, "示例体质");
```

### 2) “道”伤害分类：DamageType tags（\*\_dao）

> 其他新增 API（杀招 HUD / 地藏花 / 拍卖行 / 商店 / 逆向炼蛊）已在上方「支持的功能一览」列出。

你们的约定： **道痕识别依赖 tag**，tag ID 形如 `guzhenren:*_dao`。

规则：

- 文件路径固定： `data/guzhenren/tags/damage_type/<xxx>_dao.json`
- 必须 `"replace": false`（允许多个模组共同向同一个 tag 追加）

DevKit helper： `net.guzhenren.devkit.damage.GuzhenrenDamageTypeTags`

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

### 3) 逆向炼蛊外部配方（NiXiangLianGu）【新增】

逆向炼蛊复用炼蛊共享变量，提供与炼蛊对称的 SPI。实现 `ExternalNiXiangLianGuRecipe` 后注册即可接管逆炼 GUI 的“开始”与“每 tick”流程：

```java
import net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe;
import net.guzhenren.devkit.nixiangliangu.NiXiangLianGuRecipes;
import net.minecraft.world.entity.player.Player;

public class MyNiXiangRecipe implements ExternalNiXiangLianGuRecipe {
    @Override public String id() { return "mymod:nixiang_demo"; }

    // 是否接管本次逆炼（如：检查 slot0 是否为目标物品、是否在特定蛊方）
    @Override public boolean matches(Player player) { /* ... */ }

    // 点击“开始逆炼”时调用：置 LianGuJinDu=0 / GuFang=<你的蛊方 id> / LianGu=true
    @Override public void onStart(Player player) { /* ... */ }

    // 每 tick 调用：推进 LianGuJinDu，满档后在对应槽位产出
    @Override public void onTick(Player player) { /* ... */ }

    // 是否仍处于本配方逆炼中（供每 tick Mixin 筛选）
    @Override public boolean isActive(Player player) { /* ... */ }
}

// 注册
NiXiangLianGuRecipes.register(new MyNiXiangRecipe());
```

> 静态工具 `ExternalNiXiangLianGuRecipe.isRunning(player, guFangId)` 可直接判断“玩家是否正在 `guFangId` 这个蛊方逆炼中”。

## 重要约束 / 不要踩坑

### 1) 主模组是 MCreator 导出

`sourceCode/guzhenren/` 大量文件会在构建时重生。

- 看到 `REGENERATED` 提示的文件：只在 `user code block` 区间内修改。
- 长期维护逻辑优先放到 DevKit / Addon。

### 2) 依赖来自本地 libs/

DevKit / Example 的关键依赖通过 `mcreator.gradle` 以本地 jar 方式引入（例如 `files('libs/mod本体_魂道智道更新.jar')`）。

重要：这些 jar（蛊真人的模组和相关前置） **默认不随 git 提交**（体积/版权/分发限制），所以你克隆仓库后需要自己准备并放入对应目录。

本仓库会保留 `libs/` 目录占位（通过 `.gitkeep`），但目录里不会有 jar。

放置位置（两处都要有）：

- `sourceCode/guzhenren-devkit/libs/`
- `sourceCode/guzhenren-devkit-example/libs/`

需要的 jar 名称以各自的 `mcreator.gradle` 为准。当前版本（两边一致）：

- `mod本体_魂道智道更新.jar`（主模组 jar，文件名已用真实名，不再沿用旧别名 `guzhenren-12.9.jar`）
- `geckolib-neoforge-1.21.1-4.7.3.jar`
- `Pehkui-3.8.3+1.21-neoforge.jar`
- `player-animation-lib-forge-2.0.1+1.21.1.jar`
- `attributefix-neoforge-1.21.1-21.1.2.jar`
- `bookshelf-neoforge-1.21.1-21.1.80.jar`
- `prickle-neoforge-1.21.1-21.1.11.jar`

校验方式：在对应模块目录执行 `./gradlew classes`，能编译通过就说明依赖齐全。

注意：

- 主模组 jar 为**中文文件名**，Gradle 在 Windows 上可正常解析，**请勿改回英文名或破坏文件名**（否则 `mcreator.gradle` 的 `files('libs/mod本体_魂道智道更新.jar')` 将找不到文件）。
- 不要随意删除或改名 `libs/` 下 jar。
- 不要擅自把依赖改成远程坐标（除非你确定要迁移依赖管理）。

### 3) Mixin 配置必须同步

新增 DevKit mixin：

- 需要添加到 `sourceCode/guzhenren-devkit/src/main/resources/guzhenren_devkit.mixins.json`
- 并确保 `sourceCode/guzhenren-devkit/src/main/resources/META-INF/neoforge.mods.toml` 里声明了 `[[mixins]]`

逆向炼蛊新增的两个 Mixin 名称（已注册）： `NiXiangLianGuStartHookMixin`、 `NiXiangLianGuTickHookMixin`。

## FAQ

### Q1：为什么我在 Example 里改了 DevKit 代码却没生效？

- 确认 Example 的 `settings.gradle` 里有 `includeBuild('../guzhenren-devkit')`。
- 确认 Example 依赖坐标仍为 `net.guzhenren.devkit:guzhenren-devkit:0.1.0`（否则复合构建替换会失效）。

### Q2：启动时报 `ClassNotFound` / 找不到 guzhenren 相关类？

- DevKit 与 Example 都依赖 `libs/mod本体_魂道智道更新.jar`（见各自 `mcreator.gradle`，文件名已用真实名）。
- 确认 `libs/` 目录存在且 jar **文件名完全匹配**（含中文名 `mod本体_魂道智道更新.jar`）。

### Q3：我的 tag 追加没生效？

- 确认 tag 文件路径正确（例如道伤害： `data/guzhenren/tags/damage_type/<xxx>_dao.json`；拍卖池： `data/guzhenren/tags/items/paimaihang.json`）。
- 确认 `"replace": false`。
- 确认 values 里写的是 `"<modid>:<id>"`。

### Q4：怎么扩展逆向炼蛊（NiLian）？

- 实现 `net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe`，在 `onStart`/`onTick` 中操作玩家的炼蛊共享变量与逆炼 GUI 槽位，然后 `NiXiangLianGuRecipes.register(...)` 注册即可，无需写 Mixin。
- 参考示例 `ExampleNiXiangLianGu`（slot0 放钻石 → 开始逆炼 → 约 5 秒在 slot2 产出泥土）。
