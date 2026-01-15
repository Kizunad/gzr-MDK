package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoBindContext;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;
import net.guzhenren.devkit.shazhao.util.ShaZhaoMenuSlots;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.SanZhaoQueRen2Procedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SanZhaoQueRen2Procedure.class)
public class SanZhaoBind3HookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		ItemStack shazhaoItem = ShaZhaoMenuSlots.getSlot(player, 10);
		if (shazhaoItem.isEmpty()) {
			return;
		}
		HookResult result = ShaZhaoHooks.BIND.dispatch(new ShaZhaoBindContext(player, 3, shazhaoItem));
		if (result == HookResult.CONSUME) {
			GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
			vars.ShaZhao3 = shazhaoItem.copy();
			vars.markSyncDirty();
			ci.cancel();
		}
	}
}
