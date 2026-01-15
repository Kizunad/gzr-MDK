# net.guzhenren.devkit.mixin

## 这目录是做什么的
- 这里的类全部是 **Mixin 注入点**：把“主模组流程/网络消息/Procedure”转成 DevKit 的 Hook 事件并分发。
- 配置链路（缺一不可）：
  - `src/main/resources/META-INF/neoforge.mods.toml` → `[[mixins]] config = "guzhenren_devkit.mixins.json"`
  - `src/main/resources/guzhenren_devkit.mixins.json`（`package=net.guzhenren.devkit.mixin` + mixin 列表 + `refmap`）

## 注入点索引（按领域）
炼蛊（ExternalLianGuRecipe）：
- `KaishilianzhianniuHookMixin`：拦截“开始炼制”入口，匹配 `LianGuRecipes.findMatch(player)`，server side 调用 `recipe.onStart` 后 `ci.cancel()`。
- `LianGuguiTickHookMixin`：拦截炼蛊 UI tick；当 `vars.LianGu` 且 `LianGuRecipes.findActive(player)!=null` 时调用 `recipe.onTick` 并 `ci.cancel()`。

杀招（ShaZhaoHooks）：
- `SanZhaoAnJianZHookMixin` / `X` / `C` / `V`：按键触发阶段，分别 dispatch `ShaZhaoKeyContext(..., slotIndex=1/2/3/4)`。
- `SanZhaoBind1HookMixin` / `2` / `3` / `4`：绑定确认阶段（slot10 取物品），dispatch `ShaZhaoBindContext(player, keySlotIndex=1..4, shazhaoItem)`；若返回 `CONSUME`，写回 `vars.ShaZhaoN` 并 `ci.cancel()`。
- `ShaZhaoUiTickHookMixin` / `ShaZhao2UiTickHookMixin` / `ShaZhao3UiTickHookMixin` / `ShaZhao4UiTickHookMixin`：杀招 UI 每 tick dispatch `ShaZhaoTickContext(player, keySlotIndex=1..4)`。

任务（RenWuHooks）：
- `RenWuGuiButtonHookMixin`：拦截 `RenWuGuiButtonMessage.handleButtonAction`，dispatch `RenWuButtonContext(player, buttonId, x, y, z)`；`CONSUME` 则 `ci.cancel()`。

体质（TizhiNames）：
- `UishuxingshijuetiHookMixin`：在 UI 文本生成处读取 `vars.tizhi`，若 `TizhiNames.get(id)` 非空则直接 `setReturnValue("体质:" + name)`。

## 编写/修改 mixin 的约定
- Mixin 只做：取参数/校验 side/构造 context/dispatch/必要时 cancel。
- 任何“可复用的规则”应下沉到 `hook/*` 或领域包（`liangu/`、`shazhao/`、`quest/`）。
- 注意 `guzhenren_devkit.mixins.json` 的 `injectors.defaultRequire=1`：注入点失配会直接报错；改注入点要非常谨慎。
- `refmap` 是运行时必要文件：`guzhenren_devkit.mixins.json` 里声明了 `refmap`，缺失会导致生产环境应用 mixin 失败。
