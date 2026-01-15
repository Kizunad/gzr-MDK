package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;
import net.guzhenren.devkit.shazhao.ShaZhaoKeyContext;
import net.guzhenren.procedures.SanZhaoAnJianXAnXiaAnJianShiProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SanZhaoAnJianXAnXiaAnJianShiProcedure.class)
public class SanZhaoAnJianXHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (entity == null) {
			return;
		}
		HookResult result = ShaZhaoHooks.KEY_PRESS.dispatch(new ShaZhaoKeyContext(world, x, y, z, entity, 2));
		if (result == HookResult.CONSUME) {
			ci.cancel();
		}
	}
}
