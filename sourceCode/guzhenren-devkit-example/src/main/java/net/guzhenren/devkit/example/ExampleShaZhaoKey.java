package net.guzhenren.devkit.example;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.shazhao.ShaZhaoHooks;

import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.entity.player.Player;

/**
 * Example（附属模组）- 杀招“触发阶段”示例。
 *
 * <p>目标：复用主模组原生按键（Z/X/C/V）释放杀招，但把执行逻辑替换/扩展为附属模组自己的实现。</p>
 *
 * <p>触发入口：主模组按键流程会走到 {@code SanZhaoAnJianZ/X/C/V...Procedure}；DevKit 在这些 Procedure 的开头注入，
 * 并 dispatch 到 {@code ShaZhaoHooks.KEY_PRESS}。</p>
 *
 * <p>本例逻辑（每个槽位独立）：
 * <ul>
 *   <li>先检查该槽位绑定的杀招ID是否等于 {@code ExampleShaZhaoBind.exampleId()}。</li>
 *   <li>生命周期：如果 {@code ShaZhaoN} 物品为空，则自动解绑（避免玩家取出物品后还能释放“幽灵杀招”）。</li>
 *   <li>冷却：按槽位写回主模组的冷却字段（Z->sanzhao_CD，X->sanzhao_CD1，C->sanzhao_CD2，V->sanzhao_CD3）。</li>
 *   <li>资源：示例消耗主模组常用的 {@code jingli}，不足则拒绝释放并打日志。</li>
 * </ul>
 * </p>
 */
public final class ExampleShaZhaoKey {
	private static final Logger LOGGER = LogUtils.getLogger();

	private ExampleShaZhaoKey() {
	}

	public static void bootstrap(String modId) {
		java.util.Set<Integer> slots = java.util.Set.of(1, 2, 3, 4);
		ShaZhaoHooks.registerKeyPress(modId, 100, slots, ctx -> {
			Player player = (ctx.entity() instanceof Player p) ? p : null;
			if (player == null) {
				return HookResult.PASS;
			}

			GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
			int slot = ctx.slotIndex();
			double currentId = net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.getBoundId(vars, slot);
			net.minecraft.world.item.ItemStack boundItem = net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.getBoundItem(vars, slot);
			return handleSlot(modId, player, vars, slot, currentId, boundItem);
		});
	}

	private static HookResult handleSlot(String modId, Player player, GuzhenrenModVariables.PlayerVariables vars, int slot, double currentId, net.minecraft.world.item.ItemStack boundItem) {
		if (currentId != ExampleShaZhaoBind.exampleId()) {
			return HookResult.PASS;
		}
		if (boundItem == null || boundItem.isEmpty()) {
			net.guzhenren.devkit.shazhao.util.ShaZhaoBindings.setBoundId(vars, slot, 0);
			vars.markSyncDirty();
			if (!player.level().isClientSide()) {
				LOGGER.info("[{}] example shazhao auto-unbound because ShaZhao{} is empty", modId, slot);
			}
			return HookResult.CONSUME;
		}

		double cd = net.guzhenren.devkit.shazhao.util.ShaZhaoCooldowns.getCooldownTicks(vars, slot);
		if (cd > 0) {
			if (!player.level().isClientSide()) {
				LOGGER.info("[{}] example shazhao blocked by cooldown: slot{} cd={}", modId, slot, cd);
			}
			return HookResult.CONSUME;
		}

		double costJingLi = 20d;
		if (vars.jingli < costJingLi) {
			if (!player.level().isClientSide()) {
				LOGGER.info("[{}] example shazhao blocked by resource: jingli={} < {}", modId, vars.jingli, costJingLi);
			}
			return HookResult.CONSUME;
		}
		vars.jingli = vars.jingli - costJingLi;

		double newCd = 60;
		net.guzhenren.devkit.shazhao.util.ShaZhaoCooldowns.setCooldownTicks(vars, slot, newCd);
		vars.markSyncDirty();

		if (!player.level().isClientSide()) {
			LOGGER.info("[{}] example shazhao triggered by slot{}: -{} jingli, cd={}", modId, slot, costJingLi, newCd);
		}
		return HookResult.CONSUME;

	}
}
