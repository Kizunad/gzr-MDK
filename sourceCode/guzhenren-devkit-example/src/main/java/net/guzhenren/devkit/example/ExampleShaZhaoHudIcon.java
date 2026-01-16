package net.guzhenren.devkit.example;

import net.guzhenren.devkit.shazhao.ShaZhaoHooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public final class ExampleShaZhaoHudIcon {
	private ExampleShaZhaoHudIcon() {
	}

	public static void bootstrap(String modId) {
		java.util.Set<Integer> slots = java.util.Set.of(1, 2, 3, 4);
		ShaZhaoHooks.registerHudIcon(modId, 0, slots, ctx -> {
			Player player = ctx.player();
			if (player == null) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}

			double boundId = ctx.boundId();
			if (boundId != ExampleShaZhaoBind.exampleId()) {
				return net.guzhenren.devkit.hook.HookResult.PASS;
			}

			ResourceLocation icon = ResourceLocation.parse("guzhenren:textures/screens/huo_yan_jin_jing_.png");
			return ctx.consumeWithIcon(icon);
		});
	}
}
