package net.guzhenren.devkit.example;

import net.guzhenren.devkit.auction.PaiMaiHooks;

public final class ExamplePaiMai {
	private ExamplePaiMai() {
	}

	public static void bootstrap(String modId) {
		PaiMaiHooks.registerSelectItem(modId, 0, ctx -> {
			if (ctx.selectedItem().isEmpty()) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}
			if (Math.random() < 0.05) {
				ctx.setSelectedItem(new net.minecraft.world.item.ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse("minecraft:nether_star"))));
				return net.guzhenren.devkit.hook.HookResult.CONSUME;
			}
			return net.guzhenren.devkit.hook.HookResult.PASS;
		});
	}
}
