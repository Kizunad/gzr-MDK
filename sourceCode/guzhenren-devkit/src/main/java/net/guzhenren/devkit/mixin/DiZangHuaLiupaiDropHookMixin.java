package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.dizanghua.DiZangHuaDropContext;
import net.guzhenren.devkit.dizanghua.DiZangHuaHooks;

import net.guzhenren.procedures.LiupaidizanghuaDangYouJiFangKuaiShiProcedure;

import net.guzhenren.network.GuzhenrenModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiupaidizanghuaDangYouJiFangKuaiShiProcedure.class)
public class DiZangHuaLiupaiDropHookMixin {
	// 注意：新本体(12.9)的地藏花过程内部是一个"收敛 while 循环"——反复 setStackInSlot 直到
	// slot0 变成某个 z1~z4 标签物品才停止。若把替换注入点放在循环内的 setStackInSlot 之后，
	// 每写一次槽位就触发一次、且用非 z 标签物品(如钻石)覆盖 slot0 会导致循环终止条件永不成立 → 死循环/卡服。
	// 因此注入点改为 TAIL（整个过程执行结束后），此时 slot0 已是收敛后的合法掉落物，
	// 在此一次性替换为自定义掉落物即可，不会破坏循环也不会刷屏。
	/**
	 * 执行 after drop 操作。
	 */
	@Inject(method = "execute", at = @At("TAIL"))
	private static void guzhenren_devkit$afterDrop(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Player player)) {
			return;
		}
		if (player.level().isClientSide()) {
			return;
		}

		BlockPos pos = BlockPos.containing(x, y, z);
		ItemStack current;
		if (world instanceof ILevelExtension ext) {
			IItemHandler handler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			current = handler == null ? ItemStack.EMPTY : handler.getStackInSlot(0);
		} else {
			current = ItemStack.EMPTY;
		}
		if (current.isEmpty()) {
			return;
		}

		double qiyun = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES).qiyun;
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
		if (world instanceof ILevelExtension ext) {
			IItemHandler handler = ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (handler instanceof IItemHandlerModifiable mod) {
				ItemStack copy = replacement.copy();
				copy.setCount(1);
				mod.setStackInSlot(0, copy);
			}
		}
	}
}
