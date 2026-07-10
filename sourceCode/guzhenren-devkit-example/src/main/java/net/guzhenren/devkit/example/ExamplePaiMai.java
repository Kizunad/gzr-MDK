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
		if (Math.random() < 1.0) {
			System.out.println("[guzhenren-devkit-example] 拍卖Hook触发: 原拍品=" + ctx.selectedItem() + " -> 替换为下界之星, 起拍价=500000 竞价=50000");
			ctx.setSelectedItem(new net.minecraft.world.item.ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse("minecraft:nether_star"))));
			// 下界之星不在主模组 paimaihang_jiage_XXw 价格标签内，主模组算价流程不会赋价，
			// 因此必须由 Hook 主动定价，否则起拍价/竞价为 0。
			ctx.setPrice(500000.0, 50000.0);
			return net.guzhenren.devkit.hook.HookResult.CONSUME;
		}
			return net.guzhenren.devkit.hook.HookResult.PASS;
		});
	}
}
