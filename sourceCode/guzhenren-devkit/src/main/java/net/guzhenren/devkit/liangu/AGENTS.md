# net.guzhenren.devkit.liangu

## OVERVIEW
- 外部炼蛊配方入口：Addon 注册 `ExternalLianGuRecipe`，在“开始炼制/炼蛊 UI tick”链路中接管逻辑。

## WHERE TO LOOK
- `spi/ExternalLianGuRecipe`：配方接口（`matches` / `onStart` / `onTick` / `isActive`）。
- `LianGuRecipes`：全局注册表（容错遍历）。
- `LianGuMenuSlots`：炼蛊界面槽位读写（基于 `GuzhenrenModMenus.MenuAccessor`）。
- 注入点：`net.guzhenren.devkit.mixin.KaishilianzhianniuHookMixin`、`net.guzhenren.devkit.mixin.LianGuguiTickHookMixin`。

## CONVENTIONS
- 抢占入口：`matches(player)` 用于“开始炼制”匹配；`isActive(player)` 用于 tick 时找到当前接管者。
- server side 执行：mixin 侧在 `player.level().isClientSide()==false` 时才调用 `onStart/onTick`。
- 槽位约定：输入区通常为 `slot0..7`；`slot8` 常用作蛊方槽；`slot9/10` 在示例中用于产出/展示（以主模组 UI 为准）。
- 运行态约定：`ExternalLianGuRecipe.isRunning(player, guFangId)` 通过 `vars.LianGu && vars.GuFang==guFangId` 判断运行态。
- 容错：`LianGuRecipes.findMatch/findActive` 会捕获异常并打印；配方实现应尽量可预期地返回 false/null，而不是依赖异常。

## ANTI-PATTERNS
- 在 `matches` 做不可逆副作用（消耗物品/写变量）：应放到 `onStart`。
- 直接假设 `player.containerMenu` 结构：优先用 `LianGuMenuSlots` 做 guard。
- 在每 tick 动态注册配方：`LianGuRecipes` 是全局列表，应在模组初始化阶段注册一次。
