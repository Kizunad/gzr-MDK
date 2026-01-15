# gzr-MDK / Guzhenren（蛊真人）MDK 说明

**生成时间**: 2026-01-13

## 概览
- 本目录是一个“外层包装”，真实的 NeoForge MDK 工程位于 `sourceCode/guzhenren/`。
- 该工程由 **MCreator** 导出：大量 Java/资源文件包含“user code block”与“REGENERATED”标记。

## 结构速览
```
./
└── sourceCode/
    ├── .gitignore              # 仅用于 sourceCode 子树的忽略规则
    └── guzhenren/              # NeoForge 1.21.1 工程（Gradle）
        ├── build.gradle        # net.neoforged.moddev + mcreator.gradle
        ├── gradle.properties   # 启用 configuration-cache
        ├── guzhenren.mcreator  # MCreator 工程文件（体积很大）
        ├── elements/           # MCreator 元素定义（大量文件）
        ├── src/main/java/      # Java 源码
        ├── src/main/resources/ # 资源包 + 数据包 + META-INF
        ├── run/                # 开发运行目录（世界/日志/崩溃）
        ├── build/              # Gradle 输出
        └── bin/                # IDE/编译输出（包含 main/test）
```

## 入口与定位
| 目标 | 位置 | 备注 |
|------|------|------|
| Mod 入口 | `sourceCode/guzhenren/src/main/java/net/guzhenren/GuzhenrenMod.java` | `@Mod("guzhenren")`，统一注册与网络入口 |
| NeoForge 元数据 | `sourceCode/guzhenren/src/main/resources/META-INF/neoforge.mods.toml` | 以此为准（Minecraft 1.21.1 / NeoForge 21.1.65） |
| 旧 Forge 元数据 | `sourceCode/guzhenren/src/main/resources/META-INF/mods.toml` | 看起来是旧版遗留（含 1.20.1/loaderVersion 47） |
| 生成注册表 | `sourceCode/guzhenren/src/main/java/net/guzhenren/init/` | 多数文件“每次构建会重生” |
| 逻辑热区 | `sourceCode/guzhenren/src/main/java/net/guzhenren/procedures/` | 6088 个过程类；用 `rg` 定位 |
| 网络层 | `sourceCode/guzhenren/src/main/java/net/guzhenren/network/` | 通过 `GuzhenrenMod.addNetworkMessage` 注册 |
| 资源热点 | `sourceCode/guzhenren/src/main/resources/assets/guzhenren/models/item/` | 3934 个 item 模型 JSON |

## 常用命令（在 `sourceCode/guzhenren/` 内执行）
```bash
./gradlew --version
./gradlew runClient
./gradlew runServer
./gradlew build
```

## 本项目特有约束（避免踩坑）
- **权限/发布约束**：不要假设能编译/发布 `guzhenren` 主模组；本仓库目标是提供“附属模组（Addon）/DevKit”开发路径，避免要求主模组新增依赖或暴露 API。
- **不要手改生成文件**：出现 `MCreator note: This file will be REGENERATED on each build.` 的文件，除非在 `Start of user code block ...` / `End of user code block ...` 区间内。
- **不要把运行产物当源码**：`run/`、`build/`、`bin/` 都是输出目录（世界、日志、编译产物），排查问题可以读，但不要在里面做“源码修改”。
- **元数据以 NeoForge 为准**：同目录存在 `mods.toml` 与 `neoforge.mods.toml`，实际版本/依赖以 `neoforge.mods.toml` 为准。
