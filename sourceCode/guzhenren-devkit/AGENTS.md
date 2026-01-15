# guzhenren-devkit

## 模块定位
- DevKit：通过 **Mixin 注入**捕获主模组关键流程，再通过 **HookRegistry** 分发给 Addon/扩展逻辑。
- 目标：Mixin 只做“接线/拦截/构造上下文”，业务逻辑落在 `hook/*` 与领域包（`liangu/`、`shazhao/`、`quest/` 等）。

## 关键入口
- 构建与运行：`build.gradle`（NeoForge runs；含 `headless` / `verifyNoWindow`）。
- NeoForge 元数据：`src/main/resources/META-INF/neoforge.mods.toml`（含 `[[mixins]]`）。
- Mixin 配置：`src/main/resources/guzhenren_devkit.mixins.json`（`package=net.guzhenren.devkit.mixin`；包含 `refmap`）。
- Hook 核心：`src/main/java/net/guzhenren/devkit/hook/`（语义与排序约定见该目录 `AGENTS.md`）。
- 领域扩展：
  - 炼蛊：`src/main/java/net/guzhenren/devkit/liangu/`
  - 杀招：`src/main/java/net/guzhenren/devkit/shazhao/`
  - 任务：`src/main/java/net/guzhenren/devkit/quest/`

## 核心约定
- Mixin 层薄：不要把复杂分支/状态机塞进 `mixin/*`。
- `CONSUME` 代表“我接管了”：通常意味着 mixin 会 `cancel()` 原逻辑；只有确定要拦截时才返回。

## 依赖（flatDir libs）
- 依赖 jar 来自 `libs/`（见 `mcreator.gradle`）。常见模式：同一 jar 同时出现在 `compileOnly` 与 `runtimeOnly`。
- 除非明确要迁移依赖管理，否则不要擅自改成远程坐标/改写依赖配置。

## 常用命令（在本模块根目录）
```bash
./gradlew runClient
./gradlew runServer
./gradlew runVerifyNoWindow
./gradlew runHeadless
./gradlew build
```
