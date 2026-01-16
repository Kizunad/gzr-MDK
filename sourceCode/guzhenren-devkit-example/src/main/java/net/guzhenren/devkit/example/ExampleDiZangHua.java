package net.guzhenren.devkit.example;

import net.guzhenren.devkit.dizanghua.DiZangHuaHooks;

import net.minecraft.resources.ResourceLocation;

public final class ExampleDiZangHua {
	private ExampleDiZangHua() {
	}

	public static void bootstrap(String modId) {
		DiZangHuaHooks.registerDrop(modId, 0, ctx -> {
			if (ctx.rarityTier() < 3) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}
			if (!"huodao".equals(ctx.liupaiId())) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}
			ctx.setDrop(new net.minecraft.world.item.ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ResourceLocation.parse("minecraft:diamond"))));
			return net.guzhenren.devkit.hook.HookResult.CONSUME;
		});
	}
}
