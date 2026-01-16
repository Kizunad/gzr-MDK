package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.shop.ShopHooks;
import net.guzhenren.devkit.shop.ShouGouSellContext;

import net.guzhenren.procedures.ShouGouGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShouGouGuiDangGaiGUIDaKaiShiMeiKeFaShengProcedure.class)
public class ShouGouGuiSellHookMixin {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void guzhenren_devkit$hook(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		if (!(player.containerMenu instanceof net.guzhenren.init.GuzhenrenModMenus.MenuAccessor menu)) {
			return;
		}
		ItemStack currency = menu.getSlots().get(1).getItem();
		ItemStack input = menu.getSlots().get(0).getItem();
		if (currency.isEmpty() || input.isEmpty()) {
			return;
		}

		int price = resolvePrice(input);
		if (price <= 0) {
			return;
		}

		ShouGouSellContext ctx = new ShouGouSellContext(player, input.copy(), currency, price, true);
		net.guzhenren.devkit.hook.HookResult result = ShopHooks.SHOU_GOU_SELL.dispatch(ctx);
		if (result != net.guzhenren.devkit.hook.HookResult.CONSUME) {
			return;
		}
		if (!ctx.allow()) {
			ci.cancel();
			return;
		}
	}

	private static int resolvePrice(ItemStack input) {
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_1")))) {
			return 1;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_5")))) {
			return 5;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_10")))) {
			return 10;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_50")))) {
			return 50;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_100")))) {
			return 100;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_200")))) {
			return 200;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_400")))) {
			return 400;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_600")))) {
			return 600;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_800")))) {
			return 800;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_1000")))) {
			return 1000;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_1200")))) {
			return 1200;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiage_1500")))) {
			return 1500;
		}
		if (input.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:jiiage_5000")))) {
			return 5000;
		}
		return 0;
	}
}
