package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.liangu.LianGuRecipes;
import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;
import net.guzhenren.procedures.KaishilianzhianniuProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KaishilianzhianniuProcedure.class)
public class KaishilianzhianniuHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(LevelAccessor world, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		ExternalLianGuRecipe recipe = LianGuRecipes.findMatch(player);
		if (recipe == null) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		try {
			recipe.onStart(player);
		} catch (Throwable t) {
			System.err.println("[guzhenren-devkit] liangu start error: " + recipe.id() + ": " + t);
			t.printStackTrace();
			return;
		}
		ci.cancel();
	}
}
