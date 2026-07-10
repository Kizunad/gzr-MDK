package net.guzhenren.devkit.example;

import net.guzhenren.devkit.dizanghua.DiZangHuaHooks;
import net.guzhenren.devkit.hook.HookResult;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExampleDiZangHua {
	/**
	 * 构造方法。
	 */
	private ExampleDiZangHua() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDiZangHua.class);
	private static boolean LOGGED = false;

	/**
	 * 使用给定 mod id 将该示例注册到 DevKit。
	 */
	public static void bootstrap(String modId) {
		// 放宽验证（测试用）：原条件为 rarityTier>=3 且 liupaiId=="huodao" 才替换，正常玩法极难触发。
		// 这里改为无条件把掉落替换为钻石，仅用于确认 DiZangHua Mixin 注入链路 + Handler 在新本体生效。
		// 正式接入时，可加回条件：if (ctx.rarityTier() < 3 || !"huodao".equals(ctx.liupaiId())) return HookResult.PASS;
		DiZangHuaHooks.registerDrop(modId, 0, ctx -> {
			ctx.setDrop(new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("minecraft:diamond"))));
			// 注入点已移到过程 TAIL（收敛循环之后），每次右键触发一次，日志仅首条避免刷屏。
			if (!ctx.player().level().isClientSide() && !LOGGED) {
				LOGGED = true;
				LOGGER.info("[{}] dizanghua hook triggered: drop replaced -> DIAMOND (liupai={}, rarityTier={})",
						modId, ctx.liupaiId(), ctx.rarityTier());
			}
			return HookResult.CONSUME;
		});
	}
}
