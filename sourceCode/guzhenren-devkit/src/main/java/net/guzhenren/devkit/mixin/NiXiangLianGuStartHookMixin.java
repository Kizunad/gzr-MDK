package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.nixiangliangu.NiXiangLianGuRecipes;
import net.guzhenren.devkit.nixiangliangu.ExternalNiXiangLianGuRecipe;
import net.guzhenren.procedures.NiLian1Procedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 逆向炼蛊“开始”注入点。
 *
 * <p>主模组 {@code NiLian1Procedure} 是逆炼 GUI 内“开始逆炼”按钮被点击时执行的过程
 * （原版会按 slot0 的蛊类型置 vars.LianGu=true 并设定 NiLian_sl）。
 * 本 Mixin 在 HEAD 拦截：若有外部配方 {@code matches} 命中，则调用其 {@code onStart} 并取消原版逻辑。
 */
@Mixin(NiLian1Procedure.class)
public class NiXiangLianGuStartHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		ExternalNiXiangLianGuRecipe recipe = NiXiangLianGuRecipes.findMatch(player);
		if (recipe == null) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		try {
			recipe.onStart(player);
		} catch (Throwable t) {
			System.err.println("[guzhenren-devkit] nixiang start error: " + recipe.id() + ": " + t);
			t.printStackTrace();
			return;
		}
		ci.cancel();
	}
}
