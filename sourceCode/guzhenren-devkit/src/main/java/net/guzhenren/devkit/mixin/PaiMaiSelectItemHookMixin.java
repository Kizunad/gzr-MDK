package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.auction.PaiMaiHooks;
import net.guzhenren.devkit.auction.PaiMaiSelectItemContext;

import net.guzhenren.procedures.PaiMaiHangDianYuanZaiShiTiKeGengXinShiProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaiMaiHangDianYuanZaiShiTiKeGengXinShiProcedure.class)
public class PaiMaiSelectItemHookMixin {
	@Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/guzhenren/network/GuzhenrenModVariables$PlayerVariables;markSyncDirty()V", shift = At.Shift.AFTER), cancellable = true)
	private static void guzhenren_devkit$afterSelectItem(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (entity == null) {
			return;
		}
		if (entity.level().isClientSide()) {
			return;
		}

		ItemStack selected = entity.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES).PaiMaiHang_PaiPin.copy();
		if (selected.isEmpty()) {
			return;
		}

		PaiMaiSelectItemContext ctx = new PaiMaiSelectItemContext(world, x, y, z, entity, selected);
		net.guzhenren.devkit.hook.HookResult result = PaiMaiHooks.SELECT_ITEM.dispatch(ctx);
		if (result != net.guzhenren.devkit.hook.HookResult.CONSUME) {
			return;
		}

		ItemStack replacement = ctx.selectedItem();
		if (replacement == null || replacement.isEmpty()) {
			return;
		}

		net.guzhenren.network.GuzhenrenModVariables.PlayerVariables vars = entity.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES);
		vars.PaiMaiHang_PaiPin = replacement.copy();
		vars.markSyncDirty();
	}
}
