package net.guzhenren.devkit.example;

import net.guzhenren.devkit.shop.ShopHooks;

public final class ExampleShop {
	private ExampleShop() {
	}

	public static void bootstrap(String modId) {
		ShopHooks.registerShouGouSell(modId, 0, ctx -> {
			if (ctx.price() >= 1000) {
				ctx.setAllow(false);
				return net.guzhenren.devkit.hook.HookResult.CONSUME;
			}
			return net.guzhenren.devkit.hook.HookResult.PASS;
		});

		ShopHooks.registerShangDianStock(modId, 0, ctx -> {
			if (ctx.shopSlotIndex() != 0) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}
			ctx.setCurrentStock(new net.minecraft.world.item.ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse("minecraft:emerald"))));
			return net.guzhenren.devkit.hook.HookResult.CONSUME;
		});
	}
}
