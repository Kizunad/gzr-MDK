package net.guzhenren.devkit.mixin;

import net.guzhenren.devkit.auction.PaiMaiHooks;
import net.guzhenren.devkit.auction.PaiMaiSelectItemContext;
import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.network.GuzhenrenModVariables;
import net.guzhenren.procedures.PaiMaiHangZhuLiuChengProcedure;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 新版拍卖行（mod本体_魂道智道更新）注入点。
 *
 * <p>旧版拍卖由 NPC 实体 tick 驱动（{@code PaiMaiHangDianYuanZaiShiTiKeGengXinShiProcedure}），
 * 该类已被移除。新版改为世界 tick 状态机：{@link PaiMaiHangZhuLiuChengProcedure} 监听
 * {@code LevelTickEvent.Post}，在 {@code PaiMaiHang_ZhuangTai == 1.0} 且计时达到阈值后，
 * 从 {@code JiPai1~6} 槽位或 {@code guzhenren:paimaihang} 标签随机选取拍品写入
 * {@code MapVariables.PaiMaiHang_PaiPin}，随后调用 {@code PaiMaiHangJiaGeProcedure.execute}
 * 计算价格。</p>
 *
 * <p>注入点选在 {@code PaiMaiHangJiaGeProcedure.execute(LevelAccessor)} 调用之前：
 * 此时拍品已最终确定、但价格尚未计算，允许附属模组替换拍品并让价格跟随新拍品重算。</p>
 */
@Mixin(PaiMaiHangZhuLiuChengProcedure.class)
public class PaiMaiSelectItemHookMixin {
	@Inject(
			method = "execute(Lnet/neoforged/bus/api/Event;Lnet/minecraft/world/level/LevelAccessor;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/guzhenren/procedures/PaiMaiHangJiaGeProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;)V",
					shift = At.Shift.BEFORE
			)
	)
	private static void guzhenren_devkit$beforeAuctionPrice(Event event, LevelAccessor world, CallbackInfo ci) {
		if (world == null || world.isClientSide()) {
			return;
		}

		GuzhenrenModVariables.MapVariables vars = GuzhenrenModVariables.MapVariables.get(world);
		ItemStack selected = vars.PaiMaiHang_PaiPin.copy();
		if (selected.isEmpty()) {
			return;
		}

		PaiMaiSelectItemContext ctx = new PaiMaiSelectItemContext(world, selected);
		HookResult result = PaiMaiHooks.SELECT_ITEM.dispatch(ctx);
		if (result != HookResult.CONSUME) {
			return;
		}

		ItemStack replacement = ctx.selectedItem();
		if (replacement == null || replacement.isEmpty()) {
			return;
		}

		vars.PaiMaiHang_PaiPin = replacement.copy();

		// 若 Handler 指定了价格覆盖，则在此写入世界变量。
		// 注入点位于 PaiMaiHangJiaGeProcedure.execute 之前，但该算价流程仅对匹配
		// guzhenren:paimaihang_jiage_XXw 标签的拍品赋价；替换成标签之外的物品时不会覆盖，
		// 故此处写入的价格得以保留，解决“替换自定义拍品后起拍价/竞价为 0”的问题。
		if (ctx.hasPriceOverride()) {
			vars.PaiMaiHang_jiage = ctx.price();
			if (ctx.bidIncrement() >= 0.0) {
				vars.PaiMaiHang_JingJia = ctx.bidIncrement();
			}
		}

		vars.markSyncDirty();
	}
}
