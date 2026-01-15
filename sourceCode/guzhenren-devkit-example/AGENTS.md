# guzhenren-devkit-example

## 模块定位
- 示例 Addon：演示如何在独立工程中使用 `guzhenren-devkit` 的 API（炼蛊/杀招/体质名/数据包）。
- 代码风格以“可读、可复制、可删减”为第一优先级；避免把示例堆成生产逻辑。

## 构建与依赖（includeBuild + 坐标替换）
- 复合构建：`settings.gradle`
  - `includeBuild('../guzhenren-devkit')`
- 依赖坐标：`mcreator.gradle`
  - `implementation 'net.guzhenren.devkit:guzhenren-devkit:0.1.0'`
- 约束：如果调整 DevKit 的 `group/artifact/version`，必须同步更新 Example 的依赖坐标，否则 includeBuild 的替换会失效。

## 示例代码入口
- `src/main/java/net/guzhenren/devkit/example/GuzhenrenDevKitExampleMod.java`
  - 示例注册汇总（演示体质名注册、杀招三段示例、外部炼蛊配方注册）。
- `ExampleShaZhaoBind`：绑定阶段示例（向 `vars.sanzhaoN` 写入外部 ID）。
- `ExampleShaZhaoKey`：触发阶段示例（按键事件接管、资源/冷却检查、兜底自动解绑）。
- `ExampleShaZhaoLifecycle`：生命周期示例（UI tick 内一致性清理）。

## 数据包演示（src/main/resources/data）
- Tag 扩展：`data/guzhenren/tags/items/gufang.json`
  - 示例用于放宽炼蛊 UI 的蛊方槽（slot8）可放入的物品集合。
- 初始化触发：`data/guzhenren_devkit_example/advancement/init_markers.json`
  - `minecraft:tick` 触发后执行函数。
- 初始化函数：`data/guzhenren_devkit_example/functions/init_markers.mcfunction`
  - 演示写入玩家变量（用于 DevKit demo marker）。

## 常用命令（在本模块根目录）
```bash
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow
./gradlew build
```
