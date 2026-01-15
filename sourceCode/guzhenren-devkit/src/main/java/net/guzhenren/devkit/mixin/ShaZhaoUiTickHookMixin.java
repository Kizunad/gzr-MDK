package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;
import net.guzhenren.devkit.shazhao.ShaZhaoTickContext;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.UISHAZHAODangGaiGUIDaKaiShiMeiKeFaShengProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UISHAZHAODangGaiGUIDaKaiShiMeiKeFaShengProcedure.class)
public class ShaZhaoUiTickHookMixin {
	@Inject(method = "execute", at = @At("HEAD"))
	private static void guzhenren_devkit$hook(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
		ShaZhaoHooks.TICK.dispatch(new ShaZhaoTickContext(player, 1));
	}
}
