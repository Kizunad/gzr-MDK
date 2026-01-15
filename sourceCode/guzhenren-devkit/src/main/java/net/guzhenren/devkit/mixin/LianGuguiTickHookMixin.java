package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.liangu.LianGuRecipes;
import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.LianGuguiDangGaiGUIDaKaiShiMeiKeFaShengProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LianGuguiDangGaiGUIDaKaiShiMeiKeFaShengProcedure.class)
public class LianGuguiTickHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
		if (!vars.LianGu) {
			return;
		}
		ExternalLianGuRecipe recipe = LianGuRecipes.findActive(player);
		if (recipe == null) {
			return;
		}
		try {
			recipe.onTick(player);
		} catch (Throwable t) {
			System.err.println("[guzhenren-devkit] liangu tick error: " + recipe.id() + ": " + t);
			t.printStackTrace();
			return;
		}
		ci.cancel();
	}
}
