package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.dizanghua.DiZangHuaDropContext;
import net.guzhenren.devkit.dizanghua.DiZangHuaHooks;

import net.guzhenren.procedures.LiupaidizanghuaDangYouJiFangKuaiShiProcedure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiupaidizanghuaDangYouJiFangKuaiShiProcedure.class)
public class DiZangHuaLiupaiDropHookMixin {
	@Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/items/IItemHandlerModifiable;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER), cancellable = true)
	private static void guzhenren_devkit$afterSetStack(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		BlockPos pos = BlockPos.containing(x, y, z);
		ItemStack current;
		if (world instanceof net.neoforged.neoforge.common.extensions.ILevelExtension ext) {
			net.neoforged.neoforge.items.IItemHandler handler = ext.getCapability(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, pos, null);
			current = handler == null ? ItemStack.EMPTY : handler.getStackInSlot(0);
		} else {
			current = ItemStack.EMPTY;
		}
		if (current.isEmpty()) {
			return;
		}

		double qiyun = player.getData(net.guzhenren.network.GuzhenrenModVariables.PLAYER_VARIABLES).qiyun;
		String liupaiId = entity.getPersistentData().getString("流派id");

		int rarityTier = current.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:z4"))) ? 4
				: current.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:z3"))) ? 3
						: current.is(net.minecraft.tags.ItemTags.create(net.minecraft.resources.ResourceLocation.parse("guzhenren:z2"))) ? 2 : 1;

		DiZangHuaDropContext ctx = new DiZangHuaDropContext(world, pos, player, liupaiId, rarityTier, qiyun, current.copy());
		net.guzhenren.devkit.hook.HookResult result = DiZangHuaHooks.DROP.dispatch(ctx);
		if (result != net.guzhenren.devkit.hook.HookResult.CONSUME) {
			return;
		}

		ItemStack replacement = ctx.drop();
		if (replacement == null || replacement.isEmpty()) {
			return;
		}
		if (world instanceof net.neoforged.neoforge.common.extensions.ILevelExtension ext) {
			net.neoforged.neoforge.items.IItemHandler handler = ext.getCapability(net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK, pos, null);
			if (handler instanceof net.neoforged.neoforge.items.IItemHandlerModifiable mod) {
				ItemStack copy = replacement.copy();
				copy.setCount(1);
				mod.setStackInSlot(0, copy);
			}
		}
	}
}
