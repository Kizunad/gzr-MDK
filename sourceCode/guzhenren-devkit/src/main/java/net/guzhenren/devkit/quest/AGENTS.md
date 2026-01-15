# net.guzhenren.devkit.quest

## OVERVIEW
- 任务 UI 按钮拦截点：把“按钮点击/网络消息处理”分发给 Addon handler。

## WHERE TO LOOK
- `RenWuHooks`：注册入口（`BUTTON` registry）。
- `RenWuButtonContext`：事件上下文（`player, buttonId, x, y, z`）。
- 注入点：`net.guzhenren.devkit.mixin.RenWuGuiButtonHookMixin`（拦截 `RenWuGuiButtonMessage.handleButtonAction`）。

## CONVENTIONS
- handler 必须先过滤自己关心的 `buttonId`；不匹配则返回 `PASS`。
- 返回 `CONSUME` 会导致 mixin 侧 `ci.cancel()`：原始按钮逻辑不会继续执行。
- `x/y/z` 的含义以主模组按钮消息为准：当作“不透明参数”处理更安全，避免擅自假设是世界坐标。

## ANTI-PATTERNS
- 不加过滤地对所有 `buttonId` 返回 `CONSUME`：会把 UI 完全“吃掉”。
- 在 handler 里引入客户端/UI 依赖：该注入点是网络侧处理，默认按 server 语义编写更稳。
