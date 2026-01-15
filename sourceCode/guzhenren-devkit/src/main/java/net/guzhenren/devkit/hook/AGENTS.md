# net.guzhenren.devkit.hook

## OVERVIEW
- 通用 Hook 分发层：注册 handler → 排序 → 分发；通过 `HookResult` 控制是否消费事件。

## WHERE TO LOOK
- `HookRegistry`：注册、排序与分发（`register` / `dispatch` / `entries`）。
- `HookRegistration`：`owner` + `priority`。
- `HookHandler`：handler 接口。
- `HookResult`：`PASS` / `CONSUME`。

## CONVENTIONS
- 排序规则：`priority` 降序；同优先级按 `owner` 排序（见 `HookRegistry.ENTRY_ORDER`）。
- 短路规则：任一 handler 返回 `CONSUME` → 立刻停止后续分发并返回 `CONSUME`。
- 容错规则：handler 抛出的 `Throwable` 会被捕获并打印，然后当作 `PASS` 继续（不会中断链路）。
- 注册时机：registry 用 `CopyOnWriteArrayList`；写入会触发复制与排序，避免在每 tick/热路径里反复 `register`。
- 幂等性：重复 `register` 不会覆盖旧 handler（会新增一条 entry）；需要“替换”语义时应自行管理。

## ANTI-PATTERNS
- 用抛异常控制流程/回滚：这里会被吞掉，且只会打印。
- 在“只想观察/打点”的 handler 里返回 `CONSUME`：会把后续 handler 与原逻辑都拦截掉。
- 依赖 handler 执行顺序但不设 `priority`：会出现不可控的行为差异。
