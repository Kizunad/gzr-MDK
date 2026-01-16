package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.shop.ShopHooks;
import net.guzhenren.devkit.shop.ShangDianStockContext;

import net.guzhenren.procedures.ShangDianDangGaiGUIDaKaiShiMeiKeFaShengProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShangDianDangGaiGUIDaKaiShiMeiKeFaShengProcedure.class)
public class ShangDianStockHookMixin {
	@Inject(method = "execute", at = @At("TAIL"))
	private static void guzhenren_devkit$afterStock(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}
		if (!(player.containerMenu instanceof net.guzhenren.init.GuzhenrenModMenus.MenuAccessor menu)) {
			return;
		}

		for (int slotIndex = 0; slotIndex <= 23; slotIndex++) {
			ItemStack current = menu.getSlots().get(slotIndex).getItem();
			ShangDianStockContext ctx = new ShangDianStockContext(player, slotIndex, current.copy());
			net.guzhenren.devkit.hook.HookResult result = ShopHooks.SHANG_DIAN_STOCK.dispatch(ctx);
			if (result != net.guzhenren.devkit.hook.HookResult.CONSUME) {
				continue;
			}
			if (!ctx.overrideAllowed()) {
				continue;
			}
			ItemStack replacement = ctx.currentStock();
			if (replacement == null || replacement.isEmpty()) {
				continue;
			}
			menu.getSlots().get(slotIndex).set(replacement.copy());
			player.containerMenu.broadcastChanges();
		}
	}
}
