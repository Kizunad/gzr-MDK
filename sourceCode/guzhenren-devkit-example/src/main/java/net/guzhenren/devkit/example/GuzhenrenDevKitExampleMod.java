package net.guzhenren.devkit.example;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.guzhenren.devkit.hook.HookResult;
import net.guzhenren.devkit.liangu.LianGuInputMode;
import net.guzhenren.devkit.liangu.LianGuMenuSlots;
import net.guzhenren.devkit.liangu.LianGuRecipes;
import net.guzhenren.devkit.liangu.spi.ExternalLianGuRecipe;
import net.guzhenren.init.GuzhenrenModItems;
import net.guzhenren.network.GuzhenrenModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.neoforged.fml.common.Mod;

// 示例附属模组：演示如何使用 guzhenren-devkit 的注入扩展点。
//
// 这个文件刻意写得“可教学”：你可以直接复制它，改动很少就能做出自己的配方/杀招。
//
// 测试目标：
// 1) 炼蛊：slot8 放 kong_bai_gu_fang；消耗 1 个钻石 -> 产出 1 个泥土。
// 2) 杀招：按 Z 键（slotIndex=1）时触发一个可观察的效果（此处用变量标记演示）。
@Mod(GuzhenrenDevKitExampleMod.MODID)
public class GuzhenrenDevKitExampleMod {
	public static final String MODID = "guzhenren_devkit_example";

	private static final Logger LOGGER = LogUtils.getLogger();

	// 外部配方 ID：主模组内置 GuFang 通常是较小整数；示例约定 addon 从 100000 起，避免冲突。
	private static final double EXAMPLE_GUFANG_ID = 100000d;

	// 炼蛊输入槽策略（允许开发者选择）：
	// - SLOT0_ONLY：必须把材料放在 slot0（更贴近主模组“指定槽位”体验）
	// - ANY_INPUT_SLOT：允许材料放在 slot0..7 任意输入槽（更适合调试/快速验证）
	private static final LianGuInputMode INPUT_MODE = LianGuInputMode.SLOT0_ONLY;

	// 炼蛊耗时：进度变量每秒 +1，这里 5 秒完成。
	private static final double LIANGU_SECONDS = 5d;

	public GuzhenrenDevKitExampleMod() {
		LOGGER.info("[{}] loaded", MODID);
		LOGGER.info("[{}] usage(liangu): slot8=kong_bai_gu_fang, input=DIAMOND -> output=DIRT", MODID);
		LOGGER.info("[{}] liangu input mode: {}", MODID, INPUT_MODE);

		net.guzhenren.devkit.tizhi.TizhiNames.register(100001, "示例体质");

		ExampleShaZhaoKey.bootstrap(MODID);
		ExampleShaZhaoBind.bootstrap(MODID);
		ExampleShaZhaoLifecycle.bootstrap(MODID);
		bootstrapLianGu();
	}


	private static void bootstrapLianGu() {
		// 外部炼蛊配方的入口：DevKit 在 “点击开始炼制” 的路径上用 Mixin 注入。
		// 当 matches(player) 返回 true 时，DevKit 会让这个外部配方接管开始/每tick推进。
		LianGuRecipes.register(new ExternalLianGuRecipe() {
			@Override
			public String id() {
				return MODID + ":liangu_example";
			}

			@Override
			public boolean matches(Player player) {
				// 已经处于炼蛊中就不允许重新开始。
				if (player.getData(GuzhenrenModVariables.PLAYER_VARIABLES).LianGu) {
					return false;
				}

				// 必须在炼蛊界面里。
				if (!LianGuMenuSlots.isInLianGuMenu(player)) {
					return false;
				}

				// 炼蛊界面 slot8 是“蛊方槽位”。本例要求放入 kong_bai_gu_fang。
				// 注意：slot8 有 Tag 限制（guzhenren:gufang）。example 已通过 data pack 扩展该 tag 以允许 kong_bai_gu_fang。
				ItemStack slot8 = LianGuMenuSlots.getSlot(player, 8);
				if (slot8.isEmpty() || slot8.getItem() != GuzhenrenModItems.KONG_BAI_GU_FANG.get()) {
					return false;
				}

				// 找到可消耗的钻石输入槽。
				return findDiamondSlot(player) != -1;
			}

			@Override
			public void onStart(Player player) {
				int inputSlot = findDiamondSlot(player);
				if (inputSlot == -1) {
					return;
				}

				ItemStack input = LianGuMenuSlots.getSlot(player, inputSlot);
				if (input.isEmpty()) {
					return;
				}

				// 消耗 1 个钻石（不要求堆叠数==1）。
				ItemStack consumed = input.copy();
				consumed.setCount(1);

				ItemStack remaining = input.copy();
				remaining.shrink(1);

				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);
				vars.LianGuJinDu = 0;
				vars.lg_ke = 0;
				vars.GuFang = EXAMPLE_GUFANG_ID;
				vars.LianGu = true;
				vars.markSyncDirty();

				// 为了让“材料确实被消耗”更直观：把被消耗的 1 个钻石放到 slot10（只读槽）。
				LianGuMenuSlots.setSlot(player, 10, consumed);
				LianGuMenuSlots.setSlot(player, inputSlot, remaining.isEmpty() ? ItemStack.EMPTY : remaining);

				if (!player.level().isClientSide()) {
					LOGGER.info("[{}] liangu start ok: consumed 1x DIAMOND from slot{}", MODID, inputSlot);
				}
			}

			@Override
			public void onTick(Player player) {
				GuzhenrenModVariables.PlayerVariables vars = player.getData(GuzhenrenModVariables.PLAYER_VARIABLES);

				// lg_ke 是 tick 计数器（20 tick = 1 秒）。
				vars.lg_ke = vars.lg_ke + 1;
				if (vars.lg_ke / 20 >= 1) {
					vars.lg_ke = 0;
					vars.LianGuJinDu = vars.LianGuJinDu + 1;
				}

				// 进度达到目标秒数 -> 产出。
				if (vars.LianGuJinDu >= LIANGU_SECONDS) {
					vars.LianGuJinDu = 0;
					vars.GuFang = 0;
					vars.LianGu = false;
					vars.markSyncDirty();

					LianGuMenuSlots.setSlot(player, 9, new ItemStack(Items.DIRT, 1));
					LianGuMenuSlots.setSlot(player, 10, ItemStack.EMPTY);

					if (!player.level().isClientSide()) {
						LOGGER.info("[{}] liangu finish ok: produced 1x DIRT", MODID);
					}
					return;
				}

				vars.markSyncDirty();
			}

			@Override
			public boolean isActive(Player player) {
				return ExternalLianGuRecipe.isRunning(player, EXAMPLE_GUFANG_ID);
			}

			private int findDiamondSlot(Player player) {
				if (INPUT_MODE == LianGuInputMode.SLOT0_ONLY) {
					return isDiamond(LianGuMenuSlots.getSlot(player, 0)) ? 0 : -1;
				}
				return LianGuMenuSlots.findFirstMatchInInputs(player, this::isDiamond);
			}

			private boolean isDiamond(ItemStack stack) {
				return !stack.isEmpty() && stack.getItem() == Items.DIAMOND;
			}
		});
	}
}
