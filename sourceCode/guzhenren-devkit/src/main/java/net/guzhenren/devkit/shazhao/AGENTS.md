# net.guzhenren.devkit.shazhao

## 作用
- 复用主模组既有的杀招 UI 与按键链路（Z/X/C/V），通过 mixin 注入将“绑定/触发/生命周期”事件分发给 Addon。

## Hook 入口
- `ShaZhaoHooks`：
  - `KEY_PRESS`：按键触发（slotIndex=1..4 对应 Z/X/C/V）。
  - `BIND`：确认绑定（keySlotIndex=1..4；通常读取 UI slot10 的物品）。
  - `TICK`：杀招 UI tick（用于一致性/自动解绑等）。

## Context/Handler
- `ShaZhaoKeyContext`：`level,x,y,z,entity,slotIndex`。
- `ShaZhaoBindContext`：`player,keySlotIndex,shazhaoItem`（带 `isInShaZhaoMenu()`）。
- `ShaZhaoTickContext`：`player,keySlotIndex`。
- handler 接口：`ShaZhaoKeyHandler` / `ShaZhaoBindHandler` / `ShaZhaoTickHandler`（均继承 `HookHandler<...>`）。

## util（直接操作主模组 PlayerVariables 字段）
- `util/ShaZhaoBindings`：读取/写入 `vars.sanzhao1..4`（外部杀招 ID）与 `vars.ShaZhao1..4`（绑定物品快照）。
- `util/ShaZhaoCooldowns`：读取/写入 `vars.sanzhao_CD*`（槽位冷却 tick）。
- `util/ShaZhaoMenuSlots`：读取杀招 UI slot（例如 slot10）。

## 约定
- Addon 若写入外部 ID（`vars.sanzhaoN`），也要考虑“解绑一致性”：
  - UI tick 清理（`TICK`）+ 触发阶段兜底清理（`KEY_PRESS`）通常要配合使用。
- `KEY_PRESS/BIND/TICK` 都是可多方注册的：优先级决定先后，返回 `CONSUME` 会短路。

## 反模式
- 在 hook 回调里把 UI/物品规则写死到 mixin：应通过 `ShaZhaoHooks` 暴露给 Addon。
- 忽略槽位索引约定（1..4）：会导致按键映射错乱。
