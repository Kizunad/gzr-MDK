package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.tizhi.TizhiNames;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.UishuxingshijuetiProcedure;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UishuxingshijuetiProcedure.class)
public class UishuxingshijuetiHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(Entity entity, CallbackInfoReturnable<String> cir) {
		if (entity == null) {
			return;
		}
		int id = (int) entity.getData(GuzhenrenModVariables.PLAYER_VARIABLES).tizhi;
		String name = TizhiNames.get(id);
		if (name == null) {
			return;
		}
		cir.setReturnValue("体质:" + name);
	}
}
